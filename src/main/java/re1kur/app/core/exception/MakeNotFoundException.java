package re1kur.app.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MakeNotFoundException extends RuntimeException {
  public MakeNotFoundException(String message) {
    super(message);
  }
}
