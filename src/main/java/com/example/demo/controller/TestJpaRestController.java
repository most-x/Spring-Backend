package com.example.demo.controller;

import com.example.demo.repository.AssetRepository;
import com.example.demo.service.AssetService;
import com.example.demo.vo.AssetVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("asset")
@RequiredArgsConstructor
public class TestJpaRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AssetRepository assetRepository;
    @Autowired
    AssetService assetService;

    @GetMapping("asset/{wrms_asset_code}")
    public AssetService getAsset(@PathVariable String wrms_asset_code){
        return (AssetService) assetRepository.findByWrms_asset_code(wrms_asset_code);
    }
}
