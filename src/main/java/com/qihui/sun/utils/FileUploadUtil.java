package com.qihui.sun.utils;

import cn.hutool.crypto.digest.DigestUtil;
import com.qihui.sun.domain.FileType;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class FileUploadUtil {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(FileUploadUtil.class);

    private static String path;
    public static String serverUrl;
    private static final Tika tika = new Tika();

    @Value("${wish_tree.file.upload.path}")
    public void setPath(String path) {
        logger.info("文件上传路径：{}", path);
        FileUploadUtil.path = path;
    }

    @Value("${wish_tree.file.upload.serverUrl}")
    public void setServerUrl(String serverUrl) {
        logger.info("文件上传服务器地址：{}", serverUrl);
        FileUploadUtil.serverUrl = serverUrl;
    }

    public static String getFileUrl(String timePath, String fileName) {
        logger.info("timePath:{}, fileName:{}", timePath, fileName);
        String path = Paths.get(timePath, fileName).toString();
        String fileUrl = serverUrl + "/" + path;
        logger.info("fileUrl:{}", fileUrl);
        return fileUrl;
    }

    public static String getTimePath() {
        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        String month = String.valueOf(now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());
        return Paths.get(year, month, day).toString();
    }

    public static Path getBaseDir(String timePath) {
        logger.info("path:{},timePath:{}", path, timePath);
        return Paths.get(path, timePath);
    }

    public static Path getChunkDir(String fileId) {
        return Paths.get(path, "chunks", fileId);
    }

    public static Path getTempDir(String fileId) {
        return getChunkDir(fileId).resolve("temp");
    }

    public static boolean isSafeFilePath(String path) {
        // 规范化路径
        String normalizedPath = FilenameUtils.normalize(path, true);
        // 检查路径遍历字符
        return !(normalizedPath == null || normalizedPath.contains("../") || normalizedPath.contains("..\\") ||
                normalizedPath.startsWith("/") || normalizedPath.startsWith("\\"));
    }

    public static String getFileExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static void checkFileExtensionAndType(MultipartFile file) {
        checkFileExtensionAndType(file, null);
    }

    public static void checkFileExtensionAndType(Path filePath) {
        checkFileExtensionAndType(null, filePath);
    }

    public static void checkFileExtensionAndType(MultipartFile file, Path filePath) {
        try {
            String mimeType;
            if (file != null) {
                mimeType = tika.detect(file.getInputStream());
            } else if (filePath != null) {
                mimeType = tika.detect(filePath);
            } else {
                throw new RuntimeException("文件不存在");
            }
            String[] picContentType = FileType.PICTURE.getContentType();
            String[] videoContentType = FileType.VIDEO.getContentType();
            boolean b1 = Arrays.stream(picContentType).anyMatch(mimeType::equalsIgnoreCase);
            boolean b2 = Arrays.stream(videoContentType).anyMatch(mimeType::equalsIgnoreCase);
            if (!b1 && !b2) {
                throw new RuntimeException("文件类型不匹配");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("文件类型不匹配");
        }
    }

    public static String SHA256(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return DigestUtil.sha256Hex(inputStream);
        }
    }

    public static String SHA256(File file) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            return DigestUtil.sha256Hex(inputStream);
        }
    }

    public static void checkFileHashIsSame(String fileHash, MultipartFile file) {
        try {
            if (!fileHash.equals(SHA256(file))) {
                throw new RuntimeException("文件hash不一致");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void checkTempFileHashIsSame(String fileHash, Path tempFile) throws IOException {
        if (!fileHash.equals(SHA256(tempFile.toFile()))) {
            Files.delete(tempFile);
            throw new RuntimeException("文件hash不一致");
        }
    }


    public static void createAndWriteFile(MultipartFile file, Path filePath) throws IOException {
        Files.createDirectories(filePath.getParent());
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

    }

}