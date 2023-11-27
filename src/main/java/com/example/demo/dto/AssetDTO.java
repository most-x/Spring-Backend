package com.example.demo.dto;

import com.example.demo.vo.AssetVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssetDTO {
    private String wrms_asset_code;
    private String wrms_item_code;
    private String serial_number;
    private String ilsang_product_code;
    private String product_name;
    private int supply_price;
    private int vat;
    private int useful_life;
    private String warehouse_number;
    private String regist_department;
    private String regist_name;

    public AssetDTO(final AssetVo asset){
        this.wrms_asset_code = asset.getWrms_asset_code();
        this.wrms_item_code = asset.getWrms_item_code();
        this.serial_number = asset.getSerial_number();
        this.ilsang_product_code = asset.getIlsang_product_code();
        this.product_name = asset.getProduct_name();
        this.supply_price = asset.getSupply_price();
        this.vat = asset.getVat();
        this.useful_life = asset.getUseful_life();
        this.warehouse_number = asset.getWarehouse_number();
        this.regist_department = asset.getRegist_department();
        this.regist_name = asset.getRegist_name();
    }

    public static AssetVo toAsset(final AssetDTO dto){
        return AssetVo.builder().wrms_asset_code(dto.getWrms_asset_code())
                .wrms_item_code(dto.getWrms_item_code())
                .serial_number(dto.getSerial_number())
                .ilsang_product_code(dto.getIlsang_product_code())
                .product_name(dto.getProduct_name())
                .supply_price(dto.getSupply_price())
                .vat(dto.getVat())
                .useful_life(dto.getUseful_life())
                .warehouse_number(dto.getWarehouse_number())
                .regist_department(dto.getRegist_department())
                .regist_name(dto.getRegist_name())
                .build();
    }
}
