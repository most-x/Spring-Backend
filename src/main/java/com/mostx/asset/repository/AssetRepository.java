package com.mostx.asset.repository;

import com.mostx.asset.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByWrmsAssetCode(String wrms_asset_code);

    Optional<Asset> findBySno(Long sno);
}
