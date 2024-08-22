package com.study.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching // 레디스 캐싱 하기위해 추가, @Cacheable 같은 어노테이션을 인식 하게 함
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.study.board.repository")
@EntityScan(basePackages = "com.study.board.entity")
@EnableJpaAuditing // BaseEntity클래스의 AuditingEntityListener를 활성화 시키기 위해 추가한다
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
