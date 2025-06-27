package re1kur.fs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re1kur.fs.exceptions.FileNotFoundException;
import re1kur.fs.exceptions.UrlUpdateException;

import java.io.IOException;

@RestControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler(exception = IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file, file not reading: " + ex.getMessage());
    }

    @ExceptionHandler(exception = FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(exception = UrlUpdateException.class)
    public ResponseEntity<String> handleUrlUpdateException(UrlUpdateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
