package re1kur.app.exception;

public class UserEmailAlreadyRegisteredException extends RuntimeException {
    public UserEmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
