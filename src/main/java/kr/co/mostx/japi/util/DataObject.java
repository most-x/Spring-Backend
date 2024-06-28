package kr.co.mostx.japi.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 데이터 암 / 복호화
 * 복호화 시 복호화 데이터가 JSON 데이터 형식으로 보여지도록 Mapping 시켜 줄 Class
 */
@Getter
public class DataObject {
    private String userName;
    private String userPhone;
    private String consultantName;
    private String serveyNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime messageTime;
}
