package kr.co.mostx.japi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "자산정보 응답 DTO")
public class AssetDTO {
    @Schema(description = "자산 ID")
    private Long sno;

    @Schema(description = "자산상태")
    private String assetStatus;

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

    @Schema(description = "감가상각비(당월)")
    private Integer depreciationCurrent;

    @Schema(description = "감가상각 누계액")
    private Integer depreciationTotalprice;

    @Schema(description = "장부가액")
    private Integer bookValue;

    @Schema(description = "최초 개시일")
    private LocalDate initialStartDate;

    @Schema(description = "자산 등록일자")
    private LocalDate assetRegistDate;

    @Transient
    private Long no;
}