#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;

import akka.NotUsed;
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
import ${package}.query.${capitalize_artifactId}DAO;
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
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Singleton
public final class ${capitalize_artifactId}ServiceImpl implements MovieServicePowerApi {
    private final Logger logger = LoggerFactory.getLogger(${capitalize_artifactId}ServiceImpl.class);
    private final Duration askTimeout;
    private final ClusterSharding clusterSharding;
    private final Executor blockingJdbcExecutor;
    private final MeterRegistry meterRegistry;
    private final Counter movieCounter;
    private final ${capitalize_artifactId}DAO dao;
    private final ActiveDirectoryClient activeDirectoryClient;

    @Inject
    public ${capitalize_artifactId}ServiceImpl(ActorSystem<?> system, ${capitalize_artifactId}DAO dao,
                                     MicrometerClient micrometerClient, ActiveDirectoryClient activeDirectoryClient) {
        this.dao = dao;
        this.activeDirectoryClient = activeDirectoryClient;
        DispatcherSelector dispatcherSelector = DispatcherSelector.fromConfig("akka.persistence.r2dbc.journal.plugin-dispatcher");
        this.askTimeout = system.settings().config().getDuration("secure-template-service.ask-timeout");
        this.clusterSharding = ClusterSharding.get(system);
        this.blockingJdbcExecutor = system.dispatchers().lookup(dispatcherSelector);
        this.meterRegistry = micrometerClient.ixMonitoringSystem();
        this.movieCounter = this.meterRegistry.counter("movie", "genre");

    }

    private EntityRef<Command> entityRef(String movieId) {
        return clusterSharding.entityRefFor(${capitalize_artifactId}Aggregate.ENTITY_KEY, movieId);
    }

    @Override
    public CompletionStage<GetMovieResponse> getMovie(GetMovieRequest in, Metadata metadata) {
        return null;
    }

    @Override
    public Source<GetMoviesResponse, NotUsed> getMovies(Empty in, Metadata metadata) {
        return null;
    }

    @Override
    public CompletionStage<RegisterMovieResponse> registerMovie(RegisterMovieRequest in, Metadata metadata) {
        return null;
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
