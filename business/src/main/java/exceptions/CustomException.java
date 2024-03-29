<<<<<<< HEAD
package exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.message.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Component
@ControllerAdvice
public class CustomException extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        ErrorMessage errorMessage;
        if (mostSpecificCause instanceof InvalidFormatException) {
            String exceptionPlace = ((InvalidFormatException) mostSpecificCause).getPathReference();
            String message = ((InvalidFormatException) mostSpecificCause).getOriginalMessage();
            errorMessage = new ErrorMessage(exceptionPlace, message);
        } else {
            errorMessage = new ErrorMessage(ex.getMessage());
        }
        return new ResponseEntity(errorMessage, headers, status);
    }

=======
package exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.message.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Component
@ControllerAdvice
public class CustomException extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        ErrorMessage errorMessage;
        if (mostSpecificCause instanceof InvalidFormatException) {
            String exceptionPlace = ((InvalidFormatException) mostSpecificCause).getPathReference();
            String message = ((InvalidFormatException) mostSpecificCause).getOriginalMessage();
            errorMessage = new ErrorMessage(exceptionPlace, message);
        } else {
            errorMessage = new ErrorMessage(ex.getMessage());
        }
        return new ResponseEntity(errorMessage, headers, status);
    }

>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
}