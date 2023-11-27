package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "mostx_AssetDepreciation")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sno")
class AssetDepreciation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_code_sno")
    private Asset assetSno;

    @Column(nullable = false, name = "depreciation_cost")
    private int depreciationCost;

    @Column(nullable = false, name = "accumlated_depreciation")
    private int accumlatedDepreciation;

    @Column(nullable = false, name = "book_value")
    private int bookValue;

    @Column(nullable = false, name = "depreciation_date")
    private Date depreciationDate;
}