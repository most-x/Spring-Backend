package kr.co.mostx.japi.response;

import kr.co.mostx.japi.exception.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ServeyErrorResponse {
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public ServeyErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getCode().value();
        this.error = errorCode.getStatus().name();
        this.code = errorCode.getCode().toString();
        this.message = errorCode.getMessage();
    }
}
