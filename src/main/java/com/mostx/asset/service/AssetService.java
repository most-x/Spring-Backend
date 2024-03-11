package com.mostx.asset.service;

import com.mostx.asset.dto.*;
import com.mostx.asset.entity.Asset;
import com.mostx.asset.repository.AssetRepository;
import com.mostx.asset.response.ResponsePageInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private final EntityManager em;
    private final ModelMapper modelMapper = new ModelMapper();

    // DTO를 Entity로 변환 후 저장 Asset 등록 Controller에서만 사용
    private void convertToEntity(AssetDTO assetDto) {
        Asset asset = modelMapper.map(assetDto, Asset.class);

        asset.setTotalPrice(assetDto.getSupplyPrice() + assetDto.getVat());

        assetRepository.save(asset);
    }

    private List<AssetDTO> convertToDto(Page<Asset> asset) {
        return asset.stream()
                .map(asset1 -> modelMapper.map(asset1, AssetDTO.class))
                .collect(Collectors.toList());
    }

    private AssetDTO convertToDto(Asset asset) {
        return modelMapper.map(asset, AssetDTO.class);
    }

    // 자산등록
    // DTO로 넘겨받은 정보를 Entity로 변환 후 저장한다.
    // return 데이터는 저장된 Entity를 DTO로 변환하여 반환한다.
    @Transactional
    public String register(AssetDTO assetDto) {
        convertToEntity(assetDto);

        return "성공적으로 등록되었습니다.";
    }

    // 자산 매각, 폐기 업데이트
    // 넘어온 sno 기준으로 자산에 대하여 매각 or 폐기 update
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

    // 내용연수, 자산개시일이 존재하는 자산에 대하여 감가상각 정보 업데이트
    @Transactional
    public void update(Long id, int depreciationCost, int depreciationTotalprice, int bookValue) {
        Asset asset1 = em.find(Asset.class, id);

        asset1.setDepreciationCurrent(depreciationCost);
        asset1.setDepreciationTotalprice(depreciationTotalprice);
        asset1.setBookValue(bookValue);

        em.persist(asset1);

        em.flush();
    }

    // 전체자산 조회
    public ResponsePageInfo findAll(int page, int size) {
        Page<Asset> assetPage = assetRepository.findAll(PageRequest.of(page - 1, size));
        List<AssetDTO> assetDtos = convertToDto(assetPage);
        Long no;

        // 자산조회 시 자산별로 페이지 NO 세팅
        for (AssetDTO asset1 : assetDtos) {
            no = assetPage.getTotalElements() - ((long) (page - 1) * size) - assetDtos.indexOf(asset1);

            asset1.setNo(no);
        }

        return new ResponsePageInfo<>(assetDtos, assetPage.getTotalElements(), (long) assetPage.getTotalPages());
    }

    // 자산 sno 기준으로 조회
    public AssetDTO findAsset(Long sno) {
        Asset idAsset = assetRepository.findBySno(sno).orElseThrow(() -> {
            return new IllegalArgumentException("Asset ID를 찾을 수 없습니다.");
        });

        return convertToDto(idAsset);
    }

    // 자산코드 기준으로 조회
    public AssetDTO findAssetCode(String wrmsAssetCode){
        Asset codeAsset = assetRepository.findByWrmsAssetCode(wrmsAssetCode).orElseThrow(()->{
            return new EntityNotFoundException("자산코드로 등록된 자산이 없습니다.");
        });

        return convertToDto(codeAsset);
    }

    // 자산 감가상각 현황 조회 (건별 자산 조회)
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

    // 전체 자산에 대하여 검색조건을 기반으로 조회
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