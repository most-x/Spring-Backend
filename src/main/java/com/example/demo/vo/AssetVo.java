package com.example.demo.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="mostx_AssetRegist")
public class AssetVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sno;
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
    private Date initial_start_date;

    @Builder
    public AssetVo(String wrms_asset_code, String wrms_item_code, String serial_number, String ilsang_product_code,
                   String product_name, int supply_price, int vat, int useful_life, String warehouse_number,
                   String regist_department, String regist_name) {
        this.wrms_asset_code = wrms_asset_code;
        this.wrms_item_code = wrms_item_code;
        this.serial_number = serial_number;
        this.ilsang_product_code = ilsang_product_code;
        this.product_name = product_name;
        this.supply_price = supply_price;
        this.vat = vat;
        this.useful_life = useful_life;
        this.warehouse_number = warehouse_number;
        this.regist_department = regist_department;
        this.regist_name = regist_name;
    }
}

