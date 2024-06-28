package kr.co.mostx.japi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServeyResponse<T> {

    private String message;
    private T data;

    public static <T> ServeyResponse success(String message, T data) {
        return ServeyResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
