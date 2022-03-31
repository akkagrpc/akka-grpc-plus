#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.typesafe.config.Config;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import ${package}.server.${capitalize_artifactId};
import ${package}.util.${capitalize_artifactId}ORM;
import ${package}.util.R2dbc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ${capitalize_artifactId}DAOImpl implements ${capitalize_artifactId}DAO {

    private final ConnectionFactory connectionFactory;


    @Inject
    public ${capitalize_artifactId}DAOImpl(Config config) {

        String driver = config.getString("service.dao.driver");
        String host = config.getString("service.dao.host");
        int port = config.getInt("service.dao.port");
        String database = config.getString("service.dao.database");
        String user = config.getString("service.dao.user");
        String password = config.getString("service.dao.password");

        final ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, driver)
                .option(ConnectionFactoryOptions.HOST, host)
                .option(ConnectionFactoryOptions.PORT, port)
                .option(ConnectionFactoryOptions.DATABASE, database)
                .option(ConnectionFactoryOptions.USER, user)
                .option(ConnectionFactoryOptions.PASSWORD, password)
                .build();

        connectionFactory = ConnectionFactories.get(options);
    }

    @Override
    public final Mono<${capitalize_artifactId}> getMovieById(String movieId) {
        return Mono.usingWhen(connectionFactory.create(),
                connection ->
                        Mono.from(connection.createStatement("SELECT * FROM public.movie WHERE movieid = $1")
                                        .bind("$1", movieId)
                                        .execute())
                                .map(result -> result.map((row, rowMetadata) -> ${capitalize_artifactId}ORM.mapRowTo${capitalize_artifactId}(row)))
                                .flatMap(pub -> Mono.from(pub)),
                Connection::close);
    }

    @Override
    public final Source<${capitalize_artifactId}, NotUsed> getMovieByTemplateId(String movieId) {
        R2dbc r2dbc = new R2dbc(connectionFactory);
        return Source.fromPublisher(r2dbc.withHandle(
                handle -> {
                    return handle.select("SELECT * FROM public.movie WHERE movieid = $1")
                            .bind("$1", movieId)
                            .mapResult(result -> result.map((row, rowMetadata) -> ${capitalize_artifactId}ORM.mapRowTo${capitalize_artifactId}(row)));
                }
        ));
    }

    @Override
    public final Flux<${capitalize_artifactId}> getMovies() {
        R2dbc r2dbc = new R2dbc(connectionFactory);
        return r2dbc.withHandle(
                handle -> {
                    return handle.select("SELECT * FROM public.movie")
                            .mapResult(result -> result.map((row, rowMetadata) -> ${capitalize_artifactId}ORM.mapRowTo${capitalize_artifactId}(row)));
                }
        );
    }
}
