package kr.co.mostx.japi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AssetResponsePageInfo<T> {
    private List<T> contents;
    private Long totalCnt;
    private Long totalPage;
}
