package kr.java.springtest.model.repository;

import kr.java.springtest.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 회원 조회
    Optional<Member> findByEmail(String email);

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 특정 포인트 이상인 회원 조회
    List<Member> findByPointGreaterThanEqual(Integer point);
}
