package kr.co.mostx.japi.repository;

import kr.co.mostx.japi.entity.AssetDepreciation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssetDepreciationRepository extends JpaRepository<AssetDepreciation, Long> {
    @Query("select m from mostx_AssetDepreciation m where m.assetSno.sno = :assetCodeSno order by m.bookValue asc")
    Page<AssetDepreciation> findByAssetSno(@Param("assetCodeSno") Long assetCodeSno, Pageable pageable);
}