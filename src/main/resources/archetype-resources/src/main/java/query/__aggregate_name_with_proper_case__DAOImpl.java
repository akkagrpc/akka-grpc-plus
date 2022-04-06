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
import ${package}.server.${aggregate_name_with_proper_case};
import ${package}.util.${aggregate_name_with_proper_case}ORM;
import ${package}.util.R2dbc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ${aggregate_name_with_proper_case}DAOImpl implements ${aggregate_name_with_proper_case}DAO {

    private final ConnectionFactory connectionFactory;


    @Inject
    public ${aggregate_name_with_proper_case}DAOImpl(Config config) {

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
    public final Mono<${aggregate_name_with_proper_case}> get${aggregate_name_with_proper_case}ById(String ${aggregate_name_with_lower_case}Id) {
        return Mono.usingWhen(connectionFactory.create(),
                connection ->
                        Mono.from(connection.createStatement("SELECT * FROM public.${aggregate_name_with_lower_case} WHERE ${aggregate_name_with_lower_case}id = $1")
                                        .bind("$1", ${aggregate_name_with_lower_case}Id)
                                        .execute())
                                .map(result -> result.map((row, rowMetadata) -> ${aggregate_name_with_proper_case}ORM.mapRowTo${aggregate_name_with_proper_case}(row)))
                                .flatMap(pub -> Mono.from(pub)),
                Connection::close);
    }

    @Override
    public final Source<${aggregate_name_with_proper_case}, NotUsed> get${aggregate_name_with_proper_case}ByTemplateId(String ${aggregate_name_with_lower_case}Id) {
        R2dbc r2dbc = new R2dbc(connectionFactory);
        return Source.fromPublisher(r2dbc.withHandle(
                handle -> {
                    return handle.select("SELECT * FROM public.${aggregate_name_with_lower_case} WHERE ${aggregate_name_with_lower_case}id = $1")
                            .bind("$1", ${aggregate_name_with_lower_case}Id)
                            .mapResult(result -> result.map((row, rowMetadata) -> ${aggregate_name_with_proper_case}ORM.mapRowTo${aggregate_name_with_proper_case}(row)));
                }
        ));
    }

    @Override
    public final Flux<${aggregate_name_with_proper_case}> get${aggregate_name_with_proper_case}s() {
        R2dbc r2dbc = new R2dbc(connectionFactory);
        return r2dbc.withHandle(
                handle -> {
                    return handle.select("SELECT * FROM public.${aggregate_name_with_lower_case}")
                            .mapResult(result -> result.map((row, rowMetadata) -> ${aggregate_name_with_proper_case}ORM.mapRowTo${aggregate_name_with_proper_case}(row)));
                }
        );
    }
}
