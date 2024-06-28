package kr.co.mostx.japi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDepreciationSearchDTO {
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
}
