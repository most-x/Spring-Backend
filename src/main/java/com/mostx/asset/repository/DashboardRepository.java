package com.mostx.asset.repository;

import com.mostx.asset.dto.DashboardDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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

    public List<DashboardDTO> findDashboard() {
        CaseBuilder caseBuilder1 = new CaseBuilder();

        NumberExpression<Long> countStatus1 = caseBuilder1.when(asset.assetStatus.eq("정상")).then(1L).otherwise(0L);
        NumberExpression<Long> countStatus2 = caseBuilder1.when(asset.assetStatus.eq("매각")).then(1L).otherwise(0L);
        NumberExpression<Long> countStatus3 = caseBuilder1.when(asset.assetStatus.eq("폐기")).then(1L).otherwise(0L);
        NumberExpression<Long> startDateOpen = caseBuilder1.when(asset.initialStartDate.isNotNull()).then(1L).otherwise(0L);
        NumberExpression<Long> startDateNotOpen = caseBuilder1.when(asset.initialStartDate.isNull()).then(1L).otherwise(0L);

        return jpaQueryFactory.select(
                        Projections.constructor(DashboardDTO.class,
                                count(asset.sno),
                                countStatus1.sum(),
                                countStatus2.sum(),
                                countStatus3.sum(),
                                startDateOpen.sum(),
                                startDateNotOpen.sum(),
                                asset.depreciationTotalprice.sum(),
                                asset.bookValue.sum(),
                                asset.supplyPrice.sum()
                        )
                )
                .from(asset)
                .fetch();
    }
}
