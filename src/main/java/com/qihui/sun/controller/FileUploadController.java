package com.qihui.sun.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.qihui.sun.controller.response.ApiResponse;
import com.qihui.sun.domain.FileEntity;
import com.qihui.sun.domain.FileUploadRes;
import com.qihui.sun.service.FileUploadService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@SaCheckLogin
public class FileUploadController {

    private final FileUploadService fileService;

    public FileUploadController(FileUploadService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/initUploadFileInfo")
    public ApiResponse<FileUploadRes> initUploadFileInfo(@RequestBody FileEntity fileEntity) {
        FileUploadRes fileUploadRes = fileService.initFileUploadInfo(fileEntity);
        return ApiResponse.success(fileUploadRes);
    }

    @GetMapping("/mergeChunks")
    public ApiResponse<FileUploadRes> mergeChunks(@RequestParam("fileId") String fileId, @RequestParam("totalChunks") int totalChunks) {
        FileUploadRes fileUploadRes = fileService.mergeChunksAndVerifyHash(fileId, totalChunks);
        return ApiResponse.success(fileUploadRes);
    }

    @PutMapping("/chunkUpload")
    public ApiResponse<Boolean> uploadChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("chunk_index") int chunk_index,
            @RequestParam("fileId") String fileId,
            @RequestParam("sha256") String sha256,
            @RequestParam("totalChunks") int totalChunks) {

        fileService.handleChunkUpload(file, chunk_index, fileId, sha256, totalChunks);
        return ApiResponse.success(true);
    }

    @PutMapping("/upload")
    public ApiResponse<FileUploadRes> uploadFile(
            @RequestParam("file") @NonNull MultipartFile file
            , @RequestParam("sha256") String sha256
            , @RequestParam("alt") String alt) {
        FileUploadRes fileUploadRes = fileService.uploadFile(file, sha256, alt);
        return ApiResponse.success(fileUploadRes);
    }
}
