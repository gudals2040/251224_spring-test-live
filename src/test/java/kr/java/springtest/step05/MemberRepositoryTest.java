package kr.java.springtest.step05;

import kr.java.springtest.model.entity.Member;
import kr.java.springtest.model.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// 5-1
@DataJpaTest // 테스트하고 나서 롤백시켜줌
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// DataSource를 Test의 경우엔 알아서 H2 등으로 바꿔줌 -> 바꾸지 말라
@ActiveProfiles("test") // 명시적으로 db를 지정해줄 것 (application-test.yaml)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager em;

    // 5-2
    @Test
    @DisplayName("회원 저장 및 조회")
    void save_and_find() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .name("테스터")
                .build();
        // when
        Member saved = memberRepository.save(member);

        // 캐싱 초기화
        em.flush(); // 모두 반영시키고
        em.clear(); // 캐시를 밀어버림

        // then
        Member found = memberRepository.findById(saved.getId())
                .orElseThrow();

        assertThat(found.getEmail()).isEqualTo(member.getEmail());
        assertThat(found.getName()).isEqualTo(member.getName());
        assertThat(found.getPoint()).isEqualTo(0);
        assertThat(found.getCreatedAt()).isNotNull();
    }

    // 5-3
    @Test
    @DisplayName("이메일 중복 저장 시 예외 발생")
    void save_duplicateEmail_throwsException() {
        // given
        memberRepository.save(
                Member.builder()
                        .email("a@a.com")
                        .name("첫번째")
                        .build()
        );
        em.flush(); // INSERT

        // when
        Member d = Member.builder()
                        .email("a@a.com")
                        .name("두번째")
                        .build();

        // then
        assertThatThrownBy(() -> {
            memberRepository.save(d);
            em.flush(); // INSERT
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    // 5-4
    @Test
    @DisplayName("회원 정보 수정")
    void update() {
        // given
        Member member = memberRepository.save(
                Member.builder()
                        .email("a@a.com")
                        .name("수정전")
                        .point(1000)
                        .build()
        );
        em.flush(); // INSERT
        em.clear();

        // when
        Member found = memberRepository
                .findById(member.getId())
                .orElseThrow();
        found.changeName("수정후");
        found.addPoint(500);
        em.flush(); // UPDATE
        em.clear();

        // then
        Member updated = memberRepository.
                findById(member.getId())
                .orElseThrow();

        assertThat(updated.getName()).isEqualTo(found.getName());
        assertThat(updated.getPoint()).isEqualTo(1500);
    }
}
