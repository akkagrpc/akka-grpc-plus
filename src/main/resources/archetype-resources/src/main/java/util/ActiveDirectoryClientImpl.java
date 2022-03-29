#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;


import com.akkagrpc.grpc.ValidateActiveDirectoryDetailsRequest;
import ${package}.util.model.ADGroup;
import ${package}.util.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.List;
import java.util.Properties;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

@Singleton
public class ActiveDirectoryClientImpl implements ActiveDirectoryClient {


    @Inject
    public ActiveDirectoryClientImpl() {
    }

    DirContext getActiveDirectoryContext(ValidateActiveDirectoryDetailsRequest activeDirectoryDetails) throws NamingException {
        Properties initialProperties = new Properties();
        initialProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        initialProperties.put(Context.PROVIDER_URL, "ldap://" + activeDirectoryDetails.getIpAddress() + ":" + activeDirectoryDetails.getPort());
        initialProperties.put(Context.SECURITY_PRINCIPAL, activeDirectoryDetails.getUsername() + "@" + activeDirectoryDetails.getDomainName());
        initialProperties.put(Context.SECURITY_CREDENTIALS, activeDirectoryDetails.getPassword());
        DirContext context = new InitialDirContext(initialProperties);
        return context;
    }

    String toDC(String domainName) {
        StringBuilder builder = new StringBuilder();
        for (String token : domainName.split("${symbol_escape}${symbol_escape}.")) {
            if (token.length() == 0) continue;   // defensive check
            if (builder.length() > 0) builder.append(",");
            builder.append("DC=").append(token);
        }
        return builder.toString();
    }

    @Override
    public List<ADGroup> getADGroups(ValidateActiveDirectoryDetailsRequest activeDirectoryDetails) throws NamingException {
        DirContext context = getActiveDirectoryContext(activeDirectoryDetails);
        List<ADGroup> adGroups = new java.util.ArrayList<>();
        SearchResult searchResult;
        String authenticatedUser = (String) context.getEnvironment().get(Context.SECURITY_PRINCIPAL);
        if (authenticatedUser.contains("@")) {
            String domainName = authenticatedUser.substring(authenticatedUser.indexOf("@") + 1);
            System.out.println("domainName is..." + domainName);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SUBTREE_SCOPE);
            controls.setReturningAttributes(null);
            String filter = "(objectClass=group)";
            NamingEnumeration<SearchResult> answer = context.search("DC=npb,DC=local", filter, controls);
            try {
                while (answer.hasMore()) {
                    try {
                        searchResult = answer.next();
                        Attributes attr = searchResult.getAttributes();
                        System.out.println("attributes are..." + attr);
                        if (attr != null && attr.get("SamAccountName") != null && adGroups.size() < 20) {
                            adGroups.add(new ADGroup(attr));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // throw e;
            }
        }
        return adGroups;
    }

    @Override
    public List<User> getADUsers(ValidateActiveDirectoryDetailsRequest activeDirectoryDetails) throws NamingException {
        DirContext context = getActiveDirectoryContext(activeDirectoryDetails);
        List<User> users = new java.util.ArrayList<>();
        SearchResult searchResult;
        String authenticatedUser = (String) context.getEnvironment().get(Context.SECURITY_PRINCIPAL);
        if (authenticatedUser.contains("@")) {
            String domainName = authenticatedUser.substring(authenticatedUser.indexOf("@") + 1);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SUBTREE_SCOPE);
            controls.setReturningAttributes(ActiveDirectoryClientImpl.userAttributes);
            NamingEnumeration<SearchResult> answer = context.search("DC=npb,DC=local", "(objectClass=user)", controls);
            try {
                while (answer.hasMore()) {
                    try {
                        searchResult = answer.next();
                        Attributes attr = searchResult.getAttributes();
                        if (attr != null && attr.get("userPrincipalName") != null && users.size() < 20) {
                            users.add(new User(attr));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                // throw e;
            }
        }
        return users;
    }

    /* use this to authenticate any existing user */
    @Override
    public Boolean authenticateUser(ValidateActiveDirectoryDetailsRequest activeDirectoryDetails) {
        try {
            Properties initialProperties = new Properties();
            initialProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            initialProperties.put(Context.PROVIDER_URL, "ldap://" + activeDirectoryDetails.getIpAddress() + ":" + activeDirectoryDetails.getPort());
            initialProperties.put(Context.SECURITY_PRINCIPAL, activeDirectoryDetails.getUsername() + "@" + activeDirectoryDetails.getDomainName());
            initialProperties.put(Context.SECURITY_CREDENTIALS, activeDirectoryDetails.getPassword());
            DirContext context = new InitialDirContext(initialProperties);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
