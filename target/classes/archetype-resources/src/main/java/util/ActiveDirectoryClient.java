#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import ${groupId}.grpc.ValidateActiveDirectoryDetailsRequest;
import ${package}.util.model.ADGroup;
import ${package}.util.model.User;

import javax.naming.NamingException;
import java.util.List;

public interface ActiveDirectoryClient {
    String[] userAttributes = {
            "distinguishedName", "cn", "name", "uid",
            "sn", "givenname", "memberOf", "sid",
            "samaccountname", "userPrincipalName",
    };

    List<User> getADUsers(ValidateActiveDirectoryDetailsRequest request) throws NamingException;

    List<ADGroup> getADGroups(ValidateActiveDirectoryDetailsRequest activeDirectoryDetails) throws NamingException;

    Boolean authenticateUser(ValidateActiveDirectoryDetailsRequest activeDirectoryDetails);
}
