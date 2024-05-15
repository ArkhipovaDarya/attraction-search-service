package ssau.graduatework.attractionsearchservice.auth.exception;

public class UserIsNotVerifiedException extends RuntimeException {
    public UserIsNotVerifiedException() {
    }

    public UserIsNotVerifiedException(String message) {
        super(message);
    }

    public UserIsNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
