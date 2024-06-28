package kr.co.mostx.japi.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mostx.japi.entity.Servey;
import kr.co.mostx.japi.repository.ServeyCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.mostx.japi.entity.QServey.servey;

@Repository
@RequiredArgsConstructor
public class ServeyCustomRepositoryImpl implements ServeyCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

//    @Override
//    public List<ServeyScoreDto> averageScore() {
//        return jpaQueryFactory
//                .select(Projections.constructor(ServeyScoreDto.class,
//                        servey.kindServey.avg(),
//                        servey.serviceServey.avg(),
//                        servey.subscribeServey.avg(),
//                        servey.deliveryServey.avg(),
//                        servey.recommendServey.avg(),
//                        servey.homepageServey.avg(),
//                        servey.qualityServey.avg(),
//                        servey.exchangeServey.avg(),
//                        servey.useServey.avg(),
//                        servey.platform))
//                .from(servey)
//                .groupBy(servey.platform)
//                .fetch();
//
//    }

    @Override
    public List<Servey> searchServey(String serveyNumber) {
        return jpaQueryFactory
                .selectFrom(servey)
                .where(servey.serveyNumber.eq(serveyNumber))
                .fetch();
    }
}