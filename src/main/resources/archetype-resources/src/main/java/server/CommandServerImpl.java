#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;

import akka.actor.typed.ActorSystem;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.cluster.typed.Cluster;
import akka.grpc.javadsl.ServerReflection;
import akka.grpc.javadsl.ServiceHandler;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.MediaTypes;
import akka.http.javadsl.model.headers.RawHeader;
import akka.http.javadsl.server.Route;
import akka.japi.function.Function;
import com.akkagrpc.grpc.${first_word_of_artifactId}Service;
import com.akkagrpc.grpc.${first_word_of_artifactId}ServicePowerApi;
import com.akkagrpc.grpc.${first_word_of_artifactId}ServicePowerApiHandlerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.typesafe.config.Config;
import scala.Option;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static akka.http.javadsl.server.Directives.*;

@Singleton
public class CommandServerImpl implements CommandServer {
    ActorSystem<?> system;
    ${first_word_of_artifactId}ServicePowerApi grpcService;

    @Inject
    public CommandServerImpl(ActorSystem<?> system, ${first_word_of_artifactId}ServicePowerApi grpcService) {
        this.system = system;
        this.grpcService = grpcService;
    }

    @Override
    public void startCommandServer() {
        Config config = system.settings().config();
        String host = config.getString("secure-template-service.grpc.interface");
        int port = config.getInt("secure-template-service.grpc.port");
        int dashboardPort = memberPort(Cluster.get(system).selfMember());

        Function<HttpRequest, CompletionStage<HttpResponse>> service =
                ServiceHandler.concatOrNotFound(${first_word_of_artifactId}ServicePowerApiHandlerFactory.create(grpcService, system),
                        ServerReflection.create(Collections.singletonList(${first_word_of_artifactId}Service.description), system));

        CompletionStage<ServerBinding> bound = Http.get(system).newServerAt(host, port)
                .bind(service);

        bound.whenComplete(
                (binding, ex) -> {
                    if (binding != null) {
                        binding.addToCoordinatedShutdown(Duration.ofSeconds(3), system);
                        InetSocketAddress address = binding.localAddress();
                        system.log().info(
                                "Secure template at gRPC server {}:{}",
                                address.getHostString(),
                                address.getPort());
                    } else {
                        system.log().error("Failed to bind gRPC endpoint, terminating system", ex);
                        system.terminate();
                    }
                });
        dashboardPort = dashboardPort + 7000;
        Http.get(system).newServerAt(host, dashboardPort).bind(route(system));
        system.log().info("Secure template management dashboard available on {}:{}", host, dashboardPort);
    }

    private int memberPort(Member member) {
        final Option<Object> portOption = member.address().port();
        return portOption.isDefined()
                ? Integer.parseInt(portOption.get().toString())
                : 0;
    }

    private Route route(ActorSystem<?> system) {
        return concat(
                path("", () -> getFromResource("dashboard.html", ContentTypes.TEXT_HTML_UTF8)),
                path("dashboard.html", () -> getFromResource("dashboard.html", ContentTypes.TEXT_HTML_UTF8)),
                path("dashboard.js", () -> getFromResource("dashboard.js", ContentTypes.APPLICATION_JSON)),
                path("p5.js", () -> getFromResource("p5.js", ContentTypes.APPLICATION_JSON)),
                path("favicon.ico", () -> getFromResource("favicon.ico", MediaTypes.IMAGE_X_ICON.toContentType())),
                path("cluster-state", () -> clusterState(system))
        );
    }

    private Route clusterState(ActorSystem<?> system) {
        return get(
                () -> respondWithHeader(RawHeader.create("Access-Control-Allow-Origin", "*"),
                        () -> complete(loadNodes(system).toJson()))
        );
    }

