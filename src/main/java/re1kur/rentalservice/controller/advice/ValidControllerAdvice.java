package re1kur.rentalservice.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ValidControllerAdvice {

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
        return "/errors/valid-error";
    }
}
