package org.simple.twitter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoteAppException.class)
    public ResponseEntity<Object> handleNoteAppException(NoteAppException ex) {
        Object body = new ResponseExceptionBody(ex.getMessage());
        return new ResponseEntity<>(body, ex.getHttpStatus());
    }

    @AllArgsConstructor
    @Data
    private class ResponseExceptionBody {
        String msg;
    }
}
