package com.qihui.sun.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileUploadRes {
    private String url;
    private String alt;
    private String href;
    private String fileId;
}
