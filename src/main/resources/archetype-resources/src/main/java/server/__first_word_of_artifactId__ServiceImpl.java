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
import ${package}.query.${first_word_of_artifactId}DAO;
import ${package}.server.command.Disable${first_word_of_artifactId};
import ${package}.server.command.Get${first_word_of_artifactId};
import ${package}.server.command.Register${first_word_of_artifactId};
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
public final class ${first_word_of_artifactId}ServiceImpl implements ${first_word_of_artifactId}ServicePowerApi {
    private final Logger logger = LoggerFactory.getLogger(${first_word_of_artifactId}ServiceImpl.class);
    private final Duration askTimeout;
    private final ClusterSharding clusterSharding;
    private final Executor blockingJdbcExecutor;
    private final MeterRegistry meterRegistry;
    private final Counter ${package}Counter;
    private final ${first_word_of_artifactId}DAO dao;
    private final ActiveDirectoryClient activeDirectoryClient;

    @Inject
    public ${first_word_of_artifactId}ServiceImpl(ActorSystem<?> system, ${first_word_of_artifactId}DAO dao,
                                     MicrometerClient micrometerClient, ActiveDirectoryClient activeDirectoryClient) {
        this.dao = dao;
        this.activeDirectoryClient = activeDirectoryClient;
        DispatcherSelector dispatcherSelector = DispatcherSelector.fromConfig("akka.persistence.r2dbc.journal.plugin-dispatcher");
        this.askTimeout = system.settings().config().getDuration("${artifactId}.ask-timeout");
        this.clusterSharding = ClusterSharding.get(system);
        this.blockingJdbcExecutor = system.dispatchers().lookup(dispatcherSelector);
        this.meterRegistry = micrometerClient.ixMonitoringSystem();
        this.${package}Counter = this.meterRegistry.counter("${package}", "genre");

    }

    private EntityRef<Command> entityRef(String ${package}Id) {
        return clusterSharding.entityRefFor(${first_word_of_artifactId}Aggregate.ENTITY_KEY, ${package}Id);
    }

    @Override
    public CompletionStage<Get${first_word_of_artifactId}Response> get${first_word_of_artifactId}(Get${first_word_of_artifactId}Request in, Metadata metadata) {
        return entityRef(in.get${first_word_of_artifactId}Id())
        .<Summary>ask(replyTo -> new Get${first_word_of_artifactId}(in.get${first_word_of_artifactId}Id(), replyTo), askTimeout)
        .thenApply(summary -> Get${first_word_of_artifactId}Response.newBuilder()
            .set${first_word_of_artifactId}(com.akkagrpc.grpc.${first_word_of_artifactId}.newBuilder()
            .set${first_word_of_artifactId}Id(summary.get${first_word_of_artifactId}Id())
            .setTitle(summary.getTitle())
            .setRating(summary.getRating())
            .setReleaseYear(summary.getReleaseYear())
            .setGenre(summary.getGenre())
            .build())
        .build());
    }

    @Override
    public Source<Get${first_word_of_artifactId}sResponse, NotUsed> get${first_word_of_artifactId}s(Empty in, Metadata metadata) {
        Flux<${package}.server.${first_word_of_artifactId}> ${package}s = dao.get${first_word_of_artifactId}s();
        return Source.from(${package}s.toIterable())
        .map(${package} -> {
        return Get${first_word_of_artifactId}sResponse.newBuilder()
            .set${first_word_of_artifactId}(com.akkagrpc.grpc.${first_word_of_artifactId}.newBuilder()
            .set${first_word_of_artifactId}Id(${package}.get${first_word_of_artifactId}Id())
            .setTitle(${package}.getTitle())
            .setRating(${package}.getRating())
            .setReleaseYear(${package}.getReleaseYear())
            .setGenre(${package}.getGenre())
            .build())
        .build();
        });
    }

    @Override
    public CompletionStage<Register${first_word_of_artifactId}Response> register${first_word_of_artifactId}(Register${first_word_of_artifactId}Request in, Metadata metadata) {
        ${package}Counter.increment(1.0);
        String templateId = UUID.randomUUID().toString();
        return entityRef(templateId)
        .<Confirmation>ask(replyTo ->
        new Register${first_word_of_artifactId}(in, "Anonymous", replyTo), askTimeout)
        .thenApply(this::handleConfirmation)
        .thenApply(summary -> Register${first_word_of_artifactId}Response.newBuilder()
            .set${first_word_of_artifactId}Id(summary.getSummary().get${first_word_of_artifactId}Id())
        .build());
    }

    @Override
    public CompletionStage<Disable${first_word_of_artifactId}Response> disable${first_word_of_artifactId}(Disable${first_word_of_artifactId}Request in,Metadata metadata){
        return entityRef(in.get${first_word_of_artifactId}Id())
        .<Confirmation>ask(replyTo ->
        new Disable${first_word_of_artifactId}(in.get${first_word_of_artifactId}Id(), "Anonymous", replyTo), askTimeout)
        .thenApply(this::handleConfirmation)
        .thenApply(accepted -> Disable${first_word_of_artifactId}Response.newBuilder()
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
