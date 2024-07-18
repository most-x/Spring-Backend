package kr.co.mostx.japi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ServeyStaticsSearchDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String entryRoute;

    public ServeyStaticsSearchDto() {
        this.entryRoute = "ALL";
    }
}
