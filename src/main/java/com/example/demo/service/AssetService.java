package com.example.demo.service;
import com.example.demo.entity.Asset;
import com.example.demo.repository.AssetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    public List<Asset> findAll(){
        return assetRepository.findAll();
    }

    public Asset findByWrmsAssetCode(String wrms_asset_code){
        return assetRepository.findByWrmsAssetCode(wrms_asset_code).orElseThrow(()->{
            return new EntityNotFoundException("자산코드로 등록된 자산이 없습니다.");
        });
    }
}