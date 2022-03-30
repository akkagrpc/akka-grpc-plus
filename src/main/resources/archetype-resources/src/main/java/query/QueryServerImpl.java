#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import javax.inject.Inject;

public class QueryServerImpl implements QueryServer {

    ${capitalize_artifactId}DAO dao;

    @Inject
    QueryServerImpl(${capitalize_artifactId}DAO dao){
        this.dao = dao;
    }

    @Override
    public void startQueryServer() {
    }
}
