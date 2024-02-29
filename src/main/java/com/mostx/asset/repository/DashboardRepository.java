package com.mostx.asset.repository;

import com.mostx.asset.dto.DashboardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.mostx.asset.entity.QAsset.asset;
import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
public class DashboardRepository {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public List<Map<String, Object>> findDashboard() {
        CaseBuilder caseBuilder1 = new CaseBuilder();
        int sum = 0;
        Query query = em.createQuery("select max(ad.accumlatedDepreciation) FROM mostx_AssetDepreciation ad group by ad.assetSno");

        NumberExpression<Long> countStatus1 = caseBuilder1.when(asset.assetStatus.eq("정상")).then(1L).otherwise(0L);
        NumberExpression<Long> countStatus2 = caseBuilder1.when(asset.assetStatus.eq("매각")).then(1L).otherwise(0L);
        NumberExpression<Long> countStatus3 = caseBuilder1.when(asset.assetStatus.eq("폐기")).then(1L).otherwise(0L);
        NumberExpression<Long> startDateOpen = caseBuilder1.when(asset.initialStartDate.isNotNull()).then(1L).otherwise(0L);
        NumberExpression<Long> startDateNotOpen = caseBuilder1.when(asset.initialStartDate.isNull()).then(1L).otherwise(0L);

        List<DashboardDto> result = jpaQueryFactory.select(
                        Projections.constructor(DashboardDto.class,
                                count(asset.sno),
                                countStatus1.sum(),
                                countStatus2.sum(),
                                countStatus3.sum(),
                                startDateOpen.sum(),
                                startDateNotOpen.sum(),
                                asset.bookValue.sum(),
                                asset.totalPrice.sum()
                        )
                )
                .from(asset)
                .fetch();

        List<Object> results = query.getResultList();

        for (Object sumResult : results) {
            sum += (int) sumResult;
        }

        List<Map<String, Object>> resultMap = new ArrayList<>();
        for (DashboardDto list : result) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("totalAsset", list.getTotalAsset());
            row.put("assetNormal", list.getNormalAsset());
            row.put("assetSale", list.getSaleAsset());
            row.put("assetDispose", list.getDisposeAsset());
            row.put("assetStart", list.getStartAsset());
            row.put("assetNonStart", list.getNonStartAsset());
            row.put("totalPriceAsset", list.getTotalPrice());
            row.put("bookValueAsset", list.getBookValueTotal());
            row.put("sumDepreciationAsset", sum);
            resultMap.add(row);
        }

        return resultMap;
    }
}
