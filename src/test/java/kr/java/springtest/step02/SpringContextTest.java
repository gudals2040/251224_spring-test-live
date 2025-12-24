package kr.java.springtest.step02;

// 2-2
import kr.java.springtest.controller.MemberController;
import kr.java.springtest.model.repository.MemberRepository;
import kr.java.springtest.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

// 2-2
/**
 * Spring Context 로딩 테스트
 *
 * @SpringBootTest: 전체 애플리케이션 컨텍스트를 로딩
 * 실제 Bean들이 정상적으로 생성되는지 확인
 */
@SpringBootTest // Spring Container에 들어있는 Component를 사용해주기 위해서
@ActiveProfiles("test") // test 설정으로 만들어놓고...
class SpringContextTest {

    // Spring이 자동으로 주입 (DI)
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberController memberController;

    @Test
    @DisplayName("Spring Context가 정상적으로 로딩된다")
    void contextLoads() {
        // 이 테스트가 통과하면 Spring 설정에 문제가 없음
        // 아무 검증 없이 로딩만 확인
    }

    @Test
    @DisplayName("MemberService Bean이 정상적으로 주입된다")
    void memberService_isNotNull() {
        // given & when: Spring이 자동으로 Bean 주입

        // then: null이 아니면 Bean 생성 성공
        // import static org.assertj.core.api.Assertions.assertThat;
        assertThat(memberService).isNotNull();
    }

    @Test
    @DisplayName("MemberRepository Bean이 정상적으로 주입된다")
    void memberRepository_isNotNull() {
        assertThat(memberRepository).isNotNull();
    }

    @Test
    @DisplayName("MemberController Bean이 정상적으로 주입된다")
    void memberController_isNotNull() {
        assertThat(memberController).isNotNull();
    }
}
