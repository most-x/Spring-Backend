package common;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static kr.co.mostx.japi.entity.QServey.servey;

public class common {
    // startDate, endDate 를 받아서 처리하는 메서드
    // QueryDsl용 BooleanExpression 반환
    public static BooleanExpression getBooleanExpression(LocalDate startDate, LocalDate endDate) {
        BooleanExpression startGoeDate = startDate != null ? servey.createdDate.goe(LocalDateTime.of(startDate, LocalTime.MIN)) : null;
        BooleanExpression endLoeDate = endDate != null ? servey.createdDate.loe(LocalDateTime.of(endDate, LocalTime.MAX)) : null;

        return Expressions.allOf(startGoeDate, endLoeDate);
    }
}
