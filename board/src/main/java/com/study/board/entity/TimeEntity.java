package com.study.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass // 테이블로 생성되지 않도록 해주는 어노테이션
@Getter
@EntityListeners(AuditingEntityListener.class)
abstract class TimeEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private String updatedAt;

    /* 해당 엔티티를 저장하기 이전에 실행 */
    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.updatedAt = this.createdAt;
    }

    /* 해당 엔티티를 업데이트 하기 이전에 실행*/
    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

}
