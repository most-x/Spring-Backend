package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDTO {
    private Long sno;
    @NotNull(message = "WRMS 자산코드가 입력되지 않았습니다.")
    private String wrmsAssetCode;
    @NotNull(message = "WRMS 품목코드가 입력되지 않았습니다.")
    private String wrmsItemCode;
    @NotNull(message = "시리얼번호가 입력되지 않았습니다.")
    private String serialNumber;
    @NotNull(message = "일상구독 상품번호가 입력되지 않았습니다.")
    private String ilsangProductCode;
    @NotNull(message = "상품명이 입력되지 않았습니다.")
    private String productName;
    @NotNull(message = "매입원가-공급가가 입력되지 않았습니다.")
    private Integer supplyPrice;
    @NotNull(message = "매입원가-부가세가 입력되지 않았습니다")
    private Integer vat;
    // supplyPirce + vat 합산금액이 자동으로 등러가도록
    private Integer totalPrice;
    private Integer usefulLife;
    @NotNull(message = "창고번호가 입력되지 않았습니다.")
    private String warehouseNumber;
    @NotNull(message = "자산상태를 체크해주세요.")
    private String assetStatus;
    @NotNull(message = "자산용도를 체크해주세요.")
    private String assetUsage;
    private String registDepartment;
    private String registName;
    private Date initialStartDate;
}
