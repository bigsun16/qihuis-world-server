package com.qihui.sun.service;

import com.qihui.sun.domain.FileEntity;
import com.qihui.sun.domain.FileUploadRes;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    void handleChunkUpload(MultipartFile file, int chunk_index, String fileId, String sha256, int totalChunks);

    FileUploadRes mergeChunksAndVerifyHash(String fileId, int totalChunks);

    FileUploadRes initFileUploadInfo(FileEntity fileEntity);

    FileUploadRes uploadFile(MultipartFile file, String sha256, String alt);
}
