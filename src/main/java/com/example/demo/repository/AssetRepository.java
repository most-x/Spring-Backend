package com.example.demo.repository;

import com.example.demo.vo.AssetVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<AssetVo, Long> {
    public List<AssetVo> findByWrms_asset_code(String wrms_asset_code);
}
