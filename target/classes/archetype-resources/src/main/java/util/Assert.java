#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import reactor.util.annotation.Nullable;

/**
 * Assertion library for the implementation.
 */
public final class Assert {

    private Assert() {
    }

    /**
     * Checks that a specified object reference is not {@code null} and throws a customized {@link IllegalArgumentException} if it is.
     *
     * @param t       the object reference to check for nullity
     * @param message the detail message to be used in the event that an {@link IllegalArgumentException} is thrown
     * @param <T>     the type of the reference
     * @return {@code t} if not {@code null}
     * @throws IllegalArgumentException if {@code t} is {code null}
     */
    public static <T> T requireNonNull(@Nullable T t, String message) {
        if (t == null) {
            throw new IllegalArgumentException(message);
        }

        return t;
    }

}
