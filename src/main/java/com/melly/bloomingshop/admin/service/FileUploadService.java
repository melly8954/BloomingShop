package com.melly.bloomingshop.admin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload-dir:C:/bloomingshop}")
    private String uploadDir;

    public String saveImage(MultipartFile image) throws IOException {
        // 디렉토리가 없으면 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 고유한 파일 이름 생성
        String originalFilename = image.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename); // 확장자 추출
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // 파일 저장 경로 생성
        Path filePath = uploadPath.resolve(uniqueFileName);

        // 파일 저장
        Files.copy(image.getInputStream(), filePath);

        // **정적 자원 매핑된 경로 반환**
        return "/images/" + uniqueFileName;
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ""; // 확장자가 없는 경우 빈 문자열 반환
    }
}