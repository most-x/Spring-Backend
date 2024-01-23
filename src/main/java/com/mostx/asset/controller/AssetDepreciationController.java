package com.mostx.asset.controller;

import com.mostx.asset.dto.AssetDTO;
import com.mostx.asset.entity.Asset;
import com.mostx.asset.response.Response;
import com.mostx.asset.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Tag(name = "자산감가상각", description = "자산감가상각 API")
public class AssetDepreciationController {
    private final AssetService assetService;

    @Operation(summary = "전체 자산 조회", description = "자산을 전체 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = AssetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "자산정보가 존재하지 않습니다."),
    })
    public Response<?> findAll(@PageableDefault(size = 10) Pageable pageable){
        Page<AssetDTO> assetPage = assetService.findAll(pageable);
        return new Response<>("true", "조회 성공", assetPage);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Response<?> findAsset(@PathVariable("id") Long id){
        AssetDTO idAsset = assetService.findAsset(id);
        return new Response<>("true", "조회 성공", idAsset);
    }

    @Operation(summary = "자산번호 검색", description = "WRMS에 등록된 자산코드로 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/assets/asset-code/{wrmsAssetCode}")
    @Parameter(name="wrmsAssetCode", description = "WRMS 자산코드", example = "TEST000000001")
    public Response<?> findAssetCode(@PathVariable("wrmsAssetCode") String wrmsAssetCode){
        Asset asset = assetService.findAssetCode(wrmsAssetCode);
        return new Response<>("true", "조회 성공", asset);
    }

    @GetMapping("/assets/asset-search")
    public Response<?> findSearchAsset(@RequestParam(required = false) String wrmsAssetCode, @RequestParam(required = false) String wrmsItemCode, @RequestParam(required = false) String ilsangProductCode,
                                       @RequestParam(required = false) String serialNumber, @RequestParam(required = false) String productName,
                                       @RequestParam(required = false) String assetStatus, @RequestParam(required = false) String assetUsage, @RequestParam(required = false) Integer startPrice,
                                       @RequestParam(required = false) Integer endPrice, @RequestParam(required = false) LocalDate initialStartDate, @RequestParam(required = false) String warehouseNumber,
                                       Pageable pageable) {

        return new Response<>("true", "조회 성공", assetService.findSearchAsset(wrmsAssetCode, wrmsItemCode, ilsangProductCode, serialNumber, productName,
                assetStatus, assetUsage, startPrice, endPrice, initialStartDate, warehouseNumber, pageable));
    }
}
