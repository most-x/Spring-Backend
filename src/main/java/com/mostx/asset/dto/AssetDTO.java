package com.mostx.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "자산정보 응답 DTO")
public class AssetDTO {
    @Schema(description = "자산 ID")
    private Long sno;

    @Schema(description = "WRMS 자산코드")
    @NotNull(message = "WRMS 자산코드가 입력되지 않았습니다.")
    private String wrmsAssetCode;

    @Schema(description = "WRMS 품목코드")
    @NotNull(message = "WRMS 품목코드가 입력되지 않았습니다.")
    private String wrmsItemCode;

    @Schema(description = "시리얼번호")
    @NotNull(message = "시리얼번호가 입력되지 않았습니다.")
    private String serialNumber;

    @Schema(description = "일상구독 상품번호")
    @NotNull(message = "일상구독 상품번호가 입력되지 않았습니다.")
    private String ilsangProductCode;

    @Schema(description = "상품명")
    @NotNull(message = "상품명이 입력되지 않았습니다.")
    private String productName;

    @Schema(description = "매입원가-공급가")
    @NotNull(message = "매입원가-공급가가 입력되지 않았습니다.")
    private Integer supplyPrice;

    @Schema(description = "매입원가-부가세")
    @NotNull(message = "매입원가-부가세가 입력되지 않았습니다")
    private Integer vat;

    @Schema(description = "공급가+부가세")
    // supplyPirce + vat 합산금액이 자동으로 세팅
    private Integer totalPrice;

    @Schema(description = "내용연수")
    private Integer usefulLife;

    @Schema(description = "창고번호")
    @NotNull(message = "창고번호가 입력되지 않았습니다.")
    private String warehouseNumber;

    @Schema(description = "자산상태")
    @NotNull(message = "자산상태를 체크해주세요.")
    private String assetStatus;

    @Schema(description = "자산용도")
    @NotNull(message = "자산용도를 체크해주세요.")
    private String assetUsage;

    @Schema(description = "등록자 정보-부서명")
    private String registDepartment;

    @Schema(description = "등록자 정보-성명")
    private String registName;

    @Schema(description = "최초 개시일")
    private LocalDate initialStartDate;

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

    @Schema(description = "감가상각비(당월)")
    private Integer depreciationCurrent;

    @Schema(description = "감가상각 누계액")
    private Integer depreciationTotalprice;

    @Schema(description = "장부가액")
    private Integer bookValue;

    private List<AssetDepreciationDTO> assetDepreciationDTOs;

    public Integer getUsefulLife() {
        return Objects.requireNonNullElse(usefulLife, 0);
    }
}