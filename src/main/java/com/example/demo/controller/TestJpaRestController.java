package com.example.demo.controller;

import com.example.demo.entity.Asset;
import com.example.demo.repository.AssetRepository;
import com.example.demo.response.Response;
import com.example.demo.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestJpaRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AssetService assetService;

    @Operation(summary = "전체 자산 조회", description = "전체 자산을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/assets")
    public Response<?> findAll(Pageable pageable){
        return new Response<>("true", "조회 성공", assetService.findAll(pageable));
    }

    @Operation(summary = "자산번호 검색", description = "WRMS에 등록된 자산코드로 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/assets/asset-code/{wrmsAssetCode}")
    @Parameter(name="wrmsAssetCode", description = "WRMS 자산코드", example = "TEST000000001")
    public Response<?> findAssetCode(@PathVariable("wrmsAssetCode") String wrmsAssetCode){
        Asset asset = assetService.findAssetCode(wrmsAssetCode);
        return new Response<>("true", "조회 성공", assetService.findAssetCode(wrmsAssetCode));
    }

    @GetMapping("/assets/asset-search")
    public Response<?> findSearchAsset(@RequestParam(required = false) String wrmsAssetCode, @RequestParam(required = false) String wrmsItemCode, @RequestParam(required = false) String ilsangProductCode,
                                       @RequestParam(required = false) String serialNumber, @RequestParam(required = false) String productName,
                                       @RequestParam(required = false) String assetStatus, @RequestParam(required = false) String assetUsage, @RequestParam(required = false) Integer startPrice,
                                       @RequestParam(required = false) Integer endPrice, @RequestParam(required = false) Date initialStartDate, @RequestParam(required = false) String warehouseNumber,
                                       Pageable pageable) {

        return new Response<>("true", "조회 성공", assetService.findSearchAsset(wrmsAssetCode, wrmsItemCode, ilsangProductCode, serialNumber, productName,
                assetStatus, assetUsage, startPrice, endPrice, initialStartDate, warehouseNumber, pageable));
    }
}
