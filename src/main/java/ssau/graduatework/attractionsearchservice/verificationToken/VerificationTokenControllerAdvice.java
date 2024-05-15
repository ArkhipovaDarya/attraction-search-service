package ssau.graduatework.attractionsearchservice.verificationToken;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssau.graduatework.attractionsearchservice.util.ResponseMessage;
import ssau.graduatework.attractionsearchservice.verificationToken.exception.TokenNotFoundException;

@RestControllerAdvice
public class VerificationTokenControllerAdvice {
    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleTokenNotFoundException(TokenNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
    }
}
