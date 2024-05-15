package ssau.graduatework.attractionsearchservice.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssau.graduatework.attractionsearchservice.auth.exception.UserAlreadyExistsException;
import ssau.graduatework.attractionsearchservice.auth.exception.UserIsNotVerifiedException;
import ssau.graduatework.attractionsearchservice.util.ResponseMessage;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseMessage> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Данная почта уже занята!"));
    }

    @ExceptionHandler(UserIsNotVerifiedException.class)
    public ResponseEntity<ResponseMessage> handleUserIsNotVerifiedException(UserIsNotVerifiedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ResponseMessage("Почта не подтверждена!"));
    }
}
