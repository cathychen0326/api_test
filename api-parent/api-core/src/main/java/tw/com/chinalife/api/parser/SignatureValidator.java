
package tw.com.chinalife.api.parser;

public interface SignatureValidator {
    boolean validateSignature(byte[] content, String headerSignature);
}
