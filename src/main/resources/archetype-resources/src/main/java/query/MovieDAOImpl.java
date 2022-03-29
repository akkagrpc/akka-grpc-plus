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
import ${package}.server.Movie;
import ${package}.util.MovieORM;
import ${package}.util.R2dbc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieDAOImpl implements MovieDAO {

    private final ConnectionFactory connectionFactory;


    @Inject
    public MovieDAOImpl(Config config) {

        String driver = config.getString("secure-template-service.dao.driver");
        String host = config.getString("secure-template-service.dao.host");
        int port = config.getInt("secure-template-service.dao.port");
        String database = config.getString("secure-template-service.dao.database");
        String user = config.getString("secure-template-service.dao.user");
        String password = config.getString("secure-template-service.dao.password");

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
    public final Mono<Movie> getMovieById(String movieId) {
        return Mono.usingWhen(connectionFactory.create(),
                connection ->
                        Mono.from(connection.createStatement("SELECT * FROM public.movie WHERE movieid = $1")
                                        .bind("$1", movieId)
                                        .execute())
                                .map(result -> result.map((row, rowMetadata) -> MovieORM.mapRowToMovie(row)))
                                .flatMap(pub -> Mono.from(pub)),
                Connection::close);
    }

    @Override
    public final Source<Movie, NotUsed> getMovieByTemplateId(String movieId) {
        R2dbc r2dbc = new R2dbc(connectionFactory);
        return Source.fromPublisher(r2dbc.withHandle(
                handle -> {
                    return handle.select("SELECT * FROM public.movie WHERE movieid = $1")
                            .bind("$1", movieId)
                            .mapResult(result -> result.map((row, rowMetadata) -> MovieORM.mapRowToMovie(row)));
                }
        ));
    }

    @Override
    public final Flux<Movie> getMovies() {
        R2dbc r2dbc = new R2dbc(connectionFactory);
        return r2dbc.withHandle(
                handle -> {
                    return handle.select("SELECT * FROM public.movie")
                            .mapResult(result -> result.map((row, rowMetadata) -> MovieORM.mapRowToMovie(row)));
                }
        );
    }
}
