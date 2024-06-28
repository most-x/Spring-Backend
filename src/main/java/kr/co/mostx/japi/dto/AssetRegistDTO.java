package kr.co.mostx.japi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "자산정보 등록 DTO")
public class AssetRegistDTO {
    @Schema(description = "자산용도")
    private String assetUsage;

    @Schema(description = "WRMS 자산코드")
    @NotNull(message = "WRMS 자산코드가 입력되지 않았습니다.")
    private String wrmsAssetCode;

    @Schema(description = "WRMS 품목코드")
    @NotNull(message = "WRMS 품목코드가 입력되지 않았습니다.")
    private String wrmsItemCode;

    @Schema(description = "일상구독 상품번호")
    @NotNull(message = "일상구독 상품번호가 입력되지 않았습니다.")
    private String ilsangProductCode;

    @Schema(description = "시리얼번호")
    @NotNull(message = "시리얼번호가 입력되지 않았습니다.")
    private String serialNumber;

    @Schema(description = "상품명")
    @NotNull(message = "상품명이 입력되지 않았습니다.")
    private String productName;

    @Schema(description = "공급가")
    @NotNull(message = "공급가가 입력되지 않았습니다.")
    private Integer supplyPrice;

    @Schema(description = "내용연수")
    @NotNull(message = "내용연수가 입력되지 않았습니다.")
    private Integer usefulLife;

    @Schema(description = "등록자 정보-부서명")
    private String registDepartment;

    @Schema(description = "등록자 정보-성명")
    private String registName;

    @Schema(description = "최초 개시일")
    private LocalDate initialStartDate;
}

