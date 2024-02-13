package com.mostx.asset.service;

import com.mostx.asset.entity.AssetDepreciation;
import com.mostx.asset.repository.AssetDepreciationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetDepreciationService {
    private final AssetDepreciationRepository assetDepreciationRepository;

    @Transactional
    public Long saveDepreciation(AssetDepreciation assetDepreciation1) {
        assetDepreciationRepository.save(assetDepreciation1);
        return assetDepreciation1.getSno();
    }
}