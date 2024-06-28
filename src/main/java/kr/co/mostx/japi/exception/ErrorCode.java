package kr.co.mostx.japi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(400), "잘못된 요청입니다."),

    /**
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, HttpStatusCode.valueOf(404), "정보를 찾을 수 없습니다."),

    /**
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, HttpStatusCode.valueOf(405), "잘못된 메서드입니다."),

    /**
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatusCode.valueOf(500), "내부 서버 오류입니다."),

    SERVEY_ALREADY_COMPLETE(HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(999), "이미 완료된 만족도조사입니다."),
    ;

    private final HttpStatus status;
    private final HttpStatusCode code;
    private final String message;
}
