package com.melly.bloomingshop.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

    public String uploadImage(MultipartFile imageFile) throws IOException {
        // C드라이브에 bloomshop 폴더 저장 경로 설정
        String uploadDir = "C:/bloomshop/";
        File directory = new File(uploadDir);

        // 폴더가 없다면 생성
        if (!directory.exists()) {
            directory.mkdirs();  // C:/bloomshop/ 폴더 생성
        }

        // 고유 파일명 생성
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        // 파일 저장
        File destination = new File(uploadDir + fileName);
        imageFile.transferTo(destination);

        // 반환할 URL 생성 (예시: 로컬 파일 경로)
        return "/bloomshop/" + fileName;  // 저장된 파일 경로를 반환
    }
}
