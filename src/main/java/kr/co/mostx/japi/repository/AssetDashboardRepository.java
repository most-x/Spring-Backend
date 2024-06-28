package kr.co.mostx.japi.repository;

import kr.co.mostx.japi.dto.AssetDashboardDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.mostx.japi.entity.QAsset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
public class AssetDashboardRepository {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public List<AssetDashboardDTO> findDashboard() {
        CaseBuilder caseBuilder1 = new CaseBuilder();

        NumberExpression<Long> countStatus1 = caseBuilder1.when(QAsset.asset.assetStatus.eq("정상")).then(1L).otherwise(0L);
        NumberExpression<Long> countStatus2 = caseBuilder1.when(QAsset.asset.assetStatus.eq("매각")).then(1L).otherwise(0L);
        NumberExpression<Long> countStatus3 = caseBuilder1.when(QAsset.asset.assetStatus.eq("폐기")).then(1L).otherwise(0L);
        NumberExpression<Long> startDateOpen = caseBuilder1.when(QAsset.asset.initialStartDate.isNotNull()).then(1L).otherwise(0L);
        NumberExpression<Long> startDateNotOpen = caseBuilder1.when(QAsset.asset.initialStartDate.isNull()).then(1L).otherwise(0L);

        return jpaQueryFactory.select(
                        Projections.constructor(AssetDashboardDTO.class,
                                count(QAsset.asset.sno),
                                countStatus1.sum(),
                                countStatus2.sum(),
                                countStatus3.sum(),
                                startDateOpen.sum(),
                                startDateNotOpen.sum(),
                                QAsset.asset.depreciationTotalprice.sum(),
                                QAsset.asset.bookValue.sum(),
                                QAsset.asset.supplyPrice.sum()
                        )
                )
                .from(QAsset.asset)
                .fetch();
    }
}
