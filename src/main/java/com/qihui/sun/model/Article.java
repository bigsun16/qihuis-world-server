package com.qihui.sun.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bigsu
 */
@Data
public class Article {
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String categoryKey;;
    private Integer id;
}
