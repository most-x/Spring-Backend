package kr.co.mostx.japi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssetDashboardDTO {
    private Long totalAsset;
    private Long assetNormal;
    private Long assetSale;
    private Long assetDispose;
    private Long assetStart;
    private Long assetNonStart;
    private int sumDepreciationAsset;
    private int bookValueAsset;
    private int totalPriceAsset;
}
