#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

/**
 * Marker trait for serialization with Jackson CBOR. Enabled in serialization.conf
 * `akka.actor.serialization-bindings` (via application.conf).
 */
public interface CborSerializable {}
