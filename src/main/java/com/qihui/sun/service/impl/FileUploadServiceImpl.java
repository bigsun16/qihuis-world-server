package com.qihui.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qihui.sun.config.WishTreeThreadPool;
import com.qihui.sun.domain.*;
import com.qihui.sun.mapper.ChunksMapper;
import com.qihui.sun.service.ArticleService;
import com.qihui.sun.service.ChunkEntityService;
import com.qihui.sun.service.FileEntityService;
import com.qihui.sun.service.FileUploadService;
import com.qihui.sun.utils.FileUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
    private final FileEntityService fileEntityService;
    private final ChunkEntityService chunkEntityService;
    private final ChunksMapper chunksMapper;
    private final ArticleService articleService;

    public FileUploadServiceImpl(FileEntityService fileEntityService, ChunkEntityService chunkEntityService, ChunksMapper chunksMapper, ArticleService articleService) {
        this.fileEntityService = fileEntityService;
        this.chunkEntityService = chunkEntityService;
        this.chunksMapper = chunksMapper;
        this.articleService = articleService;
    }

    @Override
    public FileUploadRes uploadFile(MultipartFile file, String sha256, String alt) {
        FileUploadUtil.checkFileHashIsSame(sha256, file);
        FileUploadUtil.checkFileExtensionAndType(file);
        FileEntity one = fileEntityService.query()
                .eq("sha256", sha256)
                .one();
        if (one != null) {
            if (FileUploadStatusEnum.SUCCESS.equals(one.getStatus())) {
                logger.info("File already exists.");
                return buildFileUploadRes(one);
            }
            if (FileUploadStatusEnum.UPLOADING.equals(one.getStatus())) {
                throw new RuntimeException("File is already uploading.");
            }
            if (FileUploadStatusEnum.FAILURE.equals(one.getStatus())) {
                logger.info("File upload failed, retrying...");
                return tryUploadFile(file, one);
            }
        }
        FileEntity fileEntity = initFileAndGet(file, sha256, alt);
        logger.info("File does not exist, creating new record...");
        logger.info(fileEntity.toString());
        return tryUploadFile(file, fileEntity);
    }

    private FileEntity initFileAndGet(MultipartFile file, String sha256, String alt) {
        String fileId = UUID.randomUUID().toString();
        String filename = fileId + "." + FileUploadUtil.getFileExtension(file.getOriginalFilename());
        String timePath = FileUploadUtil.getTimePath();
        String fileRealPath = FileUploadUtil.getBaseDir(timePath).resolve(filename).toString();
        FileEntity fileEntity = FileEntity.builder()
                .status(FileUploadStatusEnum.UPLOADING)
                .id(fileId)
                .filename(filename)
                .realPath(fileRealPath)
                .url(FileUploadUtil.getFileUrl(timePath, filename))
                .sha256(sha256)
                .size(file.getSize())
                .type(Objects.requireNonNull(file.getContentType()))
                .create_time(LocalDateTime.now())
                .update_time(LocalDateTime.now())
                .totalChunks(0)
                .alt(alt)
                .build();
        fileEntityService.save(fileEntity);
        return fileEntity;
    }

    private FileUploadRes tryUploadFile(MultipartFile file, FileEntity fileEntity) {
        try {
            FileUploadUtil.createAndWriteFile(file, Path.of(fileEntity.getRealPath()));
            updateFileUploadStatus(fileEntity, FileUploadStatusEnum.SUCCESS);
            return buildFileUploadRes(fileEntity);
        } catch (IOException e) {
            logger.error("Failed to upload file: ", e);
            updateFileUploadStatus(fileEntity, FileUploadStatusEnum.FAILURE);
            throw new RuntimeException("Failed to upload file.");
        }
    }

    private FileUploadRes buildFileUploadRes(FileEntity fileEntity) {
        return FileUploadRes.builder()
                .url(fileEntity.getUrl())
                .alt(fileEntity.getAlt())
                .href(fileEntity.getUrl())
                .build();
    }

    private void updateFileUploadStatus(FileEntity fileEntity, FileUploadStatusEnum status) {
        fileEntity.setStatus(status);
        fileEntity.setUpdate_time(LocalDateTime.now());
        fileEntityService.updateById(fileEntity);
    }

    public FileUploadRes initFileUploadInfo(FileEntity fileEntity) {
        FileEntity one = fileEntityService.query()
                .eq("sha256", fileEntity.getSha256())
                .one();
        if (one != null) {
            if (FileUploadStatusEnum.SUCCESS.equals(one.getStatus())) {
                return buildFileUploadRes(one);
            } else if (FileUploadStatusEnum.UPLOADING.equals(one.getStatus())) {
                throw new RuntimeException("File is already uploading.");
            } else {
                return FileUploadRes.builder()
                        .fileId(one.getId())
                        .build();
            }
        }
        return initFileAndChunkInfoAndReturnFileId(fileEntity);
    }

    private FileUploadRes initFileAndChunkInfoAndReturnFileId(FileEntity fileEntity) {
        String fileId = UUID.randomUUID().toString();
        String filename = fileId + "." + FileUploadUtil.getFileExtension(fileEntity.getFilename());
        String timePath = FileUploadUtil.getTimePath();
        String filePath = FileUploadUtil.getBaseDir(timePath).resolve(filename).toString();
        fileEntity.setStatus(FileUploadStatusEnum.UPLOADING);
        fileEntity.setId(fileId);
        fileEntity.setFilename(filename);
        fileEntity.setRealPath(filePath);
        fileEntity.setUrl(FileUploadUtil.getFileUrl(timePath, filename));
        fileEntity.setCreate_time(LocalDateTime.now());
        fileEntity.setUpdate_time(LocalDateTime.now());
        fileEntityService.save(fileEntity);
        int totalChunks = fileEntity.getTotalChunks();
        List<ChunkEntity> missingChunks = new ArrayList<>(totalChunks);
        // 创建初始的分片记录
        for (int i = 0; i < totalChunks; i++) {
            ChunkEntity missing = ChunkEntity.builder()
                    .file_id(fileId)
                    .chunk_index(i)
                    .status(FileUploadStatusEnum.MISSING)
                    .create_time(LocalDateTime.now())
                    .update_time(LocalDateTime.now())
                    .build();
            missingChunks.add(missing);
        }
        chunkEntityService.saveBatch(missingChunks);
        return FileUploadRes
                .builder()
                .fileId(fileId)
                .build();
    }

    @Override
    public void handleChunkUpload(MultipartFile file, int chunk_index, String fileId, String sha256, int totalChunks) {
        FileUploadUtil.checkFileHashIsSame(sha256, file);
        // 获取当前文件记录
        ChunkEntity one = chunkEntityService.query()
                .eq("file_id", fileId)
                .eq("chunk_index", chunk_index)
                .one();
        if (sha256.equals(one.getSha256())) {
            if (FileUploadStatusEnum.SUCCESS.equals(one.getStatus()) || FileUploadStatusEnum.UPLOADING.equals(one.getStatus())) {
                return;
            } else if (FileUploadStatusEnum.FAILURE.equals(one.getStatus())) {
                tryUploadChunkFile(file, chunk_index, fileId, one);
            }
        }
        // 更新分片状态并保存分片到临时位置
        one.setSha256(sha256);
        one.setSize(file.getSize());
        one.setStatus(FileUploadStatusEnum.UPLOADING);
        one.setUpdate_time(LocalDateTime.now());
        chunkEntityService.updateById(one);
        tryUploadChunkFile(file, chunk_index, fileId, one);
    }

    private void tryUploadChunkFile(MultipartFile file, int chunk_index, String fileId, ChunkEntity one) {
        Path filePath = FileUploadUtil.getChunkDir(fileId).resolve(chunk_index + ".part");
        one.setUpdate_time(LocalDateTime.now());
        try {
            FileUploadUtil.createAndWriteFile(file, filePath);
            updateChunkFileUploadStatus(one, FileUploadStatusEnum.SUCCESS);
        } catch (IOException e) {
            logger.error("Failed to upload ChunkFile: ", e);
            updateChunkFileUploadStatus(one, FileUploadStatusEnum.FAILURE);
            throw new RuntimeException("Failed to upload file.");
        }
    }

    private void updateChunkFileUploadStatus(ChunkEntity chunkEntity, FileUploadStatusEnum status) {
        chunkEntity.setStatus(status);
        chunkEntity.setUpdate_time(LocalDateTime.now());
        chunkEntityService.updateById(chunkEntity);
    }

    @Override
    public FileUploadRes mergeChunksAndVerifyHash(String fileId, int totalChunks) {
        FileEntity fileEntity = checkReadyToMerge(fileId, totalChunks);
        Path tempFilePath = null;
        try {
            tempFilePath = Files.createTempFile(FileUploadUtil.getTempDir(fileId), "merged_", ".file");
            try (OutputStream outputStream = Files.newOutputStream(tempFilePath)) {
                for (int i = 0; i < totalChunks; i++) {
                    Path chunkPath = FileUploadUtil.getChunkDir(fileId).resolve(i + ".part");
                    Files.copy(chunkPath, outputStream);
                    Files.deleteIfExists(chunkPath);
                }
                FileUploadUtil.checkTempFileHashIsSame(fileEntity.getSha256(), tempFilePath);
                FileUploadUtil.checkFileExtensionAndType(tempFilePath);
                Files.move(tempFilePath, Path.of(fileEntity.getRealPath()), StandardCopyOption.ATOMIC_MOVE);
                // 更新文件状态为完成
                updateFileUploadStatus(fileEntity, FileUploadStatusEnum.SUCCESS);
                return buildFileUploadRes(fileEntity);
            }
        } catch (IOException e) {
            assert tempFilePath != null;
            if (Files.exists(tempFilePath)) {
                try {
                    Files.deleteIfExists(tempFilePath);
                } catch (IOException ex) {
                    throw new RuntimeException("Failed to delete temporary file.");
                }
            }
            updateFileUploadStatus(fileEntity, FileUploadStatusEnum.FAILURE);
            throw new RuntimeException("Failed to merge chunks.");
        } catch (RuntimeException e) {
            updateFileUploadStatus(fileEntity, FileUploadStatusEnum.FAILURE);
            throw e;
        } finally {
            if (tempFilePath != null && Files.exists(tempFilePath)) {
                try {
                    Files.deleteIfExists(tempFilePath);
                } catch (IOException e) {
                    logger.error("Failed to delete temporary file.", e);
                }
            }
        }
    }

    private FileEntity checkReadyToMerge(String fileId, int totalChunks) {
        FileEntity one = fileEntityService.getById(fileId);
        Map<String, Long> stringLongMap = chunksMapper.countAndSumSizeByFileId(fileId);
        if (stringLongMap.get("totalCount") == totalChunks && Objects.equals(stringLongMap.get("totalSize"), one.getSize())) {
            return one;
        }
        throw new RuntimeException("File size mismatch.");
    }

    // 定时清理无关联的文章的图片
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanNoLinkedFiles() {
        fileEntityService.query()
                .select("url")
                .list()
                .forEach(fileEntity -> WishTreeThreadPool.getExecutor().execute(() -> {
                    boolean exists = articleService.exists(new QueryWrapper<Article>().like("content", fileEntity.getUrl()));
                    if (!exists) {
                        try {
                            Files.deleteIfExists(Path.of(fileEntity.getRealPath()));
                        } catch (IOException e) {
                            logger.error("Failed to delete file: {}", fileEntity.getFilename());
                        }
                    }
                }));
    }
}
