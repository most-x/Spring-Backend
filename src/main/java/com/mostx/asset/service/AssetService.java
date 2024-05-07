package com.mostx.asset.service;

import com.mostx.asset.dto.*;
import com.mostx.asset.entity.Asset;
import com.mostx.asset.repository.AssetRepository;
import com.mostx.asset.response.Response;
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
    private void convertToEntity(AssetRegistDTO assetRegistDTO) {
        Asset asset = modelMapper.map(assetRegistDTO, Asset.class);

        asset.setAssetStatus("정상");
        asset.setAssetRegistDate(LocalDate.now());
        asset.setBookValue(asset.getSupplyPrice());

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

    private AssetDetailDTO convertToDetailDto(Asset asset) {
        return modelMapper.map(asset, AssetDetailDTO.class);
    }

    private List<AssetDisposalDTO> convertToDisposalDto(Page<Asset> asset) {
        return asset.stream()
                .map(asset1 -> modelMapper.map(asset1, AssetDisposalDTO.class))
                .collect(Collectors.toList());
    }

    // 자산등록
    // DTO로 넘겨받은 정보를 Entity로 변환 후 저장한다.
    // return 데이터는 저장된 Entity를 DTO로 변환하여 반환한다.
    @Transactional
    public String register(AssetRegistDTO assetRegistDTO) {
        convertToEntity(assetRegistDTO);

        return "성공적으로 등록되었습니다.";
    }

    // 자산 매각, 폐기 업데이트
    // 넘어온 sno 기준으로 자산에 대하여 매각 or 폐기 update
    @Transactional
    public String update(List<AssetRequestDTO> assetRequestDto) {
        for (AssetRequestDTO assetRequestDTO1 : assetRequestDto) {
            Asset assetUpdate = assetRepository.findBySno(assetRequestDTO1.getSno()).orElseThrow(() -> {
                return new NullPointerException("해당 자산이 존재하지 않습니다.");
            });

            if(assetRequestDTO1.getInitialStartDate() != null) {
                assetUpdate.setInitialStartDate(assetRequestDTO1.getInitialStartDate());
            }

            if(assetRequestDTO1.getSaleDate() != null) {
                assetUpdate.setSaleDate(assetRequestDTO1.getSaleDate());
                assetUpdate.setSaleAmount(assetRequestDTO1.getSaleAmount());
                assetUpdate.setAssetStatus("매각");
            } else if(assetRequestDTO1.getDisposalDate() != null) {
                assetUpdate.setDisposalDate(assetRequestDTO1.getDisposalDate());
                assetUpdate.setDisposalAmount(assetRequestDTO1.getDisposalAmount());

                assetUpdate.setAssetStatus("폐기");
            } else {
                return "매각 날짜에 대한 정보가 존재하지 않습니다.";
            }

        }

        return "업데이트가 완료되었습니다.";
    }

    // 내용연수, 자산개시일이 존재하는 자산에 대하여 감가상각 정보 업데이트
    @Transactional
    public void updateDepreciation(Long id, int depreciationCost, int depreciationTotalprice, int bookValue) {
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

        for (AssetDTO assetDTO : assetDtos) {
            no = assetPage.getTotalElements() - ((long) (page - 1) * size) - assetDtos.indexOf(assetDTO);

            assetDTO.setNo(no);
        }

        return new ResponsePageInfo<>(assetDtos, assetPage.getTotalElements(), (long) assetPage.getTotalPages());
    }

    // 자산처분 조회
    public ResponsePageInfo findDisposalAll(int page, int size) {
        Page<Asset> assetPage = assetRepository.findAll(PageRequest.of(page - 1, size));
        List<AssetDisposalDTO> assetDtos = noMargin(page, size, assetPage);

        return new ResponsePageInfo<>(assetDtos, assetPage.getTotalElements(), (long) assetPage.getTotalPages());
    }

    // 자산 sno 기준으로 조회
    public AssetDetailDTO findAsset(Long sno) {
        Asset idAsset = assetRepository.findBySno(sno).orElseThrow(() -> {
            return new IllegalArgumentException("Asset ID를 찾을 수 없습니다.");
        });

        return convertToDetailDto(idAsset);
    }

    // 자산코드 기준으로 조회
    public AssetDetailDTO findAssetCode(String wrmsAssetCode){
        Asset codeAsset = assetRepository.findByWrmsAssetCode(wrmsAssetCode).orElseThrow(()->{
            return new EntityNotFoundException("자산코드로 등록된 자산이 없습니다.");
        });

        return convertToDetailDto(codeAsset);
    }

    // 자산 감가상각 현황 조회 (건별 자산 조회)
    public Response<List<AssetDetailDTO>> findAssetDepreciationSearch(AssetDepreciationSearchDTO assetDepreciationSearchDto) {
        List<Asset> assetDepreciationSearchDtoList = jpaQueryFactory
                .selectFrom(asset)
                .where(assetSearch(assetDepreciationSearchDto.getWrmsAssetCode(), asset.wrmsAssetCode), assetSearch(assetDepreciationSearchDto.getWrmsItemCode(), asset.wrmsItemCode),
                        assetSearch(assetDepreciationSearchDto.getSerialNumber(), asset.serialNumber), assetSearch(assetDepreciationSearchDto.getIlsangProductCode(), asset.ilsangProductCode),
                        assetSearch(assetDepreciationSearchDto.getProductName(), asset.productName))
                .fetch();

        List<AssetDetailDTO> assetDtoList = assetDepreciationSearchDtoList.stream().map(this::convertToDetailDto).toList();

        return new Response<>(assetDtoList);
    }

    // 전체 자산에 대하여 검색조건을 기반으로 조회
    public ResponsePageInfo findSearchAsset(AssetSearchDTO assetSearchDto, int page, int size) {
        List<Asset> searchAsset = jpaQueryFactory
                .selectFrom(asset)
                .where(assetSearch(assetSearchDto.getWrmsAssetCode(), asset.wrmsAssetCode), assetSearch(assetSearchDto.getWrmsItemCode(), asset.wrmsItemCode),
                        assetSearch(assetSearchDto.getIlsangProductCode(), asset.ilsangProductCode), assetSearch(assetSearchDto.getSerialNumber(), asset.serialNumber), assetSearch(assetSearchDto.getProductName(), asset.productName),
                        assetSearch(assetSearchDto.getAssetStatus(), asset.assetStatus), assetSearch(assetSearchDto.getAssetUsage(), asset.assetUsage), priceBetween(assetSearchDto.getPriceType(), assetSearchDto.getMinPrice(), assetSearchDto.getMaxPrice()),
                        currentMonthBetween(assetSearchDto.getDateType(), assetSearchDto.getStartDate(), assetSearchDto.getEndDate()))
                .fetch();

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), searchAsset.size());
        Long no;

        // List로 검색된 데이터 Page로 변환
        Page<Asset> assetPage = new PageImpl<>(searchAsset.subList(start, end), pageRequest, searchAsset.size());
        List<AssetDTO> resultDto = convertToDto(assetPage);


        for (AssetDTO asset1 : resultDto) {
            no = assetPage.getTotalElements() - ((long) (page - 1) * size) - resultDto.indexOf(asset1);

            asset1.setNo(no);
        }

        return new ResponsePageInfo<>(resultDto, assetPage.getTotalElements(), (long) assetPage.getTotalPages());
    }

    // 자산 처분에 대하여 검색조건을 기반으로 조회
    public ResponsePageInfo findDisposalSearchAsset(AssetDisposalSearchDTO assetDisposalSearchDto, int page, int size) {
        List<Asset> searchAsset = jpaQueryFactory
                .selectFrom(asset)
                .where(assetSearch(assetDisposalSearchDto.getWrmsAssetCode(), asset.wrmsAssetCode), assetSearch(assetDisposalSearchDto.getWrmsItemCode(), asset.wrmsItemCode),
                        assetSearch(assetDisposalSearchDto.getIlsangProductCode(), asset.ilsangProductCode), assetSearch(assetDisposalSearchDto.getSerialNumber(), asset.serialNumber), assetSearch(assetDisposalSearchDto.getProductName(), asset.productName),
                        assetSearch(assetDisposalSearchDto.getAssetStatus(), asset.assetStatus), priceBetween(assetDisposalSearchDto.getPriceType(), assetDisposalSearchDto.getMinPrice(), assetDisposalSearchDto.getMaxPrice()),
                        currentMonthBetween(assetDisposalSearchDto.getDateType(), assetDisposalSearchDto.getStartDate(), assetDisposalSearchDto.getEndDate()))
                .fetch();

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), searchAsset.size());

        // List로 검색된 데이터 Page로 변환
        Page<Asset> assetPage = new PageImpl<>(searchAsset.subList(start, end), pageRequest, searchAsset.size());
        List<AssetDisposalDTO> resultDto = noMargin(page, size, assetPage);

        return new ResponsePageInfo<>(resultDto, assetPage.getTotalElements(), (long) assetPage.getTotalPages());
    }

    private BooleanExpression assetSearch(String searchAssetValue, StringPath asset) {
        return StringUtils.hasText(searchAssetValue) ? asset.contains(searchAssetValue) : null;
    }

    /**
     * @param priceType  - vatPlus(발주단가 VAT+), vatMinus(발주단가 VAT-), depreCost(감가상각비(당월)), accumDepre(감가상각 누계액), bookValue(장부가액)
     * @param minPrice
     * @param maxPrice
     * @return
     */
    private BooleanExpression priceBetween(String priceType, Integer minPrice, Integer maxPrice) {
        NumberPath<Integer> assetType;

        switch (priceType != null ? priceType : "NULL") {
            case "supplyPrice" -> assetType = asset.supplyPrice;
            case "depreciationCost" -> assetType = asset.depreciationCurrent;
            case "depreciationTotalprice" -> assetType = asset.depreciationTotalprice;
            case "bookValue" -> assetType = asset.bookValue;
            default -> assetType = null;
        }

        if(assetType != null) {
            if (minPrice != null && maxPrice != null) {
                return assetType.between(minPrice, maxPrice);
            } else if (minPrice != null) {
                return assetType.goe(minPrice);
            } else if (maxPrice != null) {
                return assetType.loe(maxPrice);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private BooleanExpression currentMonthBetween(String dateType, LocalDate startDate, LocalDate endDate) {
        if(dateType != null) {
            if(dateType.equals("assetRegistDate")){
                return asset.assetRegistDate.between(startDate, endDate);
            } else if(dateType.equals("initailStartDate")) {
                return asset.initialStartDate.between(startDate, endDate);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // 사용API 리스트 - 자산 전체조회, 자산 검색조건 조회
    // 자산리스트 no, 손익액, 이익률 계산 메서드
    private List<AssetDisposalDTO> noMargin(int page, int size, Page<Asset> assetPage) {
        List<AssetDisposalDTO> assetDtos = convertToDisposalDto(assetPage);

        Long no;
        int saleMargin = 0, saleMarginRate = 0;

        // 자산조회 시 자산별로 페이지 NO 세팅
        for (AssetDisposalDTO asset1 : assetDtos) {
            no = assetPage.getTotalElements() - ((long) (page - 1) * size) - assetDtos.indexOf(asset1);

            // 매각금액이 null이 아닐 경우 손익액 및 이익률 계산
            if (asset1.getSaleAmount() != null) {
                // 매각손익액 계산 (매각금액 - 장부가액) = 손익액
                saleMargin = asset1.getSaleAmount() - asset1.getBookValue();

                // 장부가액이 0이 아닐 경우 이익률 계산
                // 이익률 = 손익액 / 장부가액 * 100
                if (asset1.getBookValue() != 0) {
                    saleMarginRate = (int) Math.ceil(((double) saleMargin / asset1.getBookValue()) * 100);
                } else {
                    // 장부가액이 0일 경우
                    // 이익률 계산불가, 이익률 100퍼로 고정
                    saleMarginRate = 100;
                }
            }

            asset1.setSaleMargin(saleMargin);
            asset1.setSaleMarginRate(saleMarginRate);

            // 손익액, 이익률 0으로 초기화
            saleMargin = 0;
            saleMarginRate = 0;
        }

        return assetDtos;
    }
}