package kr.java.springtest.model.dto;

import kr.java.springtest.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    private Long id;
    private String email;
    private String name;
    private Integer point;
    private LocalDateTime createdAt;

    // Entity → DTO 변환
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .point(member.getPoint())
                .createdAt(member.getCreatedAt())
                .build();
    }
}