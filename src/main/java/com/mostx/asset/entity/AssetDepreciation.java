package com.mostx.asset.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "mostx_AssetDepreciation")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sno")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter @Setter
public class AssetDepreciation {
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
    private LocalDate depreciationDate;
}