package com.mostx.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetRequestDTO {
    @Schema(description = "매출인식액")
    private Integer salesRecognitionAmount;

    @Schema(description = "매각(인수)일자")
    private LocalDate saleDate;

    @Schema(description = "매각(인수)금액")
    private Integer saleAmount;

    @Schema(description = "폐기일자")
    private LocalDate disposalDate;

    @Schema(description = "폐기금액")
    private Integer disposalAmount;
}
