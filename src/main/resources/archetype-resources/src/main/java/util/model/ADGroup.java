#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util.model;

import javax.naming.directory.Attributes;

/**
 * Used to represent a Group in Active Directory
 */
public class ADGroup {
    private String firstName;
    private String sAMAccountName;

    public ADGroup(Attributes attr) throws javax.naming.NamingException {
        try {
            firstName = (String) attr.get("Name").get();
        } catch (Exception e) {
            firstName = "";
        }
        try {
            sAMAccountName = (String) attr.get("SamAccountName").get();
        } catch (Exception e) {
            sAMAccountName = "";
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
}
