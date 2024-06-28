package kr.co.mostx.japi.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @Column(nullable = true)
    private int vat;

    @Column(nullable = true, name="total_price")
    private int totalPrice;

    @Column(nullable = true, name="useful_life")
    private Integer usefulLife;

    @Column(nullable = true, name="warehouse_number")
    private String warehouseNumber;

    @Column(nullable = false, name="asset_status")
    private String assetStatus;

    @Column(nullable = false, name="asset_usage")
    private String assetUsage;

    @Column(nullable = false, name="regist_department")
    private String registDepartment;

    @Column(nullable = false, name="regist_name")
    private String registName;

    @Column(nullable = true, name="asset_regist_date")
    private LocalDate assetRegistDate;

    @Column(nullable = true, name="initial_start_date")
    private LocalDate initialStartDate;

    @Column(nullable = true, name="sales_recognition_amount")
    private Integer salesRecognitionAmount;

    @Column(nullable = true, name="sale_date")
    private LocalDate saleDate;

    @Column(nullable = true, name="sale_amount")
    private Integer saleAmount;

    @Column(nullable = true, name="disposal_date")
    private LocalDate disposalDate;

    @Column(nullable = true, name="disposal_amount")
    private Integer disposalAmount;

    @Column(nullable = true, name="depreciation_current")
    private Integer depreciationCurrent;

    @Column(nullable = true, name="depreciation_totalprice")
    private Integer depreciationTotalprice;

    @Column(nullable = true, name="book_value")
    private Integer bookValue;

    @OneToMany(mappedBy = "assetSno", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("depreciationDate desc")
    private List<AssetDepreciation> assetDepreciation;
}

