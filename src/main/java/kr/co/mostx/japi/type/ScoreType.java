package kr.co.mostx.japi.type;

import com.querydsl.core.types.dsl.BooleanExpression;

import static kr.co.mostx.japi.entity.QServey.servey;

public enum ScoreType {
    avgScore(servey.avgScore::between),
    totalScore(servey.totalScore::between);

    private final TwoFunction<Integer, Integer, BooleanExpression> expression;

    ScoreType(TwoFunction<Integer, Integer, BooleanExpression> expression) {
        this.expression = expression;
    }

    public BooleanExpression getScore(Integer minScore, Integer maxScore) {
        // minScore 나 maxScore가 입력되지 않았을 경우 해당 값의 최소, 최댓값 세팅

        // minScore의 경우 totalScore의 MIN 값은 5이지만, avgScore의 MIN 값은 1이므로 최소 1을 세팅
        if (minScore == 0) {
            minScore = 1;
        }

        // maxScore의 경우 avgScore의 MAX값은 5이지만, totalScore의 MAX값은 25이므로 최대 25를 세팅
        if (maxScore == 0) {
            maxScore = 25;
        }

        return expression.apply(minScore, maxScore);
    }
}

@FunctionalInterface
interface TwoFunction<T, U, R>{
    R apply(T t, U u);
}
