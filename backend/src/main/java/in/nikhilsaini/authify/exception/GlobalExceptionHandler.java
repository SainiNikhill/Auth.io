package in.nikhilsaini.authify.exception;



import in.nikhilsaini.authify.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiError> build(HttpStatus status, String message, String code, String path){
        ApiError err = ApiError.builder()
                .success(false)
                .message(message)
                .errorCode(code)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(err, status);
    }

    // validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex , HttpServletRequest req) {
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return build(HttpStatus.BAD_REQUEST, msg , "VALIDATION ERRORS" , req.getRequestURI());
    }
    // Illegal arguments like (throw new IllegalArgumentException())
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), "BAD_REQUEST", req.getRequestURI());
    }

    // JPA / Database errors
    @ExceptionHandler(jakarta.persistence.PersistenceException.class)
    public ResponseEntity<ApiError> handleDB(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "DATABASE_ERROR", req.getRequestURI());
    }

    // Catch all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!", "SERVER_ERROR", req.getRequestURI());
    }
}

