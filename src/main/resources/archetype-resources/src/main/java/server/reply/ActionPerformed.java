#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.reply;

public class ActionPerformed implements Confirmation {
    public final String description;
    public ActionPerformed(String description) {
        this.description = description;
    }
}
