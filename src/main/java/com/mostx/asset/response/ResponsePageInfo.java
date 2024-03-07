package com.mostx.asset.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResponsePageInfo<T> {
    private List<T> contents;
    private Long totalCnt;
    private Long totalPage;
}
