package it.polito.ai.springserver.resource.exception;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<String> jsonParserExceptionHandler (Exception e, WebRequest request){
    return new ResponseEntity<>("The json associated is invalid", new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }
}
