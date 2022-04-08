#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import javax.inject.Inject;

public class QueryServerImpl implements QueryServer {

    ${aggregate_name_with_proper_case}DAO dao;

    @Inject
    QueryServerImpl(${aggregate_name_with_proper_case}DAO dao){
        this.dao = dao;
    }

    @Override
    public void startQueryServer() {
    }
}
