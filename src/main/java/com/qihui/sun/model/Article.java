package com.qihui.sun.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author bigsu
 */
@Data
public class Article {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private LocalDate createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private LocalDate updateTime;

    @TableField("category_key")
    private String categoryKey;

    @TableField("delete_flag")
    private Integer deleteFlag;

    @TableField("preview_text")
    private String previewText;
}
