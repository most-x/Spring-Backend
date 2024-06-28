package kr.co.mostx.japi.repository;

import kr.co.mostx.japi.entity.Asset;
import kr.co.mostx.japi.entity.AssetDepreciation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AssetScheduleRepository {
    private final EntityManager em;

    public List<Asset> findAll() {
        return em.createQuery(
                        "select a from mostx_AssetRegist a", Asset.class)
                .getResultList();
    }

    public List<AssetDepreciation> findDepreciation(Long assetId) {
        return em.createQuery(
                        "select d from mostx_AssetDepreciation d" +
                                " where d.assetSno.sno= :assetId" +
                                " order by depreciationDate desc, accumlatedDepreciation desc", AssetDepreciation.class)
                .setParameter("assetId", assetId)
                .getResultList();
    }
}
