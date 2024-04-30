package com.mostx.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "자산처분 응답 DTO")
public class AssetDisposalDTO {
    @Schema(description = "자산 ID")
    private Long sno;

    @Schema(description = "자산상태")
    private String assetStatus;

    @Schema(description = "WRMS 자산코드")
    private String wrmsAssetCode;

    @Schema(description = "WRMS 품목코드")
    private String wrmsItemCode;

    @Schema(description = "일상구독 상품번호")
    private String ilsangProductCode;

    @Schema(description = "시리얼번호")
    private String serialNumber;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "자산 등록일자")
    private LocalDate assetRegistDate;

    @Schema(description = "최초 개시일")
    private LocalDate initialStartDate;

    @Schema(description = "장부가액")
    private Integer bookValue;

    @Schema(description = "감가상각 누계액")
    private Integer depreciationTotalprice;

    @Schema(description = "매각일자")
    private LocalDate saleDate;

    @Schema(description = "매각금액")
    private Integer saleAmount;

    @Schema(description = "폐기일자")
    private LocalDate disposalDate;

    @Schema(description = "폐기금액")
    private Integer disposalAmount;

    @Schema(description = "매각손익액")
    @Transient
    private int saleMargin;

    @Schema(description = "매각이익률")
    @Transient
    private int saleMarginRate;
}
