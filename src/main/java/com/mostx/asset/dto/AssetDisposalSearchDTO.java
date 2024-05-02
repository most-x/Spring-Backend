package com.mostx.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDisposalSearchDTO {
    @Schema(description = "WRMS 자산코드")
    private String wrmsAssetCode;

    @Schema(description = "WRMS 품목코드")
    private String wrmsItemCode;

    @Schema(description = "일상구독 상품번호")
    private String ilsangProductCode;

    @Schema(description = "시리얼 번호")
    private String serialNumber;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "자산상태")
    private String assetStatus;

    @Schema(description = "금액유형")
    private String priceType;

    @Schema(description = "최소금액")
    private Integer minPrice;

    @Schema(description = "최대금액")
    private Integer maxPrice;

    @Schema(description = "검색일 유형")
    private String dateType;

    @Schema(description = "시작일")
    private LocalDate startDate;

    @Schema(description = "종료일")
    private LocalDate endDate;
}
