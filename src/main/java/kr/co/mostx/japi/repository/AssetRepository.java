package kr.co.mostx.japi.repository;

import kr.co.mostx.japi.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByWrmsAssetCode(String wrms_asset_code);

    Optional<Asset> findBySno(Long sno);
}
