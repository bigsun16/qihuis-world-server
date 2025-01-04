package com.qihui.sun.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName files
 */
@TableName(value = "files")
@Data
@Builder
public class FileEntity implements Serializable {
    @TableId
    private String id;
    @NonNull
    private String filename;
    @NonNull
    private Integer totalChunks = 0;
    @NonNull
    private String sha256;
    @NonNull
    private Long size;
    @NonNull
    private String type;
    private String alt;
    private String url;
    private String realPath;
    private FileUploadStatusEnum status;
    private LocalDateTime create_time;
    private LocalDateTime update_time;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FileEntity other = (FileEntity) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && this.getFilename().equals(other.getFilename())
                && this.getSha256().equals(other.getSha256())
                && this.getSize().equals(other.getSize())
                && this.getType().equals(other.getType())
                && this.getRealPath().equals(other.getRealPath())
                && this.getTotalChunks().equals(other.getTotalChunks())
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
                && (this.getAlt() == null ? other.getAlt() == null : this.getAlt().equals(other.getAlt()))
                && (this.getCreate_time() == null ? other.getCreate_time() == null : this.getCreate_time().equals(other.getCreate_time()))
                && (this.getUpdate_time() == null ? other.getUpdate_time() == null : this.getUpdate_time().equals(other.getUpdate_time()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + getFilename().hashCode();
        result = prime * result + getSha256().hashCode();
        result = prime * result + getSize().hashCode();
        result = prime * result + getType().hashCode();
        result = prime * result + getRealPath().hashCode();
        result = prime * result + getTotalChunks().hashCode();
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreate_time() == null) ? 0 : getCreate_time().hashCode());
        result = prime * result + ((getUpdate_time() == null) ? 0 : getUpdate_time().hashCode());
        result = prime * result + ((getAlt() == null) ? 0 : getAlt().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", filename=" + filename +
                ", sha256=" + sha256 +
                ", size=" + size +
                ", status=" + status +
                ", type=" + type +
                ", alt=" + alt +
                ", url=" + url +
                ", realPath=" + realPath +
                ", totalChunks=" + totalChunks +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}