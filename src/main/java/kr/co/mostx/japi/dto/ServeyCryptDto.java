package kr.co.mostx.japi.dto;

import kr.co.mostx.japi.util.DataObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 데이터 암 / 복호화
 * Front에서 만족도조사 데이터 및 암호화 데이터가
 * 들어올 경우 받아줄 Dto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServeyCryptDto {
    private DataObject decryptData;
    private String registStatus;
    private String onedayCheck;
}
