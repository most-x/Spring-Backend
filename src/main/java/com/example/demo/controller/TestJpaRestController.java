package com.example.demo.controller;

import com.example.demo.repository.AssetRepository;
import com.example.demo.response.Response;
import com.example.demo.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestJpaRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AssetService assetService;

    @Operation(summary = "전체 자산 조회", description = "전체 자산을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/assets")
    public Response<?> findAll(){
        return new Response<>("true", "조회 성공", assetService.findAll());
    }
}
