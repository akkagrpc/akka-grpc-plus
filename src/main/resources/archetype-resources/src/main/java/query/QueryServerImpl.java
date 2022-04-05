#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import javax.inject.Inject;

public class QueryServerImpl implements QueryServer {

    ${first_word_of_artifactId}DAO dao;

    @Inject
    QueryServerImpl(${first_word_of_artifactId}DAO dao){
        this.dao = dao;
    }

    @Override
    public void startQueryServer() {
    }
}
