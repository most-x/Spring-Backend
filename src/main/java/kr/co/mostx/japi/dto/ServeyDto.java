package kr.co.mostx.japi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kr.co.mostx.japi.entity.Servey;
import kr.co.mostx.japi.excel.ExcelColumnName;
import kr.co.mostx.japi.excel.ExcelFileName;
import kr.co.mostx.japi.type.Platform;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties()
@ExcelFileName(fileName = "만족도조사 리스트")
public class ServeyDto {
    private Long id;
    @ExcelColumnName(headerName = "만족도조사 1번 항목")
    private int serveyOne;
    @ExcelColumnName(headerName = "만족도조사 2번 항목")
    private int serveyTwo;
    @ExcelColumnName(headerName = "만족도조사 3번 항목")
    private int serveyThree;
    @ExcelColumnName(headerName = "만족도조사 4번 항목")
    private int serveyFour;
    @ExcelColumnName(headerName = "만족도조사 5번 항목")
    private int serveyFive;
    @ExcelColumnName(headerName = "평균점수")
    private double avgScore;
    @ExcelColumnName(headerName = "총점수")
    private int totalScore;
    @ExcelColumnName(headerName = "고객명")
    private String userName;
    @ExcelColumnName(headerName = "고객전화번호")
    private String userPhone;
    @ExcelColumnName(headerName = "상담자명")
    private String consultantName;
    @ExcelColumnName(headerName = "인입경로")
    private Platform platform;
    @ExcelColumnName(headerName = "상담번호")
    private String serveyNumber;
    @ExcelColumnName(headerName = "등록일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Builder
    public ServeyDto(Long id, int serveyOne, int serveyTwo, int serveyThree, int serveyFour, int serveyFive, double avgScore, int totalScore, String userName,
                     String userPhone, String consultantName, Platform platform, String serveyNumber, LocalDateTime createdDate) {
        this.id = id;
        this.serveyOne = serveyOne;
        this.serveyTwo = serveyTwo;
        this.serveyThree = serveyThree;
        this.serveyFour = serveyFour;
        this.serveyFive = serveyFive;
        this.avgScore = avgScore;
        this.totalScore = totalScore;
        this.userName = userName;
        this.userPhone = userPhone;
        this.consultantName = consultantName;
        this.platform = platform;
        this.serveyNumber = serveyNumber;
        this.createdDate = createdDate;
    }

    public static ServeyDto convertToServeyCreateResponse(Servey servey) {
        return ServeyDto.builder()
                .id(servey.getId())
                .createdDate(servey.getCreatedDate())
                .build();
    }

    public static ServeyDto convertToServeyFindAllResponse(Servey servey) {
        return ServeyDto.builder()
                .id(servey.getId())
                .serveyOne(servey.getServeyOne())
                .serveyTwo(servey.getServeyTwo())
                .serveyThree(servey.getServeyThree())
                .serveyFour(servey.getServeyFour())
                .serveyFive(servey.getServeyFive())
                .avgScore(servey.getAvgScore())
                .totalScore(servey.getTotalScore())
                .userName(servey.getUserName())
                .userPhone(servey.getUserPhone())
                .consultantName(servey.getConsultantName())
                .platform(servey.getPlatform())
                .serveyNumber(servey.getServeyNumber())
                .createdDate(servey.getCreatedDate())
                .build();
    }
}
