package ssau.graduatework.attractionsearchservice.verificationToken.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
    }

    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
