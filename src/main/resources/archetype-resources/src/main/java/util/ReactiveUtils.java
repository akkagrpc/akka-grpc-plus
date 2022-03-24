#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utilities for working with Reactive flows.
 */
public final class ReactiveUtils {

    private ReactiveUtils() {
    }

    /**
     * Execute the {@link Publisher} provided by a {@link Supplier} and propagate the error that initiated this behavior.  Typically used with {@link Flux${symbol_pound}onErrorResume(Function)} and
     * {@link Mono${symbol_pound}onErrorResume(Function)}.
     *
     * @param s   a {@link Supplier} of a {@link Publisher} to execute when an error occurs
     * @param <T> the type passing through the flow
     * @return a {@link Mono${symbol_pound}error(Throwable)} with the original error
     * @see Flux${symbol_pound}onErrorResume(Function)
     * @see Mono${symbol_pound}onErrorResume(Function)
     */
    public static <T> Function<Throwable, Mono<T>> appendError(Supplier<Publisher<?>> s) {
        Assert.requireNonNull(s, "s must not be null");

        return t ->
            Flux.from(s.get())
                .then(Mono.error(t));
    }

    /**
     * Convert a {@code Publisher<Void>} to a {@code Publisher<T>} allowing for type passthrough behavior.
     *
     * @param s   a {@link Supplier} of a {@link Publisher} to execute
     * @param <T> the type passing through the flow
     * @return {@link Mono${symbol_pound}empty()} of the appropriate type
     */
    public static <T> Mono<T> typeSafe(Supplier<Publisher<Void>> s) {
        Assert.requireNonNull(s, "s must not be null");

        return Flux.from(s.get())
            .then(Mono.empty());
    }

}
