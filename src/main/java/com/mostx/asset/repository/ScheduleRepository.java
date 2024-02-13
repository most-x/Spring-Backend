package com.mostx.asset.repository;

import com.mostx.asset.entity.Asset;
import com.mostx.asset.entity.AssetDepreciation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {
    private final EntityManager em;

    public List<Asset> findAll() {
        return em.createQuery(
                        "select a from mostx_AssetRegist a", Asset.class)
                .getResultList();
    }

    public AssetDepreciation findDepreciation(Long assetId) {
        return em.createQuery(
                        "select d from mostx_AssetDepreciation d" +
                                " where d.assetSno.sno= :assetId" +
                                " order by depreciationDate desc, accumlatedDepreciation desc limit 1", AssetDepreciation.class)
                .setParameter("assetId", assetId)
                .getSingleResult();
    }
}
