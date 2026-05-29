package re1kur.app.exception;

public class CarIsNotAvailableException extends RuntimeException {
    public CarIsNotAvailableException(String message) {
        super(message);
    }
}
