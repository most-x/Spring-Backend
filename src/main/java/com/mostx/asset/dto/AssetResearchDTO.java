package com.mostx.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetResearchDTO {
    @Schema(description = "WRMS 자산코드")
    private String wrmsAssetCode;

    @Schema(description = "WRMS 품목코드")
    private String wrmsItemCode;

    @Schema(description = "시리얼 번호")
    private String serialNumber;

    @Schema(description = "일상구독 상품번호")
    private String ilsangProductCode;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "금액유형")
    private String priceType;

    @Schema(description = "최소금액")
    private Integer minPrice;

    @Schema(description = "최대금액")
    private Integer maxPrice;

    @Schema(description = "창고번호")
    private String warehouseNumber;

    @Schema(description = "자산상태")
    private String assetStatus;

    @Schema(description = "자산용도")
    private String assetUsage;

    @Schema(description = "최초 개시일")
    private LocalDate initialStartDate;

    @Schema(description = "감가상각비")
    private Integer depreciationCost;

    @Schema(description = "감가상각 누계액")
    private Integer accumlatedDepreciation;

    @Schema(description = "장부가액")
    private Integer bookValue;

    @Schema(description = "감가상각일")
    private LocalDate depreciationDate;
}
