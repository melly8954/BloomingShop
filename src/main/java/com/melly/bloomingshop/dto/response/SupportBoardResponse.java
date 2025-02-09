package com.melly.bloomingshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SupportBoardResponse<T> {
    private List<T> supportBoards;  // 상품 목록
    private long totalElements;  // 전체 상품 수
    private int totalPages;  // 전체 페이지 수
    private int currentPage;  // 현재 페이지
    private int pageSize;  // 페이지 크기

}
