package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="mostx_AssetRegist")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sno")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sno;

    @OneToMany(mappedBy = "assetSno")
    @OrderBy("depreciationDate desc")
    private List<AssetDepreciation> assetDepreciation;

    @Column(nullable = false, unique = true, name="wrms_asset_code")
    private String wrmsAssetCode;

    @Column(nullable = false, name="wrms_item_code")
    private String wrmsItemCode;

    @Column(nullable = false, name="serial_number")
    private String serialNumber;

    @Column(nullable = false, name="ilsang_product_code")
    private String ilsangProductCode;

    @Column(nullable = false, name="product_name")
    private String productName;

    @Column(nullable = false, name="supply_price")
    private int supplyPrice;

    @Column(nullable = false)
    private int vat;

    @Column(nullable = false, name="total_price")
    private int totalPrice;

    @Column(nullable = true, name="useful_life")
    private Integer usefulLife;

    @Column(nullable = false, name="warehouse_number")
    private String warehouseNumber;

    @Column(nullable = false, name="asset_status")
    private String assetStatus;

    @Column(nullable = false, name="asset_usage")
    private String assetUsage;

    @Column(nullable = false, name="regist_department")
    private String registDepartment;

    @Column(nullable = false, name="regist_name")
    private String registName;

    @Column(nullable = true, name="initial_start_date")
    private Date initialStartDate;
}

