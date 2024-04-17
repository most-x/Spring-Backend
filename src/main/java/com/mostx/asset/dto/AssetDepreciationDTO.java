package com.mostx.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDepreciationDTO {
    @Schema(description = "자산 ID")
    private Long sno;

    @Schema(description = "감가상각비")
    private int depreciationCost;

    @Schema(description = "감가상각 누계액")
    private int accumlatedDepreciation;

    @Schema(description = "장부가액")
    private int bookValue;

    @Schema(description = "감가상각일")
    private LocalDate depreciationDate;

    @Transient
    @Schema(description = "게시글 No")
    private Long no;
}
