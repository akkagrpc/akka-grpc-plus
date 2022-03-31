#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dagger;

import com.akkagrpc.grpc.MovieServicePowerApi;
import dagger.Binds;
import dagger.Module;
import ${package}.projection.*;
import ${package}.query.${capitalize_artifactId}DAO;
import ${package}.query.${capitalize_artifactId}DAOImpl;
import ${package}.query.QueryServer;
import ${package}.query.QueryServerImpl;

import ${package}.server.CommandServer;
import ${package}.server.CommandServerImpl;
import ${package}.server.${capitalize_artifactId}ServiceImpl;
import ${package}.util.*;

import javax.inject.Singleton;

@Module
public interface ${capitalize_artifactId}ManagementModule {

    @Binds
    @Singleton
    MovieServicePowerApi bindMovieServicePowerApi(${capitalize_artifactId}ServiceImpl impl);

    @Binds
    @Singleton
    ${capitalize_artifactId}DAO bind${capitalize_artifactId}DAO(${capitalize_artifactId}DAOImpl dao);

    @Binds
    @Singleton
    MicrometerClient bindMicrometerClient(MicrometerClientImpl client);

    @Singleton
    @Binds
    ActiveDirectoryClient bindActiveDirectoryClient(ActiveDirectoryClientImpl client);

    @Singleton
    @Binds
    ElasticSearchRestClient bindElasticSearchRestClientImpl(ElasticSearchRestClientImpl impl);

    @Singleton
    @Binds
    DBProjectionHandler bindDBProjectionHandler(DBProjectionHandlerImpl impl);

    @Singleton
    @Binds
    DBProjection bindDBProjection(DBProjectionImpl impl);

    @Singleton
    @Binds
    EventsProjection bindPublishEventsProjection(EventsProjectionImpl impl);

    @Singleton
    @Binds
    CommandServer bindCommandServer(CommandServerImpl impl);

    @Singleton
    @Binds
    QueryServer bindQueryServer(QueryServerImpl impl);
}

