package com.qihui.sun.domain;

import lombok.Data;

@Data
public class PageReq {
    private String paramValue;
    private int current;
    private int size;
}
