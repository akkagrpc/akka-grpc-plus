#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import javax.inject.Inject;

public class QueryServerImpl implements QueryServer {

    MovieDAO dao;

    @Inject
    QueryServerImpl(MovieDAO dao){
        this.dao = dao;
    }

    @Override
    public void startQueryServer() {
    }
}
