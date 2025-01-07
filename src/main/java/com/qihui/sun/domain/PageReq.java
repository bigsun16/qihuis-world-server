package com.qihui.sun.domain;

import lombok.Data;

@Data
public class PageReq {
    private SelectType selectType;
    private String paramValue;
    private int current;
    private int size;

    public enum SelectType {
        ALL,
        MINE,
        OTHERS
    }
}
