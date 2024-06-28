package kr.co.mostx.japi.type;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.function.Function;

import static kr.co.mostx.japi.entity.QServey.servey;

public enum SearchType {
    serveyNumber(servey.serveyNumber::contains),
    userName(servey.userName::contains),
    userPhone(servey.userPhone::contains),
    consultantName(servey.consultantName::contains);

    private final Function<String, BooleanExpression> expression;

    SearchType(Function<String, BooleanExpression> expression) {
        this.expression = expression;
    }

    public BooleanExpression getEq(String keyWord) {
        return expression.apply(keyWord);
    }
}
