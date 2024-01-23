package com.mostx.asset.dto;

import com.mostx.asset.entity.Asset;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDepreciationDTO {
    @Schema(description = "자산 ID")
    private Long assetCodeSno;

    @Schema(description = "감가상각비")
    private int depreciationCost;

    @Schema(description = "감가상각 누계액")
    private int accumlatedDepreciation;

    @Schema(description = "장부가액")
    private int bookValue;

    @Schema(description = "감가상각일")
    private LocalDate depreciationDate;

    // asset Sno 만 추출하여 API 로 표기해주도록
    public Long assetEntity(Asset asset){
        AssetDepreciationDTO assetDepreciationDTO = new AssetDepreciationDTO();
        assetDepreciationDTO.setAssetCodeSno(asset.getSno());

        return assetDepreciationDTO.getAssetCodeSno();
    }
}
