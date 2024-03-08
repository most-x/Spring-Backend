package com.mostx.asset.service;

import com.mostx.asset.dto.AssetDepreciationDTO;
import com.mostx.asset.entity.AssetDepreciation;
import com.mostx.asset.repository.AssetDepreciationRepository;
import com.mostx.asset.response.ResponsePageInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetDepreciationService {
    private final AssetDepreciationRepository assetDepreciationRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    private List<AssetDepreciationDTO> convertToDto(Page<AssetDepreciation> assetDepreciation) {
        return assetDepreciation.stream()
                .map(assetDepreciation1 -> modelMapper.map(assetDepreciation1, AssetDepreciationDTO.class))
                .collect(Collectors.toList());
    }

    public ResponsePageInfo findSno(Long assetCodeSno, int page, int size) {
        // 게시글의 no, totalPage 수 표기용도
        long no;

        // 감가상각 데이터 page, size 적용
        Page<AssetDepreciation> assetDepreciations = assetDepreciationRepository.findByAssetSno(assetCodeSno, PageRequest.of(page, size));

        List<AssetDepreciationDTO> assetDepreciationDtos = convertToDto(assetDepreciations);

        for (AssetDepreciationDTO numberCount : assetDepreciationDtos) {
            // 감가상각 게시글 no 데이터
            no = assetDepreciations.getTotalElements() - ((long) page * size) - assetDepreciationDtos.indexOf(numberCount);

            numberCount.setNo(no);
        }

        return new ResponsePageInfo(assetDepreciationDtos, assetDepreciations.getTotalElements(), (long) assetDepreciations.getTotalPages());
    }

    // 자산 감가상각 데이터 insert
    // 자산 개시일 및 내용연수 입력된 자산을 기준으로 매월 1일에 스케줄러를 통하여 실행
    @Transactional
    public Long saveDepreciation(AssetDepreciation assetDepreciation1) {
        AssetDepreciation savedDepreciation = assetDepreciationRepository.save(assetDepreciation1);
        return savedDepreciation.getSno();
    }
}