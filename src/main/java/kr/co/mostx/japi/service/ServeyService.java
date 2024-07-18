package kr.co.mostx.japi.service;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import kr.co.mostx.japi.dto.ServeyCryptDto;
import kr.co.mostx.japi.dto.ServeyDto;
import kr.co.mostx.japi.dto.ServeySearchDto;
import kr.co.mostx.japi.dto.ServeyStaticsSearchDto;
import kr.co.mostx.japi.entity.Servey;
import kr.co.mostx.japi.exception.CustomException;
import kr.co.mostx.japi.exception.ErrorCode;
import kr.co.mostx.japi.repository.ServeyRepository;
import kr.co.mostx.japi.repository.impl.ServeyCustomRepositoryImpl;
import kr.co.mostx.japi.response.ServeyResponse;
import kr.co.mostx.japi.response.ServeyResponsePage;
import kr.co.mostx.japi.response.ServeyScoreResponse;
import kr.co.mostx.japi.type.ScoreType;
import kr.co.mostx.japi.type.SearchType;
import kr.co.mostx.japi.type.ServeySortType;
import kr.co.mostx.japi.util.AesUtil;
import kr.co.mostx.japi.util.DataObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static common.common.getBooleanExpression;
import static kr.co.mostx.japi.entity.QServey.servey;

@Service
@RequiredArgsConstructor
public class ServeyService {
    private final ServeyRepository serveyRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ServeyCustomRepositoryImpl serveyCustomRepository;

    @Transactional
    public Servey saveServey(ServeyDto serveyRequestDto) {
        // 만족도조사 등록 전 검증
        validateDuplicateServey(serveyRequestDto.getServeyNumber());

        // 평균점수 및 합계점수 계산 후 등록
        double avgScore = (double) (serveyRequestDto.getServeyOne() + serveyRequestDto.getServeyTwo() + serveyRequestDto.getServeyThree() + serveyRequestDto.getServeyFour() + serveyRequestDto.getServeyFive()) / 5;
        int totalScore = serveyRequestDto.getServeyOne() + serveyRequestDto.getServeyTwo() + serveyRequestDto.getServeyThree() + serveyRequestDto.getServeyFour() + serveyRequestDto.getServeyFive();

        Servey servey = Servey.builder()
                .serveyOne(serveyRequestDto.getServeyOne())
                .serveyTwo(serveyRequestDto.getServeyTwo())
                .serveyThree(serveyRequestDto.getServeyThree())
                .serveyFour(serveyRequestDto.getServeyFour())
                .serveyFive(serveyRequestDto.getServeyFive())
                .userName(serveyRequestDto.getUserName())
                .userPhone(serveyRequestDto.getUserPhone())
                .consultantName(serveyRequestDto.getConsultantName())
                .platform(serveyRequestDto.getPlatform())
                .serveyNumber(serveyRequestDto.getServeyNumber())
                .avgScore(avgScore)
                .totalScore(totalScore)
                .build();

        return serveyRepository.save(servey);
    }

    /**
     * serveyNumber 중복 확인, 하나의 상담번호에는 하나의 만족도조사 등록가능
     * @param serveyNumber
     */
    private void validateDuplicateServey(String serveyNumber) {
        if("".equals(serveyNumber) || serveyNumber == null){
            throw new IllegalArgumentException(String.format("상담정보가 올바르지 않습니다 : %s", serveyNumber));
        }

        Optional<Servey> serveyOptional = serveyRepository.findByServeyNumber(serveyNumber);

        serveyOptional.ifPresent(findServey -> {
            throw new CustomException(ErrorCode.SERVEY_ALREADY_COMPLETE);
        });
    }

    // 만족도조사 리스트
    public ServeyResponsePage<List<ServeyDto>> findServey(int pageNumber, int pageSize) {
        Page<Servey> serveyList = serveyRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        List<ServeyDto> serveyDtoList = serveyList.stream().map(ServeyDto::convertToServeyFindAllResponse).toList();

        return new ServeyResponsePage<>(serveyDtoList, serveyList.getTotalPages(), serveyList.getTotalElements());
    }

    // 만족도조사 리스트 검색
    public ServeyResponsePage<List<ServeyDto>> findSearchServey(ServeySearchDto serveySearchDto, ServeySortType[] sortType, int pageNumber, int pageSize) {
        // sort 기준이 입력되지 않았을 경우 queryDsl 권장 order by null ASC 반환
//        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(sortType);

        List<Servey> serveySearch = jpaQueryFactory
                .selectFrom(servey)
                .where(
                        searchDate(serveySearchDto.getStartDate(), serveySearchDto.getEndDate()),
                        eqSearchType(serveySearchDto.getSearchType(), serveySearchDto.getSearchWord()),
                        searchPlatform(serveySearchDto.getPlatform()),
                        eqScoreType(serveySearchDto.getScoreType(), serveySearchDto.getMinScore(), serveySearchDto.getMaxScore())
                )
                .orderBy(createOrderSpecifier(sortType).toArray(OrderSpecifier[]::new))
                .fetch();

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        int startPage = (int) pageRequest.getOffset();
        int endPage = Math.min((startPage + pageRequest.getPageSize()), serveySearch.size());

        Page<Servey> serveyPage = new PageImpl<>(serveySearch.subList(startPage, endPage), pageRequest, serveySearch.size());
        List<ServeyDto> serveyDtoList = serveyPage.stream().map(ServeyDto::convertToServeyFindAllResponse).toList();

        return new ServeyResponsePage<>(serveyDtoList, serveyPage.getTotalPages(), serveyPage.getTotalElements());
    }

