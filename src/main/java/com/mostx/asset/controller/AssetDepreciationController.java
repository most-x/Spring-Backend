package com.mostx.asset.controller;

import com.mostx.asset.dto.AssetDTO;
import com.mostx.asset.dto.AssetDepreciationSearchDTO;
import com.mostx.asset.dto.AssetRequestDTO;
import com.mostx.asset.dto.AssetResearchDTO;
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
import jakarta.validation.Valid;
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

    @Operation(summary = "감가상각 자산검색", description = "검색내용과 일치하는 자산의 리스트를 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/asset-depreciation-search")
    public Response<?> findAssetDepreciationSearch(@ModelAttribute AssetDepreciationSearchDTO assetDepreciationSearchDto, @PageableDefault(size = 10) Pageable pageable){
        Page<AssetDTO> assetDepreciationSearchDto1 = assetService.findAssetDepreciationSearch(assetDepreciationSearchDto, pageable);
        return new Response<>("true", "조회 성공", assetDepreciationSearchDto1);
    }

    @Operation(summary = "자산번호 검색", description = "WRMS에 등록된 자산코드로 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/assets/asset-code/{wrmsAssetCode}")
    @Parameter(name="wrmsAssetCode", description = "WRMS 자산코드", example = "TEST000000001")
    public Response<?> findAssetCode(@PathVariable("wrmsAssetCode") String wrmsAssetCode){
        Asset asset = assetService.findAssetCode(wrmsAssetCode);
        return new Response<>("true", "조회 성공", asset);
    }

    @Operation(summary = "자산등록", description = "WRMS에 등록된 자산을 등록한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/asset-regist")
    public Response<?> register(@RequestBody @Valid AssetDTO assetDto){
        AssetDTO savedAsset = assetService.register(assetDto);
        return new Response<>("true", "등록 성공", savedAsset);
    }

    @Operation(summary = "자산매각, 폐기", description = "WRMS에 등록된 자산을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/asset-update/{id}")
    public Response<?> update(@PathVariable Long id, @RequestBody @Valid AssetRequestDTO assetRequestDto) {
        AssetDTO updateAsset = assetService.update(id, assetRequestDto);
        return new Response<>("true", "수정 성공", updateAsset);
    }

    @GetMapping("/asset-search")
    public Response<?> findSearchAsset(@ModelAttribute AssetResearchDTO assetResearchDto, @PageableDefault(size = 10) Pageable pageable) {
        Page<AssetDTO> researchDto = assetService.findSearchAsset(assetResearchDto, pageable);
        return new Response<>("true", "조회 성공", researchDto);
    }
}
