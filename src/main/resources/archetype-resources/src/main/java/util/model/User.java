#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util.model;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/**
 * Used to represent a User in Active Directory
 */
public class User {
    private String distinguishedName;
    private String userPrincipal;
    private String commonName;
    private String firstName;
    private String lastName;
    private String sAMAccountName;

    public User(Attributes attr) throws NamingException {

        userPrincipal = (String) attr.get("userPrincipalName").get();
        commonName = (String) attr.get("cn").get();
        distinguishedName = (String) attr.get("distinguishedName").get();
        try {
            lastName = (String) attr.get("sn").get();
        } catch (Exception e) {
            lastName = "";
        }
        try {
            firstName = (String) attr.get("givenName").get();
        } catch (Exception e) {
            firstName = "";
        }
        try {
            sAMAccountName = (String) attr.get("sAMAccountName").get();
        } catch (Exception e) {
            sAMAccountName = "";// TODO: handle exception
        }
    }

    public String getsAMAccountName() {
        return sAMAccountName;
    }

    public String getFirstName() {
        if (firstName == null || firstName.equals(""))
            return "";
        return firstName;
    }

    public String getLastName() {
        if (lastName == null || lastName.equals(""))
            return "";
        return lastName;
    }

    public String getUserPrincipal() {
        return userPrincipal;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public String toString() {
        return getDistinguishedName();
    }
}
