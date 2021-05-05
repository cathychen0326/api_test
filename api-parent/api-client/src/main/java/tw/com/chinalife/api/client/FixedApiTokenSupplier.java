package tw.com.chinalife.api.client;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Implementation of {@link ApiTokenSupplier} which always return fixed channel token.
 */
@AllArgsConstructor(staticName = "of")
@ToString
public final class FixedApiTokenSupplier implements ApiTokenSupplier {
    @NonNull
    private final String channelToken;

    @Override
    public String get() {
        return channelToken;
    }
}
