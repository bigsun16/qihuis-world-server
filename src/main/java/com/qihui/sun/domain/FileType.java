package com.qihui.sun.domain;

import lombok.Getter;

@Getter
public enum FileType {
    PICTURE("image/jpeg", "image/jpeg", "image/jpeg"),
    VIDEO("video/mp4", "video/quicktime", "video/x-msvideo,video/avi,video/x-flv");

    private final String[] contentType;

    FileType(String... contentType) {
        this.contentType = contentType;
    }

}
