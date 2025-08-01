package gift.exception;

import gift.exception.kakao.KakaoException;
import gift.exception.member.MemberException;
import gift.exception.product.ProductException;
import gift.exception.product.option.OptionException;
import gift.exception.wishList.WishException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>>
    handleResponseStatusError(ResponseStatusException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getReason());

        return ResponseEntity.status(e.getStatusCode()).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(ProductException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(errors);
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(MemberException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(errors);
    }

    @ExceptionHandler(WishException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(WishException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(errors);
    }

    @ExceptionHandler(OptionException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(OptionException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(errors);
    }

    @ExceptionHandler(KakaoException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(KakaoException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(errors);
    }

}
