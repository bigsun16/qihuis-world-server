package com.qihui.sun.domain;

import lombok.Getter;

@Getter
public enum FileType {
    JPG("image/jpeg"),
    PNG("image/png"),
    JPEG("image/jpeg"),
    MP4("video/mp4");

    private final String contentType;

    FileType(String contentType) {
        this.contentType = contentType;
    }

}
