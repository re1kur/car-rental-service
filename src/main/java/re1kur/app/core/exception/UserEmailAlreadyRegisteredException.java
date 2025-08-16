package re1kur.app.core.exception;

public class UserEmailAlreadyRegisteredException extends RuntimeException {
    public UserEmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
