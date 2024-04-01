package com.mostx.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Long totalAsset;
    private Long normalAsset;
    private Long saleAsset;
    private Long disposeAsset;
    private Long startAsset;
    private Long nonStartAsset;
    private int accumlatedTotal;
    private int bookValueTotal;
    private int totalPrice;
}
