package kr.co.mostx.japi.dto;

import kr.co.mostx.japi.type.ScoreType;
import kr.co.mostx.japi.type.SearchType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ServeySearchDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private SearchType searchType;
    private String searchWord;
    private String platform;
    private ScoreType scoreType;
    private int minScore;
    private int maxScore;
}
