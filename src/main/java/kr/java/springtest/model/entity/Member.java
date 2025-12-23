package kr.java.springtest.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 프레임워크(JPA 등)를 위해 기본 생성자는 열어두되, 일반 코드에서의 무분별한 생성을 막겠다
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    // 포인트 (기본값 0)
    @Column(nullable = false)
    private Integer point = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Member(String email, String name, Integer point) {
        this.email = email;
        this.name = name;
        this.point = point != null ? point : 0;
        this.createdAt = LocalDateTime.now();
    }

    // 이름 변경
    public void changeName(String name) {
        this.name = name;
    }

    // 포인트 충전
    public void addPoint(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("충전 금액은 0 이상이어야 합니다");
        }
        this.point += amount;
    }

    // 포인트 사용
    public void usePoint(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("사용 금액은 0 이상이어야 합니다");
        }
        if (this.point < amount) {
            throw new IllegalStateException("포인트가 부족합니다");
        }
        this.point -= amount;
    }
}