package com.melly.bloomingshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SupportBoardRegister {
    private String title;
    private String content;
    private String imageUrl; // 첨부파일이 있을 경우 저장
    private String authorName;
    private Boolean isSecret;
    private String password; // 비밀글인 경우 비밀번호 저장
}
