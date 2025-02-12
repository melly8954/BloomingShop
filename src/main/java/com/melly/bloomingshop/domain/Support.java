package com.melly.bloomingshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "support_board_tbl")
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name = "view_qty")
    private int viewQty = 0;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "is_secret")
    private Boolean isSecret;

    @Column
    private String password;

    @Column(name = "is_answer")
    private Boolean isAnswer;

    @Column(name = "answer_content")
    private String answerContent;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Column(name = "answer_created_date")
    private LocalDateTime answerCreatedDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    public void changeIsAnswer(Boolean isAnswer) {
        this.isAnswer = isAnswer;
    }

    public void changeAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public void changeAnswerDate(LocalDateTime answerCreatedDate) {
        this.answerCreatedDate = answerCreatedDate;
    }

    public void changeDeletedFlag(boolean b) {
        this.deletedFlag = b;
    }

    public void changeDeleted_date(LocalDateTime now) {
        this.deletedDate = now;
    }
}
