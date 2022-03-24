#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.command;

import ${package}.util.CborSerializable;

public interface Command<R> extends CborSerializable {
}