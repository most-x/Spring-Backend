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
            row.put("총 자산", list.getTotalAsset());
            row.put("정상", list.getNormalAsset());
            row.put("매각", list.getSaleAsset());
            row.put("폐기", list.getDisposeAsset());
            row.put("개시", list.getStartAsset());
            row.put("미개시", list.getNonStartAsset());
            row.put("총 금액", list.getTotalPrice());
            row.put("장부가액", list.getBookValueTotal());
            row.put("누계액", sum);
            resultMap.add(row);
        }

        return resultMap;
    }
}
