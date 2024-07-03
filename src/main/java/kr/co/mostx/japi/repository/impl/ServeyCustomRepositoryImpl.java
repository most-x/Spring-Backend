package kr.co.mostx.japi.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mostx.japi.entity.QServey;
import kr.co.mostx.japi.entity.Servey;
import kr.co.mostx.japi.repository.ServeyCustomRepository;
import kr.co.mostx.japi.response.ServeyResponse;
import kr.co.mostx.japi.type.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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


    // Weekly 통계
    @Override
    public ServeyResponse weeklyStaticsServey() {
        LocalDate nowDate = LocalDate.now();
        LocalDate prevDate = nowDate.minusDays(7);
        TreeMap<String, TreeMap<String, Long>> resultMap = new TreeMap<>();

        // queryDsl에서 date_format('%Y-%m-%d') 로 데이터 select 를 위한 변수
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                servey.createdDate,
                ConstantImpl.create("%Y-%m-%d")
        );

        // 일주일 데이터 통계자료 추출
        // Group by : platform, Date
        //
        List<Tuple> staticsData = jpaQueryFactory
                .select(
                        servey.count().as("cnt"),
                        servey.platform,
                        formattedDate.as("date")
                )
                .from(servey)
                .where(servey.createdDate.between(prevDate.atStartOfDay(), nowDate.atStartOfDay()))
                .groupBy(servey.platform, formattedDate)
                .fetch();


        int diff = Period.between(prevDate, nowDate).getDays();

        // 첫째날부터 어제까지의 Date Map에 매핑,
        // totalCnt가 항상 필요하므로 세팅된 Map을 식별하여 해당 Key, Value 세팅 진행
        for (int i = 0; i < diff; i++) {
            resultMap.put(prevDate.plusDays(i).toString(), new TreeMap<>());
            resultMap.get(prevDate.plusDays(i).toString()).put("totalCnt", 0L);
        }

        // queryDsl에서 받아온 객체로 loop 진행
        // 매핑되는 데이터가 있을 경우 해당 데이터에 Key, Value 세팅 진행
        for (Tuple tuple : staticsData) {
            Long serveyCnt = tuple.get(servey.count().as("cnt"));
            Platform platform = tuple.get(servey.platform);
            String date = tuple.get(formattedDate.as("date"));

            resultMap.get(date).put(String.valueOf(platform), serveyCnt);
            resultMap.get(date).put("totalCnt", resultMap.get(date).get("totalCnt") + serveyCnt);
        }

        return new ServeyResponse("주간 통계 데이터", resultMap);
    }

    // monthly 통계
    @Override
    public ServeyResponse monthlyStaticsServey() {
        /**
         *"2024": {
         *    "01": {
         *        "ILSANG": 237,
         *        "WRMS": 148,
         *        "totalCnt": 385
         *    }
         *}
         *
         * 위의 형식으로 데이터를 표기하기 위한 Map
         */
        TreeMap<String, TreeMap<String, TreeMap<String, Long>>> monthlyData = new TreeMap<>();
        // queryDsl에서 date_format('%Y-%m') 로 데이터 select 를 위한 변수
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                servey.createdDate,
                ConstantImpl.create("%Y-%m")
        );

        List<Tuple> monthlyList = jpaQueryFactory
                .select(
                        servey.count().as("cnt"),
                        servey.platform,
                        formattedDate.as("date")
                )
                .from(servey)
                .groupBy(servey.platform, formattedDate)
                .orderBy(formattedDate.asc())
                .fetch();

        for (Tuple tuple : monthlyList) {
            Long monthlyCnt = tuple.get(servey.count().as("cnt"));
            Platform platform = tuple.get(servey.platform);
            String[] splitDate = tuple.get(formattedDate.as("date")).split("-");

            String year = splitDate[0];
            String month = splitDate[1];

            if (!monthlyData.containsKey(year)) {
                monthlyData.put(year, new TreeMap<>());

                for (int i = 1; i < 13; i++) {
                    String loopMonth = String.format("%02d", i);

                    monthlyData.get(year).put(loopMonth, new TreeMap<>());
                    monthlyData.get(year).get(loopMonth).put("totalCnt", 0L);
                }
            }

            if (monthlyData.get(year).containsKey(month)) {
                monthlyData.get(year).get(month).put(String.valueOf(platform), monthlyCnt);
                monthlyData.get(year).get(month).put("totalCnt", monthlyData.get(year).get(month).get("totalCnt") + monthlyCnt);
            }
        }

        return new ServeyResponse("월간 통계 데이터", monthlyData);
    }

    // scoreStatistics
    @Override
    public ServeyResponse scoreStaticsServey() {
        Map<String, Map<String, Map<String, Object>>> scoreData = new LinkedHashMap<>();
        String[] serveyList = {"serveyOne", "serveyTwo", "serveyThree", "serveyFour", "serveyFive"};
        List<Integer> scoreArray = new ArrayList<>();

        List<Tuple> scoreList = jpaQueryFactory
                .select(
                        servey.serveyOne,
                        servey.serveyTwo,
                        servey.serveyThree,
                        servey.serveyFour,
                        servey.serveyFive,
                        servey.platform
                )
                .from(servey)
                .fetch();

        for (String s : serveyList) {
            scoreData.put(s, new TreeMap<>());
            scoreData.get(s).put("totalCnt", new TreeMap<>());
            scoreData.get(s).put("WRMS", new TreeMap<>());
            scoreData.get(s).put("ILSANG", new TreeMap<>());
            for (int i = 1; i <= serveyList.length; i++) {
                scoreData.get(s).get("totalCnt").put(String.format("%d", i), 0);
                scoreData.get(s).get("WRMS").put(String.format("%d", i), 0);
                scoreData.get(s).get("ILSANG").put(String.format("%d", i), 0);
            }
        }

        for (Tuple result : scoreList) {
            scoreArray.add(result.get(servey.serveyOne));
            scoreArray.add(result.get(servey.serveyTwo));
            scoreArray.add(result.get(servey.serveyThree));
            scoreArray.add(result.get(servey.serveyFour));
            scoreArray.add(result.get(servey.serveyFive));
            Platform platform = result.get(servey.platform);

            for (int i = 0; i < 5; i++) {
                scoreData.get(serveyList[i]).get(String.valueOf(platform))
                        .put(String.valueOf(scoreArray.get(i)), (int) scoreData.get(serveyList[i]).get(String.valueOf(platform)).getOrDefault(String.valueOf(scoreArray.get(i)), 0) + 1);

                scoreData.get(serveyList[i]).get("totalCnt")
                        .put(String.valueOf(scoreArray.get(i)), (int) scoreData.get(serveyList[i]).get("totalCnt").getOrDefault(String.valueOf(scoreArray.get(i)), 0) + 1);
            }

            // 초기화
            scoreArray.clear();
        }

        for (int i = 0; i < 5; i++) {
            calculateAverage(scoreData.get(serveyList[i]));
        }

        return new ServeyResponse("점수 통계 데이터", scoreData);
    }

    @Override
    public ServeyResponse consultantStaticsServey() {
        // 상담사 통계 data 구조
        Map<String, Map<String, Object>> consultMap = new HashMap<>();
        // 질문 List 배열
        String[] loopServeyList = {"serveyOne", "serveyTwo", "serveyThree", "serveyFour", "serveyFive"};

        List<Tuple> serveyList = jpaQueryFactory
                .select(
                        Expressions.numberTemplate(Long.class, "count({0}) + count({1}) + count({2}) + count({3}) + count({4})",
                                servey.serveyOne, servey.serveyTwo, servey.serveyThree, servey.serveyFour, servey.serveyFive),
                        servey.serveyOne.avg(),
                        servey.serveyTwo.avg(),
                        servey.serveyThree.avg(),
                        servey.serveyFour.avg(),
                        servey.serveyFive.avg(),
                        servey.consultantName
                )
                .from(servey)
                .groupBy(servey.consultantName)
                .fetch();

        for (Tuple result : serveyList) {
            // 질문 전체 count (상담사 group by 기준)
            Long consultCnt = result.get(Expressions.numberTemplate(Long.class, "count({0}) + count({1}) + count({2}) + count({3}) + count({4})",
                    servey.serveyOne, servey.serveyTwo, servey.serveyThree, servey.serveyFour, servey.serveyFive));
            Double[] averages = new Double[loopServeyList.length];
            String consultantName = result.get(servey.consultantName);

            // 질문 배열에 등록 시 자동으로 추가하여 average 계산되도록
            for (int i = 0; i < averages.length; i++) {
                // 질문 배열에 있는 값으로 Tuple 값을 꺼내오기위한 코드
                // 아래의 코드를 실행하면 fieldPath = servey.serveyOne ... 으로 Path를 반환
                NumberPath<Integer> fieldPath = getFieldPath(servey, loopServeyList[i]);
                if(fieldPath != null) {
                    averages[i] = (double) Math.round(result.get(fieldPath.avg()) * 10) / 10.0;
                }
            }

            // 질문 전체의 totalAvg 계산
            Double totalAvg = calculateTotalAvg(averages);

            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("totalCnt", consultCnt);

            for (int i = 0; i < loopServeyList.length; i++) {
                resultMap.put(loopServeyList[i] + "Avg", averages[i]);
            }
            resultMap.put("totalAvg", totalAvg);

            consultMap.put(consultantName, resultMap);
        }

        // 반환된 Map을 sort하여 Response
        return new ServeyResponse("상담사 통계 데이터", sortMapByTotalAvg(consultMap));
    }

    @Override
    public ServeyResponse dailyStaticsServey() {
        // Response 데이터 구조
        Map<String, Map<String, Long>> dailyCount = new LinkedHashMap<>();
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                servey.createdDate,
                ConstantImpl.create("%Y-%m-%d %H")
        );

        // format_date 기준 및 platform 기준 group by
        // 위 조건으로 count 및 date, platform 도출
        List<Tuple> dailyList = jpaQueryFactory
                .select(
                        servey.id.count(),
                        formattedDate,
                        servey.platform
                )
                .from(servey)
                .groupBy(servey.platform, formattedDate)
                .fetch();

        // 도출된 List 반복문
        for (Tuple resultList : dailyList) {
            Long staticsCnt = resultList.get(servey.id.count());
            String searchDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String resultDate = resultList.get(formattedDate);
            Platform platform = resultList.get(servey.platform);

            // yyyy-MM-dd HH 날짜와 시간 분리
            String[] splitList = resultDate.split(" ");

            // searchDate > 전일 날짜, splitList[0] > DB에서 조회된 날짜가 동일할 경우
            // totalCnt 및 WRMS, ILSANG count
            if (splitList[0].equals(searchDate)) {
                if (!dailyCount.containsKey(splitList[1])) {
                    dailyCount.put(splitList[1], new LinkedHashMap<>());
                    dailyCount.get(splitList[1]).put(String.valueOf(platform), staticsCnt);
                } else {
                    dailyCount.get(splitList[1]).put(String.valueOf(platform), staticsCnt);
                }

                dailyCount.get(splitList[1]).put("totalCnt", dailyCount.get(splitList[1]).getOrDefault("totalCnt", 0L) + staticsCnt);
            }
        }

        return new ServeyResponse("전일 통계 데이터", dailyCount);
    }

    private NumberPath<Integer> getFieldPath(QServey servey, String fieldName) {
        try {
            Field field = servey.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (NumberPath<Integer>) field.get(servey);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * 점수 통계 평균 계산 후 MAP에 추가
     * scoreStaticsServey avg 계산
     * platform이 아닌, 전체에 대한 avg를 계산하기위한 메서드
     * @param scoreData
     */
    private static void calculateAverage(Map<String, Map<String, Object>> scoreData) {
        for (Map.Entry<String, Map<String, Object>> platformEntry : scoreData.entrySet()) {
            String platform = platformEntry.getKey();
            Map<String, Object> platformData = platformEntry.getValue();

            int totalScore = 0;
            int totalCount = 0;

            for (Map.Entry<String, Object> scoreEntry : platformData.entrySet()) {
                int score = Integer.parseInt(scoreEntry.getKey());
                int count = (int) scoreEntry.getValue();

                totalScore += score * count;
                totalCount += count;
            }

            double average = totalCount == 0 ? 0 : Math.round((double)totalScore / totalCount * 10) / 10.0;

            platformData.put("avg", average);
        }
    }

    /**
     * consultantStaticsServey의 totalAvg를 구하기위한 메서드
     * 위 메서드와는 로직이 다르다
     * @param averages
     * @return
     */
    private Double calculateTotalAvg(Double[] averages) {
        double sum = 0;
        int count = 0;

        for (Double avg : averages) {
            if (avg != null) {
                sum += avg;
                count++;
            }
        }
        return count == 0 ? 0 : Math.round(sum / count * 10) / 10.0;
    }

    /**
     * 상담원별 통계자료 정렬
     * 정렬순서 totalAvg 높은순, totalAvg가 같은경우 totalCnt 높은순 정렬, totalCnt까지 같을 경우
     * name기준 가나다순 정렬
     * @param consultMap
     * @return
     */
    private Map<String, Map<String, Object>> sortMapByTotalAvg(Map<String, Map<String, Object>> consultMap) {
        return consultMap.entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    Double totalAvg1 = (Double) e1.getValue().get("totalAvg");
                    Double totalAvg2 = (Double) e2.getValue().get("totalAvg");
                    int compareResult = totalAvg2.compareTo(totalAvg1);
                    if (compareResult == 0) {
                        Long totalCnt1 = (Long) e1.getValue().get("totalCnt");
                        Long totalCnt2 = (Long) e2.getValue().get("totalCnt");
                        compareResult = totalCnt2.compareTo(totalCnt1);
                        if (compareResult == 0) {
                            return e1.getKey().compareTo(e2.getKey());
                        }
                    }
                    return compareResult;
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}