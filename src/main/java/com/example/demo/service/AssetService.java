package com.example.demo.service;
import com.example.demo.repository.AssetRepository;
import com.example.demo.vo.AssetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    public List<AssetVo> findAll(){
        List<AssetVo> assets = new ArrayList<>();
        assetRepository.findAll().forEach(e -> assets.add(e));
        return assets;
    }

    public List<AssetVo> findByWrmsAssetCode(String wrms_asset_code){
        return assetRepository.findByWrms_asset_code(wrms_asset_code);
    }
}