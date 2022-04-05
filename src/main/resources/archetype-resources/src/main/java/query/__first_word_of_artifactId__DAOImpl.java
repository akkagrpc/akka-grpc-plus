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
import ${package}.server.${first_word_of_artifactId};
import ${package}.util.${first_word_of_artifactId}ORM;
import ${package}.util.R2dbc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ${first_word_of_artifactId}DAOImpl implements ${first_word_of_artifactId}DAO {

    private final ConnectionFactory connectionFactory;


    @Inject
    public ${first_word_of_artifactId}DAOImpl(Config config) {

        String driver = config.getString("${artifactId}.dao.driver");
        String host = config.getString("${artifactId}.dao.host");
        int port = config.getInt("${artifactId}.dao.port");
        String database = config.getString("${artifactId}.dao.database");
        String user = config.getString("${artifactId}.dao.user");
        String password = config.getString("${artifactId}.dao.password");

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
    public final Mono<${first_word_of_artifactId}> get${first_word_of_artifactId}ById(String ${package}Id) {
        return Mono.usingWhen(connectionFactory.create(),
                connection ->
                        Mono.from(connection.createStatement("SELECT * FROM public.${package} WHERE ${package}id = $1")
                                        .bind("$1", ${package}Id)
                                        .execute())
                                .map(result -> result.map((row, rowMetadata) -> ${first_word_of_artifactId}ORM.mapRowTo${first_word_of_artifactId}(row)))
                                .flatMap(pub -> Mono.from(pub)),
                Connection::close);
    }

    @Override
    public final Source<${first_word_of_artifactId}, NotUsed> get${first_word_of_artifactId}ByTemplateId(String ${package}Id) {
        R2dbc r2dbc = new R2dbc(connectionFactory);
        return Source.fromPublisher(r2dbc.withHandle(
                handle -> {
                    return handle.select("SELECT * FROM public.${package} WHERE ${package}id = $1")
                            .bind("$1", ${package}Id)
                            .mapResult(result -> result.map((row, rowMetadata) -> ${first_word_of_artifactId}ORM.mapRowTo${first_word_of_artifactId}(row)));
                }
        ));
    }

    @Override
    public final Flux<${first_word_of_artifactId}> get${first_word_of_artifactId}s() {
        R2dbc r2dbc = new R2dbc(connectionFactory);
        return r2dbc.withHandle(
                handle -> {
                    return handle.select("SELECT * FROM public.${package}")
                            .mapResult(result -> result.map((row, rowMetadata) -> ${first_word_of_artifactId}ORM.mapRowTo${first_word_of_artifactId}(row)));
                }
        );
    }
}
