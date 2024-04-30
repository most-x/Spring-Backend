package com.mostx.asset.controller;

import com.mostx.asset.dto.*;
import com.mostx.asset.repository.DashboardRepository;
import com.mostx.asset.response.Response;
import com.mostx.asset.response.ResponsePageInfo;
import com.mostx.asset.service.AssetDepreciationService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Tag(name = "자산감가상각", description = "자산감가상각 API")
//@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class AssetDepreciationController {
    private final AssetService assetService;
    private final AssetDepreciationService assetDepreciationService;
    private final DashboardRepository dashboardRepository;

    // Asset DashBoard
    @Operation(summary = "대시보드", description = "대시보드 표기정보")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/dashboard")
    public List<DashboardDTO> dashBoard() {
        return dashboardRepository.findDashboard();
    }

    // 전체 자산조회
    @Operation(summary = "전체 자산 조회", description = "자산을 전체 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = AssetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "자산정보가 존재하지 않습니다."),
    })
    public ResponsePageInfo findAll(@RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "10") int size){
        return assetService.findAll(page, size);
    }

    @Operation(summary = "자산 처분 조회", description = "자산을 전체 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/disposal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = AssetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "자산정보가 존재하지 않습니다."),
    })
    public ResponsePageInfo findDisposalAll(@RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        return assetService.findDisposalAll(page, size);
    }

    // sno으로 자산조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public AssetDetailDTO findAsset(@PathVariable("id") Long id){
        return assetService.findAsset(id);
    }

    // 사내정보관리시스템 > 자산감가상각 > 건별 자산 조회 최상단 검색조건을 기준으로 자산 리스트를 검색한다.
    @Operation(summary = "건별자산조회 검색", description = "검색내용과 일치하는 건별자산 리스트를 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/asset-depreciation-search")
    public Response<List<AssetDetailDTO>> findAssetDepreciationSearch(@ModelAttribute AssetDepreciationSearchDTO assetDepreciationSearchDto){
        return assetService.findAssetDepreciationSearch(assetDepreciationSearchDto);
    }

    // 자산번호 기준으로 자산을 조회
    // 자산정보 및 자산의 감가상각 현황을 조회하기 위한 API
    @Operation(summary = "자산번호 검색", description = "WRMS에 등록된 자산코드로 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/assets/asset-code/{wrmsAssetCode}")
    @Parameter(name="wrmsAssetCode", description = "WRMS 자산코드", example = "TEST000000001")
    public AssetDetailDTO findAssetCode(@PathVariable("wrmsAssetCode") String wrmsAssetCode){
        return  assetService.findAssetCode(wrmsAssetCode);
    }

    // 자산등록을 위한 API
    @Operation(summary = "자산등록", description = "WRMS에 등록된 자산을 등록한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/asset-regist")
    public String register(@RequestBody @Valid AssetRegistDTO assetRegitsDTO){
        return assetService.register(assetRegitsDTO);
    }

    // 자산 매각 및 폐기를 위한 API
    // API는 patch를 적용하여 받은 내용에 대해서만 업데이트 되도록 구현
    @Operation(summary = "자산매각, 폐기", description = "WRMS에 등록된 자산을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/asset-update")
    public String update(@RequestBody @Valid List<AssetRequestDTO> assetRequestDto) {
        return assetService.update(assetRequestDto);
    }

    // 전체 자산 검색 API (검색조건을 사용)
    @GetMapping("/asset-search")
    public ResponsePageInfo findSearchAsset(@ModelAttribute AssetResearchDTO assetResearchDto,
                                            @RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        return assetService.findSearchAsset(assetResearchDto, page, size);
    }

    // 자산 처분 검색 API (검색조건을 사용)
    @GetMapping("/asset-disposal-search")
    public ResponsePageInfo findDisposalSearchAsset(@ModelAttribute AssetResearchDTO assetResearchDto,
                                                    @RequestParam(required = false, defaultValue = "1") int page,
                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        return assetService.findDisposalSearchAsset(assetResearchDto, page, size);
    }

    // assetSno으로 감가상각 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/asset-depreciation/{assetCodeSno}")
    public ResponsePageInfo findDepreciation(@PathVariable("assetCodeSno") Long assetCodeSno,
                                             @RequestParam(required = false, defaultValue = "1") int page,
                                             @RequestParam(required = false, defaultValue = "60") int size){
        return assetDepreciationService.findSno(assetCodeSno, page, size);
    }
}
