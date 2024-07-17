package kr.co.mostx.japi.repository;

import kr.co.mostx.japi.dto.ServeyStaticsSearchDto;
import kr.co.mostx.japi.entity.Servey;
import kr.co.mostx.japi.response.ServeyResponse;
import kr.co.mostx.japi.response.ServeyScoreResponse;

import java.util.List;

public interface ServeyCustomRepository {
    List<Servey> searchServey(String serveyNumber);

    ServeyResponse weeklyStaticsServey();

    // 월간 통계 데이터
    ServeyResponse monthlyStaticsServey();

    // 점수 통계 데이터
    ServeyScoreResponse scoreStaticsServey(ServeyStaticsSearchDto searchDto);

    ServeyResponse consultantStaticsServey();

    ServeyResponse dailyStaticsServey();
}

