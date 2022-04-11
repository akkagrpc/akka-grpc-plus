#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dagger;

import com.akkagrpc.${aggregate_name_with_lower_case}.${aggregate_name_with_proper_case}ServicePowerApi;
import dagger.Binds;
import dagger.Module;
import ${package}.projection.*;
import ${package}.query.${aggregate_name_with_proper_case}DAO;
import ${package}.query.${aggregate_name_with_proper_case}DAOImpl;
import ${package}.query.QueryServer;
import ${package}.query.QueryServerImpl;

import ${package}.server.CommandServer;
import ${package}.server.CommandServerImpl;
import ${package}.server.${aggregate_name_with_proper_case}ServiceImpl;
import ${package}.util.*;

import javax.inject.Singleton;

@Module
public interface ${aggregate_name_with_proper_case}ManagementModule {

    @Binds
    @Singleton
    ${aggregate_name_with_proper_case}ServicePowerApi bind${aggregate_name_with_proper_case}ServicePowerApi(${aggregate_name_with_proper_case}ServiceImpl impl);

    @Binds
    @Singleton
    ${aggregate_name_with_proper_case}DAO bind${aggregate_name_with_proper_case}DAO(${aggregate_name_with_proper_case}DAOImpl dao);

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