    private Nodes loadNodes(ActorSystem<?> actorSystem) {
        final Cluster cluster = Cluster.get(actorSystem);
        final ClusterEvent.CurrentClusterState clusterState = cluster.state();

        final Set<Member> unreachable = clusterState.getUnreachable();

        final Optional<Member> old = StreamSupport.stream(clusterState.getMembers().spliterator(), false)
                .filter(member -> member.status().equals(MemberStatus.up()))
                .filter(member -> !(unreachable.contains(member)))
                .reduce((older, member) -> older.isOlderThan(member) ? older : member);

        final Member oldest = old.orElse(cluster.selfMember());

        final List<Integer> seedNodePorts = seedNodePorts(actorSystem);

        final Nodes nodes = new Nodes(
                memberPort(cluster.selfMember()),
                cluster.selfMember().address().equals(clusterState.getLeader()),
                oldest.equals(cluster.selfMember()));

        StreamSupport.stream(clusterState.getMembers().spliterator(), false)
                .forEach(new Consumer<Member>() {
                    @Override
                    public void accept(Member member) {
                        nodes.add(member, leader(member), oldest(member), seedNode(member));
                    }

                    private boolean leader(Member member) {
                        return member.address().equals(clusterState.getLeader());
                    }

                    private boolean oldest(Member member) {
                        return oldest.equals(member);
                    }

                    private boolean seedNode(Member member) {
                        return seedNodePorts.contains(memberPort(member));
                    }
                });

        clusterState.getUnreachable()
                .forEach(nodes::addUnreachable);

        return nodes;
    }

    private static boolean isValidPort(int port) {
        return port >= 2551 && port <= 2559;
    }

    private static List<Integer> seedNodePorts(ActorSystem<?> actorSystem) {
        return actorSystem.settings().config().getList("akka.cluster.seed-nodes")
                .stream().map(s -> s.unwrapped().toString())
                .map(s -> {
                    final String[] split = s.split(":");
                    return split.length == 0 ? 0 : Integer.parseInt(split[split.length - 1]);
                }).collect(Collectors.toList());
    }

    public class Nodes implements Serializable {
        private static final long serialVersionUID = 1L;
        public final int selfPort;
        public final boolean leader;
        public final boolean oldest;
        public List<Node> nodes = new ArrayList<>();

        public Nodes(int selfPort, boolean leader, boolean oldest) {
            this.selfPort = selfPort;
            this.leader = leader;
            this.oldest = oldest;
        }

        void add(Member member, boolean leader, boolean oldest, boolean seedNode) {
            final int port = memberPort(member);
            if (isValidPort(port)) {
                nodes.add(new Node(port, state(member.status()), memberStatus(member.status()), leader, oldest, seedNode));
            }
        }

        void addUnreachable(Member member) {
            final int port = memberPort(member);
            if (isValidPort(port)) {
                Node node = new Node(port, "unreachable", "unreachable", false, false, false);
                nodes.remove(node);
                nodes.add(node);
            }
        }

        private static String state(MemberStatus memberStatus) {
            if (memberStatus.equals(MemberStatus.down())) {
                return "down";
            } else if (memberStatus.equals(MemberStatus.joining())) {
                return "starting";
            } else if (memberStatus.equals(MemberStatus.weaklyUp())) {
                return "starting";
            } else if (memberStatus.equals(MemberStatus.up())) {
                return "up";
            } else if (memberStatus.equals(MemberStatus.exiting())) {
                return "stopping";
            } else if (memberStatus.equals(MemberStatus.leaving())) {
                return "stopping";
            } else if (memberStatus.equals(MemberStatus.removed())) {
                return "stopping";
            } else {
                return "offline";
            }
        }

        private static String memberStatus(MemberStatus memberStatus) {
            if (memberStatus.equals(MemberStatus.down())) {
                return "down";
            } else if (memberStatus.equals(MemberStatus.joining())) {
                return "joining";
            } else if (memberStatus.equals(MemberStatus.weaklyUp())) {
                return "weaklyup";
            } else if (memberStatus.equals(MemberStatus.up())) {
                return "up";
            } else if (memberStatus.equals(MemberStatus.exiting())) {
                return "exiting";
            } else if (memberStatus.equals(MemberStatus.leaving())) {
                return "leaving";
            } else if (memberStatus.equals(MemberStatus.removed())) {
                return "removed";
            } else {
                return "unknown";
            }
        }

        String toJson() {
            final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                return ow.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return String.format("{ ${symbol_escape}"error${symbol_escape}" : ${symbol_escape}"%s${symbol_escape}" }", e.getMessage());
            }
        }
    }

    public static class Node implements Serializable {
        private static final long serialVersionUID = 1L;
        public final int port;
        public final String state;
        public final String memberState;
        public final boolean leader;
        public final boolean oldest;
        public final boolean seedNode;

        public Node(int port, String state, String memberState, boolean leader, boolean oldest, boolean seedNode) {
            this.port = port;
            this.state = state;
            this.memberState = memberState;
            this.leader = leader;
            this.oldest = oldest;
            this.seedNode = seedNode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(port, node.port);
        }

        @Override
        public int hashCode() {
            return Objects.hash(port);
        }
    }
}
