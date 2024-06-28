package kr.co.mostx.japi.advice;

import kr.co.mostx.japi.exception.CustomException;
import kr.co.mostx.japi.exception.ErrorCode;
import kr.co.mostx.japi.response.ServeyErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ServeyExceptionAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServeyErrorResponse> illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus().value())
                .body(new ServeyErrorResponse(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ServeyErrorResponse> duplicateServeyExceptionAdvice(CustomException e) {
        return ResponseEntity
                .status(ErrorCode.SERVEY_ALREADY_COMPLETE.getStatus().value())
                .body(new ServeyErrorResponse(ErrorCode.SERVEY_ALREADY_COMPLETE));
    }
}
