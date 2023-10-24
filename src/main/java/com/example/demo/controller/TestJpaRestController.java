package com.example.demo.controller;

import com.example.demo.service.AssetService;
import com.example.demo.vo.AssetVo;
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
@RequestMapping("assetTest")
public class TestJpaRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AssetService assetService;

    @GetMapping(value="/{wrms_asset_code}, produces = { MediaType.APPLICATION_JSON_VALUE }")
    public ResponseEntity<AssetVo> getAsset(@PathVariable("wrms_asset_code") String wrms_asset_code){
        List<AssetVo> asset = assetService.findByWrmsAssetCode(wrms_asset_code);
        return new ResponseEntity<AssetVo>(asset.get(1), HttpStatus.OK);
    }
}
