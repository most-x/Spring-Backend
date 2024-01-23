package com.mostx.asset.service;
import com.mostx.asset.dto.AssetDTO;
import com.mostx.asset.dto.AssetDepreciationDTO;
import com.mostx.asset.entity.Asset;
import com.mostx.asset.repository.AssetRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mostx.asset.entity.QAsset.asset;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final JPAQueryFactory jpaQueryFactory;

    private Asset convertToEntity(AssetDTO assetDto) {
        Asset asset = new Asset();
        asset.setWrmsAssetCode(assetDto.getWrmsAssetCode());
        asset.setWrmsItemCode(assetDto.getWrmsItemCode());
        asset.setSerialNumber(assetDto.getSerialNumber());
        asset.setIlsangProductCode(assetDto.getIlsangProductCode());
        asset.setProductName(assetDto.getProductName());
        if (assetDto.getSupplyPrice() != 0) {
            asset.setSupplyPrice(assetDto.getSupplyPrice());
        }
        if (assetDto.getVat() != 0) {
            asset.setVat(assetDto.getVat());
        }
        asset.setTotalPrice(assetDto.getSupplyPrice() + assetDto.getVat());
        if (assetDto.getUsefulLife() != 0) {
            asset.setUsefulLife(assetDto.getUsefulLife());
        }
        asset.setWarehouseNumber(assetDto.getWarehouseNumber());
        asset.setAssetStatus(assetDto.getAssetStatus());
        asset.setAssetUsage(assetDto.getAssetUsage());
        asset.setRegistDepartment(assetDto.getRegistDepartment());
        asset.setRegistName(assetDto.getRegistName());
        asset.setInitialStartDate(assetDto.getInitialStartDate());

        return asset;
    }

    private AssetDTO convertToDto(Asset asset) {
        AssetDTO assetDto = new AssetDTO();

        assetDto.setSno(asset.getSno());
        assetDto.setWrmsAssetCode(asset.getWrmsAssetCode());
        assetDto.setWrmsItemCode(asset.getWrmsItemCode());
        assetDto.setSerialNumber(asset.getSerialNumber());
        assetDto.setIlsangProductCode(asset.getIlsangProductCode());
        assetDto.setProductName(asset.getProductName());
        assetDto.setSupplyPrice(asset.getSupplyPrice());
        assetDto.setVat(asset.getVat());
        assetDto.setTotalPrice(asset.getTotalPrice());
        assetDto.setUsefulLife(asset.getUsefulLife());
        assetDto.setWarehouseNumber(asset.getWarehouseNumber());
        assetDto.setAssetStatus(asset.getAssetStatus());
        assetDto.setAssetUsage(asset.getAssetUsage());
        assetDto.setRegistDepartment(asset.getRegistDepartment());
        assetDto.setRegistName(asset.getRegistName());
        assetDto.setInitialStartDate(asset.getInitialStartDate());
        assetDto.setSalesRecognitionAmount(asset.getSalesRecognitionAmount());
        assetDto.setSaleDate(asset.getSaleDate());
        assetDto.setSaleAmount(asset.getSaleAmount());
        assetDto.setDisposalDate(asset.getDisposalDate());
        assetDto.setDisposalAmount(asset.getDisposalAmount());
        assetDto.setDepreciationCurrent(asset.getDepreciationCurrent());
        assetDto.setDepreciationTotalprice(asset.getDepreciationTotalprice());
        assetDto.setBookValue(asset.getBookValue());

        return assetDto;
    }

    private AssetDTO convertToDto(Asset asset, int page) {
        AssetDTO assetDto = new AssetDTO();

        assetDto.setSno(asset.getSno());
        assetDto.setWrmsAssetCode(asset.getWrmsAssetCode());
        assetDto.setWrmsItemCode(asset.getWrmsItemCode());
        assetDto.setSerialNumber(asset.getSerialNumber());
        assetDto.setIlsangProductCode(asset.getIlsangProductCode());
        assetDto.setProductName(asset.getProductName());
        assetDto.setSupplyPrice(asset.getSupplyPrice());
        assetDto.setVat(asset.getVat());
        assetDto.setTotalPrice(asset.getTotalPrice());
        assetDto.setUsefulLife(asset.getUsefulLife());
        assetDto.setWarehouseNumber(asset.getWarehouseNumber());
        assetDto.setAssetStatus(asset.getAssetStatus());
        assetDto.setAssetUsage(asset.getAssetUsage());
        assetDto.setRegistDepartment(asset.getRegistDepartment());
        assetDto.setRegistName(asset.getRegistName());
        assetDto.setInitialStartDate(asset.getInitialStartDate());

        List<AssetDepreciationDTO> assetDepreciationDtos = asset.getAssetDepreciation().stream()
                .skip((long)(page-1) * 5)
                .limit(5)
                .map(assetDepreciation -> {
                    AssetDepreciationDTO assetDepreciationDto = new AssetDepreciationDTO();
                    assetDepreciationDto.setAssetCodeSno(assetDepreciationDto.assetEntity(asset));
                    assetDepreciationDto.setAccumlatedDepreciation(assetDepreciation.getAccumlatedDepreciation());
                    assetDepreciationDto.setDepreciationCost(assetDepreciation.getDepreciationCost());
                    assetDepreciationDto.setDepreciationDate(assetDepreciation.getDepreciationDate());
                    assetDepreciationDto.setBookValue(assetDepreciation.getBookValue());
                    return assetDepreciationDto;
                })
                .collect(Collectors.toList());

        assetDto.setAssetDepreciationDTOs(assetDepreciationDtos);

        return assetDto;
    }

    public Page<AssetDTO> findAll(Pageable pageable) {
        Page<Asset> assetPage = assetRepository.findAll(pageable);

        return assetPage.map(this::convertToDto);
    }

    public AssetDTO findAsset(Long sno) {
        Asset idAsset = assetRepository.findBySno(sno).orElseThrow(() -> {
            return new IllegalArgumentException("Asset ID를 찾을 수 없습니다.");
        });

        return convertToDto(idAsset);
    }

    public Asset findAssetCode(String wrmsAssetCode){
        return assetRepository.findByWrmsAssetCode(wrmsAssetCode).orElseThrow(()->{
            return new EntityNotFoundException("자산코드로 등록된 자산이 없습니다.");
        });
    }

    public List<Asset> findSearchAsset(String wrmsAssetCode, String wrmsItemCode,
                                       String ilsangProductCode, String serialNumber, String productName,
                                       String assetStatus, String assetUsage, Integer startPrice, Integer endPrice,
                                       LocalDate initialStartDate, String warehouseNumber, Pageable pageable) {
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

    private BooleanExpression initialStartDateEq(LocalDate initialStartDate) {
        return initialStartDate != null ? asset.initialStartDate.eq(initialStartDate) : null;
    }

    private BooleanExpression warehouseNumberEq(String warehouseNumber) {
        return warehouseNumber != null ? asset.warehouseNumber.eq(warehouseNumber) : null;
    }
}