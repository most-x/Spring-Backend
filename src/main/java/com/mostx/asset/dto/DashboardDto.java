package com.mostx.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private Long totalAsset;
    private Long normalAsset;
    private Long saleAsset;
    private Long disposeAsset;
    private Long startAsset;
    private Long nonStartAsset;
    private int accumlatedTotal;
    private int bookValueTotal;
    private int totalPrice;

    public DashboardDto(Long totalAsset, Long normalAsset, Long saleAsset, Long disposeAsset, Long startAsset, Long nonStartAsset,
                        int bookValueTotal, int totalPrice) {
        this.totalAsset = totalAsset;
        this.normalAsset = normalAsset;
        this.saleAsset = saleAsset;
        this.disposeAsset = disposeAsset;
        this.startAsset = startAsset;
        this.nonStartAsset = nonStartAsset;
        this.bookValueTotal = bookValueTotal;
        this.totalPrice = totalPrice;
    }
}
