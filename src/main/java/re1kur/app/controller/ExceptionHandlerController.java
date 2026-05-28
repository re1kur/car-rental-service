package re1kur.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import re1kur.app.core.exception.CarIsNotAvailableException;
import re1kur.app.core.exception.CarIsOccupiedException;
import re1kur.app.core.exception.FileNotFoundException;

import java.io.IOException;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(
            MethodArgumentNotValidException ex,
            Model model,
            HttpServletRequest req
    ) {
        model.addAttribute("referer", req.getHeader("referer"));
        model.addAttribute("errors", ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(toList()));
        return "errors/valid-error";
    }

    @ExceptionHandler({CarIsNotAvailableException.class, CarIsOccupiedException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleRentConflict(RuntimeException ex, Model model, HttpServletRequest req) {
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("referer", req.getHeader("referer"));
        return "errors/conflict";
    }

    @ExceptionHandler(exception = IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file, file not reading: " + ex.getMessage());
    }

    @ExceptionHandler(exception = FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
