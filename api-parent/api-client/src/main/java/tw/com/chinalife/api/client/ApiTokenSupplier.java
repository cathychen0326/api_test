package tw.com.chinalife.api.client;

import java.util.function.Supplier;

/**
 * Special {@link Supplier} for Channel Access Token.
 *
 * <p>You can implement it to return same channel tokens.
 * Or refresh tokens internally.
 */
@FunctionalInterface
public interface ApiTokenSupplier extends Supplier<String> {
}
