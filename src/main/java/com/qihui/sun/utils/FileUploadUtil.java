package com.qihui.sun.utils;

import cn.hutool.crypto.digest.DigestUtil;
import com.qihui.sun.domain.FileType;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Objects;

public class FileUploadUtil {
    private static final String path = "/opt/qihuis/wish_tree/upload";
    public static final String serverUrl = "https://8.155.9.156/upload";
//    public static final String serverUrl = "https://192.168.10.128/upload";

    public static Path getBaseDir() {
        LocalDate now = LocalDate.now();
        return Paths.get(path)
                .resolve(now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth());
    }

    public static Path getChunkDir(String fileId) {
        return getBaseDir().resolve("chunks").resolve(fileId);
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
        /*if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex);*/
    }

    public static void checkFileExtensionAndType(MultipartFile file) {
        String upperCase = Objects.requireNonNull(file.getOriginalFilename()).toUpperCase();
        checkFileExtensionAndType(upperCase, file, null);
    }

    public static void checkFileExtensionAndType(String filename, Path filePath) {
        checkFileExtensionAndType(filename, null, filePath);
    }

    public static void checkFileExtensionAndType(String filename, MultipartFile file, Path filePath) {
        String fileExtension = getFileExtension(filename);
        Tika tika = new Tika();
        try {
            String mimeType;
            if (file != null) {
                mimeType = tika.detect(file.getInputStream());
            } else if (filePath != null) {
                mimeType = tika.detect(filePath);
            } else {
                throw new RuntimeException("文件类型不匹配");
            }
            FileType fileType = FileType.valueOf(fileExtension);
            if (!fileType.getContentType().equals(mimeType)) {
                throw new RuntimeException("文件类型不匹配");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("文件类型不匹配");
        }
    }

    public static String SHA256(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return DigestUtil.sha256Hex(bytes);
    }

    public static String SHA256(File file) {
        return DigestUtil.sha256Hex(file);
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
        Files.createDirectories(filePath);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void deleteFile(String filePath) throws IOException {
        Files.deleteIfExists(Path.of(filePath));
    }
}