package com.mostx.asset.service;
import com.mostx.asset.dto.*;
import com.mostx.asset.entity.Asset;
import com.mostx.asset.repository.AssetRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mostx.asset.entity.QAsset.asset;
import static com.mostx.asset.entity.QAssetDepreciation.assetDepreciation;

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

    @Transactional
    public AssetDTO register(AssetDTO assetDto) {
        Asset assetRegist = convertToEntity(assetDto);
        Asset savedAssetRegist = assetRepository.save(assetRegist);

        return convertToDto(savedAssetRegist);
    }

    public AssetDTO update(Long id, AssetRequestDTO assetRequestDto) {
        Asset assetUpdate = assetRepository.findBySno(id).orElseThrow(() -> {
            return new NullPointerException("해당 자산이 존재하지 않습니다.");
        });

        if(assetRequestDto.getSaleDate() != null) assetUpdate.setSaleDate(assetRequestDto.getSaleDate());
        if(assetRequestDto.getSaleAmount() != null) assetUpdate.setSaleAmount(assetRequestDto.getSaleAmount());
        if(assetRequestDto.getSalesRecognitionAmount() != null) assetUpdate.setSalesRecognitionAmount(assetRequestDto.getSalesRecognitionAmount());
        if(assetRequestDto.getDisposalDate() != null) assetUpdate.setDisposalDate(assetRequestDto.getDisposalDate());
        if(assetRequestDto.getDisposalAmount() != null) assetUpdate.setDisposalAmount(assetRequestDto.getDisposalAmount());

        Asset savedAssetUpdate = assetRepository.save(assetUpdate);

        return convertToDto(savedAssetUpdate);
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

    public Page<AssetDTO> findAssetDepreciationSearch(AssetDepreciationSearchDTO assetDepreciationSearchDto, Pageable pageable) {
        List<Asset> assetDepreciationSearchDtoList = jpaQueryFactory
                .selectFrom(asset)
                .where(assetSearch(assetDepreciationSearchDto.getWrmsAssetCode(), asset.wrmsAssetCode), assetSearch(assetDepreciationSearchDto.getWrmsItemCode(), asset.wrmsItemCode),
                        assetSearch(assetDepreciationSearchDto.getSerialNumber(), asset.serialNumber), assetSearch(assetDepreciationSearchDto.getIlsangProductCode(), asset.ilsangProductCode),
                        assetSearch(assetDepreciationSearchDto.getProductName(), asset.productName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(asset.count())
                .from(asset)
                .where(assetSearch(assetDepreciationSearchDto.getWrmsAssetCode(), asset.wrmsAssetCode), assetSearch(assetDepreciationSearchDto.getWrmsItemCode(), asset.wrmsItemCode),
                        assetSearch(assetDepreciationSearchDto.getSerialNumber(), asset.serialNumber), assetSearch(assetDepreciationSearchDto.getIlsangProductCode(), asset.ilsangProductCode),
                        assetSearch(assetDepreciationSearchDto.getProductName(), asset.productName))
                .fetchOne();

        List<AssetDTO> assetDtoList = assetDepreciationSearchDtoList.stream().map(this::convertToDto).toList();

        return new PageImpl<>(assetDtoList, pageable, count);
    }

    public Page<AssetDTO> findSearchAsset(AssetResearchDTO assetResearchDto, Pageable pageable) {
        List<Asset> searchAsset = jpaQueryFactory
                .selectFrom(asset)
                .where(assetSearch(assetResearchDto.getWrmsAssetCode(), asset.wrmsAssetCode), assetSearch(assetResearchDto.getWrmsItemCode(), asset.wrmsItemCode),
                        assetSearch(assetResearchDto.getIlsangProductCode(), asset.ilsangProductCode), assetSearch(assetResearchDto.getSerialNumber(), asset.serialNumber), assetSearch(assetResearchDto.getProductName(), asset.productName),
                        assetSearch(assetResearchDto.getAssetStatus(), asset.assetStatus), assetSearch(assetResearchDto.getAssetUsage(), asset.assetUsage), priceBetween(assetResearchDto.getPriceType(), assetResearchDto.getStartPrice(), assetResearchDto.getEndPrice()),
                        initialStartDateEq(assetResearchDto.getInitialStartDate()), assetSearch(assetResearchDto.getWarehouseNumber(), asset.warehouseNumber),
                        currentMonthBetween(assetResearchDto.getPriceType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<AssetDTO> resultDto = searchAsset.stream().map(this::convertToDto).toList();

        return new PageImpl<>(resultDto, pageable, searchAsset.size());
    }

    private BooleanExpression assetSearch(String searchAssetValue, StringPath asset) {
        return StringUtils.hasText(searchAssetValue) ? asset.contains(searchAssetValue) : null;
    }

    /**
     * @param priceType  - vatPlus(발주단가 VAT+), vatMinus(발주단가 VAT-), depreCost(감가상각비(당월)), accumDepre(감가상각 누계액), bookValue(장부가액)
     * @param startPrice
     * @param endPrice
     * @return
     */
    private BooleanExpression priceBetween(String priceType, Integer startPrice, Integer endPrice) {
        NumberPath<Integer> assetType;

        switch (priceType != null ? priceType : "NULL") {
            case "vatPlus" -> assetType = asset.totalPrice;
            case "vatMinus" -> assetType = asset.supplyPrice;
            case "depreCost" -> assetType = assetDepreciation.depreciationCost;
            case "accumDepre" -> assetType = assetDepreciation.accumlatedDepreciation;
            case "bookValue" -> assetType = assetDepreciation.bookValue;
            default -> assetType = null;
        }

        if(assetType != null) {
            if (startPrice != null && endPrice != null) {
                return assetType.between(startPrice, endPrice);
            } else if (startPrice != null) {
                return assetType.goe(startPrice);
            } else if (endPrice != null) {
                return assetType.loe(endPrice);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private BooleanExpression initialStartDateEq(LocalDate initialStartDate) {
        return initialStartDate != null ? asset.initialStartDate.eq(initialStartDate) : null;
    }

    /**
     * 감가상각 데이터는 당월을 기준으로 조회 따라서 startDay between today (Ex. 2023-12-01 ~ 2023-12-15)
     * today = 현재날짜
     * startDay = 당월 1일 세팅
     * @return
     */
    private BooleanExpression currentMonthBetween(String priceType) {
        if(priceType != null) {
            LocalDate today = LocalDate.now();
            LocalDate startDay = today.withDayOfMonth(1);

            return assetDepreciation.depreciationDate.between(startDay, today);
        } else {
            return null;
        }
    }
}