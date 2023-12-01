package com.example.demo.service;
import com.example.demo.entity.Asset;
import com.example.demo.repository.AssetRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.demo.entity.QAsset.asset;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Asset> findAll(Pageable pageable){
        return assetRepository.findAll(pageable);
    }

    public Asset findAssetCode(String wrmsAssetCode){
        return assetRepository.findByWrmsAssetCode(wrmsAssetCode).orElseThrow(()->{
            return new EntityNotFoundException("자산코드로 등록된 자산이 없습니다.");
        });
    }

    public List<Asset> findSearchAsset(String wrmsAssetCode, String wrmsItemCode,
                                       String ilsangProductCode, String serialNumber, String productName,
                                       String assetStatus, String assetUsage, Integer startPrice, Integer endPrice,
                                       Date initialStartDate, String warehouseNumber, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(asset)
                .where(wrmsAssetCodeEq(wrmsAssetCode), wrmsItemCodeEq(wrmsItemCode),
                        ilsangProductCodeEq(ilsangProductCode), serialNumberEq(serialNumber), productNameEq(productName),
                        assetStatusEq(assetStatus), assetUsageEq(assetUsage), priceBetween(startPrice, endPrice),
                        initialStartDateEq(initialStartDate), warehouseNumberEq(warehouseNumber))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression wrmsAssetCodeEq(String wrmsAssetCode) {
        return StringUtils.hasText(wrmsAssetCode) ? asset.wrmsAssetCode.contains(wrmsAssetCode) : null;
    }

    private BooleanExpression wrmsItemCodeEq(String wrmsItemCode) {
        return StringUtils.hasText(wrmsItemCode) ? asset.wrmsItemCode.contains(wrmsItemCode) : null;
    }

    private BooleanExpression ilsangProductCodeEq(String ilsangProdocuCode) {
        return StringUtils.hasText(ilsangProdocuCode) ? asset.ilsangProductCode.contains(ilsangProdocuCode) : null;
    }

    private BooleanExpression serialNumberEq(String serialNumber) {
        return StringUtils.hasText(serialNumber) ? asset.serialNumber.contains(serialNumber) : null;
    }

    private BooleanExpression productNameEq(String productName) {
        return StringUtils.hasText(productName) ? asset.productName.contains(productName) : null;
    }

    private BooleanExpression assetStatusEq(String assetStatus) {
        return StringUtils.hasText(assetStatus) ? asset.assetStatus.contains(assetStatus) : null;
    }

    private BooleanExpression assetUsageEq(String assetUsage) {
        return StringUtils.hasText(assetUsage) ? asset.assetUsage.contains(assetUsage) : null;
    }

    private BooleanExpression priceBetween(Integer startPrice, Integer endPrice) {
        if(startPrice != null && endPrice != null) {
            return asset.totalPrice.between(startPrice, endPrice);
        } else if(startPrice != null) {
            return asset.totalPrice.goe(startPrice);
        } else if(endPrice != null) {
            return asset.totalPrice.loe(endPrice);
        } else {
            return null;
        }
    }

    private BooleanExpression initialStartDateEq(Date initialStartDate) {
        return initialStartDate != null ? asset.initialStartDate.eq(initialStartDate) : null;
    }

    private BooleanExpression warehouseNumberEq(String warehouseNumber) {
        return warehouseNumber != null ? asset.warehouseNumber.eq(warehouseNumber) : null;
    }
}