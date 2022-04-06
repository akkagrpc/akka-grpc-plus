#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;

import akka.NotUsed;
import akka.Done;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.DispatcherSelector;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import akka.grpc.javadsl.Metadata;
import akka.stream.javadsl.Source;
import com.akkagrpc.grpc.*;
import com.google.common.base.Strings;
import com.google.protobuf.Empty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import ${package}.query.${aggregate_name_with_proper_case}DAO;
import ${package}.server.command.Disable${aggregate_name_with_proper_case};
import ${package}.server.command.Get${aggregate_name_with_proper_case};
import ${package}.server.command.Register${aggregate_name_with_proper_case};
import ${package}.server.reply.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ${package}.server.command.Command;
import ${package}.server.reply.Accepted;
import ${package}.server.reply.Confirmation;
import ${package}.server.reply.Rejected;
import ${package}.util.ActiveDirectoryClient;
import ${package}.util.MicrometerClient;
import ${package}.util.TokenVerifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import reactor.core.publisher.Flux;

@Singleton
public final class ${aggregate_name_with_proper_case}ServiceImpl implements ${aggregate_name_with_proper_case}ServicePowerApi {
    private final Logger logger = LoggerFactory.getLogger(${aggregate_name_with_proper_case}ServiceImpl.class);
    private final Duration askTimeout;
    private final ClusterSharding clusterSharding;
    private final Executor blockingJdbcExecutor;
    private final MeterRegistry meterRegistry;
    private final Counter ${aggregate_name_with_lower_case}Counter;
    private final ${aggregate_name_with_proper_case}DAO dao;
    private final ActiveDirectoryClient activeDirectoryClient;

    @Inject
    public ${aggregate_name_with_proper_case}ServiceImpl(ActorSystem<?> system, ${aggregate_name_with_proper_case}DAO dao,
                                     MicrometerClient micrometerClient, ActiveDirectoryClient activeDirectoryClient) {
        this.dao = dao;
        this.activeDirectoryClient = activeDirectoryClient;
        DispatcherSelector dispatcherSelector = DispatcherSelector.fromConfig("akka.persistence.r2dbc.journal.plugin-dispatcher");
        this.askTimeout = system.settings().config().getDuration("${artifactId}.ask-timeout");
        this.clusterSharding = ClusterSharding.get(system);
        this.blockingJdbcExecutor = system.dispatchers().lookup(dispatcherSelector);
        this.meterRegistry = micrometerClient.ixMonitoringSystem();
        this.${aggregate_name_with_lower_case}Counter = this.meterRegistry.counter("${aggregate_name_with_lower_case}", "count");

    }

    private EntityRef<Command> entityRef(String ${aggregate_name_with_lower_case}Id) {
        return clusterSharding.entityRefFor(${aggregate_name_with_proper_case}Aggregate.ENTITY_KEY, ${aggregate_name_with_lower_case}Id);
    }

    @Override
    public CompletionStage<Get${aggregate_name_with_proper_case}Response> get${aggregate_name_with_proper_case}(Get${aggregate_name_with_proper_case}Request in, Metadata metadata) {
        return entityRef(in.get${aggregate_name_with_proper_case}Id())
        .<Summary>ask(replyTo -> new Get${aggregate_name_with_proper_case}(in.get${aggregate_name_with_proper_case}Id(), replyTo), askTimeout)
        .thenApply(summary -> Get${aggregate_name_with_proper_case}Response.newBuilder()
            .set${aggregate_name_with_proper_case}(com.akkagrpc.grpc.${aggregate_name_with_proper_case}.newBuilder()
            .set${aggregate_name_with_proper_case}Id(summary.get${aggregate_name_with_proper_case}Id())
            .setTitle(summary.getTitle())
            .setRating(summary.getRating())
            .setReleaseYear(summary.getReleaseYear())
            .setGenre(summary.getGenre())
            .build())
        .build());
    }

    @Override
    public Source<Get${aggregate_name_with_proper_case}sResponse, NotUsed> get${aggregate_name_with_proper_case}s(Empty in, Metadata metadata) {
        Flux<${package}.server.${aggregate_name_with_proper_case}> ${aggregate_name_with_lower_case}s = dao.get${aggregate_name_with_proper_case}s();
        return Source.from(${aggregate_name_with_lower_case}s.toIterable())
        .map(${aggregate_name_with_lower_case} -> {
        return Get${aggregate_name_with_proper_case}sResponse.newBuilder()
            .set${aggregate_name_with_proper_case}(com.akkagrpc.grpc.${aggregate_name_with_proper_case}.newBuilder()
            .set${aggregate_name_with_proper_case}Id(${aggregate_name_with_lower_case}.get${aggregate_name_with_proper_case}Id())
            .setTitle(${aggregate_name_with_lower_case}.getTitle())
            .setRating(${aggregate_name_with_lower_case}.getRating())
            .setReleaseYear(${aggregate_name_with_lower_case}.getReleaseYear())
            .setGenre(${aggregate_name_with_lower_case}.getGenre())
            .build())
        .build();
        });
    }

    @Override
    public CompletionStage<Register${aggregate_name_with_proper_case}Response> register${aggregate_name_with_proper_case}(Register${aggregate_name_with_proper_case}Request in, Metadata metadata) {
        ${aggregate_name_with_lower_case}Counter.increment(1.0);
        String ${aggregate_name_with_proper_case}Id = UUID.randomUUID().toString();
        return entityRef(${aggregate_name_with_proper_case}Id)
        .<Confirmation>ask(replyTo ->
        new Register${aggregate_name_with_proper_case}(in, "Anonymous", replyTo), askTimeout)
        .thenApply(this::handleConfirmation)
        .thenApply(summary -> Register${aggregate_name_with_proper_case}Response.newBuilder()
            .set${aggregate_name_with_proper_case}Id(summary.getSummary().get${aggregate_name_with_proper_case}Id())
        .build());
    }

    @Override
    public CompletionStage<Disable${aggregate_name_with_proper_case}Response> disable${aggregate_name_with_proper_case}(Disable${aggregate_name_with_proper_case}Request in,Metadata metadata){
        return entityRef(in.get${aggregate_name_with_proper_case}Id())
        .<Confirmation>ask(replyTo ->
        new Disable${aggregate_name_with_proper_case}(in.get${aggregate_name_with_proper_case}Id(), "Anonymous", replyTo), askTimeout)
        .thenApply(this::handleConfirmation)
        .thenApply(accepted -> Disable${aggregate_name_with_proper_case}Response.newBuilder()
            .setResponse(Done.getInstance().toString())
        .build());
    }

    private Accepted handleConfirmation(Confirmation confirmation) {
        if (confirmation instanceof Accepted) {
            return (Accepted) confirmation;
        }
        Rejected rejected = (Rejected) confirmation;
        throw new RuntimeException(rejected.getReason());
    }

    private Set<String> verifyTokenAndGetUserRoles(String token) {
        Jws<Claims> jwt = TokenVerifier.parseJwt(token);
        if (!Strings.isNullOrEmpty(jwt.getBody().toString())) {
            final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getBody().get("realm_access");
            final String username = jwt.getBody().get("preferred_username").toString();
            Set<String> roles = ((List<String>) realmAccess.get("roles")).stream().collect(Collectors.toSet());
            return roles;
        }
        return Collections.<String>emptySet();
    }

    private String getUserNameFromToken(String token) {
        Jws<Claims> jwt = TokenVerifier.parseJwt(token);
        if (!Strings.isNullOrEmpty(jwt.getBody().toString())) {
            return jwt.getBody().get("preferred_username").toString();
        }
        return "";
    }
}