    // 주간 통계 데이터
    public ServeyResponse weeklyData() {
        return serveyCustomRepository.weeklyStaticsServey();
    }

    // 월간 통계 데이터
    public ServeyResponse monthlyData() {
        return serveyCustomRepository.monthlyStaticsServey();
    }

    // 점수 통계 데이터
    public ServeyScoreResponse scoreData(ServeyStaticsSearchDto searchDto) {
        return serveyCustomRepository.scoreStaticsServey(searchDto);
    }

    // 상담사 통계 데이터
    public ServeyResponse consultantData() {
        return serveyCustomRepository.consultantStaticsServey();
    }

    // 전일 통계 데이터
    public ServeyResponse dailyData(){
        return serveyCustomRepository.dailyStaticsServey();
    }

    private List<OrderSpecifier<?>> createOrderSpecifier(ServeySortType[] sortType) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        // sort 기준이 입력되지 않았을 경우 id만 내림차순 정렬 후 반환
        if (sortType == null) {
            orders.add(new OrderSpecifier<>(Order.DESC, servey.id));
            return orders;
        }

        for (ServeySortType serveySortType : sortType) {
            switch (serveySortType) {
                case avgScoreDESC -> orders.add(new OrderSpecifier<>(Order.DESC, servey.avgScore));
                case avgScoreASC -> orders.add(new OrderSpecifier<>(Order.ASC, servey.avgScore));
                case totalScoreDESC -> orders.add(new OrderSpecifier<>(Order.DESC, servey.totalScore));
                case totalScoreASC -> orders.add(new OrderSpecifier<>(Order.ASC, servey.totalScore));
            };
        }
        // totalScore, avgScore 정렬 후 id 기준으로 내림차순 정렬
        orders.add(new OrderSpecifier<>(Order.DESC, servey.id));
        // sort 기준에 따른 order specifier 반환
        return orders;
    }

    public List<ServeyDto> findExcelDownload(ServeySearchDto serveySearchDto) {
        List<Servey> serveyList = jpaQueryFactory
                .selectFrom(servey)
                .where(
                        searchDate(serveySearchDto.getStartDate(), serveySearchDto.getEndDate()),
                        eqSearchType(serveySearchDto.getSearchType(), serveySearchDto.getSearchWord()),
                        searchPlatform(serveySearchDto.getPlatform()),
                        eqScoreType(serveySearchDto.getScoreType(), serveySearchDto.getMinScore(), serveySearchDto.getMaxScore())
                )
                .fetch();

        List<ServeyDto> serveyDtoList = serveyList.stream().map(ServeyDto::convertToServeyFindAllResponse).toList();

        return serveyDtoList;
    }

    // startDate, endDate 입력여부에 따른 goe, loe
    private BooleanExpression searchDate(LocalDate startDate, LocalDate endDate) {
        return getBooleanExpression(startDate, endDate);
    }

    // 검색타입에 대한 검색
    private BooleanExpression eqSearchType(SearchType searchType, String keyWord) {
        if (searchType == null) {
            return null;
        }

        return searchType.getEq(keyWord);
    }

    private BooleanExpression searchPlatform(String platform) {
        return StringUtils.hasText(platform) ? servey.platform.stringValue().contains(platform) : null;
    }

    // 점수 타입에 대한 검색
    private BooleanExpression eqScoreType(ScoreType scoreType, Integer minScore, Integer maxScore) {
        if (scoreType == null || (minScore == null && maxScore == null)) {
            return null;
        }

        return scoreType.getScore(minScore, maxScore);
    }

    public String findServeyNumber(String serveyNumber) {
        List<Servey> serveyList = serveyRepository.searchServey(serveyNumber);
        if (serveyList.isEmpty()) {
            return "N";
        } else {
            return "Y";
        }
    }

    public String encryptData(String jsonData) {
        return AesUtil.aesCBCEncode(jsonData);
    }

    public ServeyCryptDto decryptData(String elementData) {
        String decodedData = UriUtils.decode(elementData, StandardCharsets.UTF_8);
        DataObject decryptJson = AesUtil.aesCBCDecode(decodedData);
        String registStatus = this.findServeyNumber(decryptJson.getServeyNumber());
//        String onedayCheck = "N";
//
//        if (LocalDateTime.now().isAfter(decryptJson.getMessageTime().plusHours(24))) {
//            onedayCheck = "Y";
//        }

        return new ServeyCryptDto(decryptJson, registStatus);
    }

}
