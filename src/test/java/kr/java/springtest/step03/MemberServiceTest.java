package kr.java.springtest.step03;

import kr.java.springtest.model.dto.MemberRequest;
import kr.java.springtest.model.dto.MemberResponse;
import kr.java.springtest.model.entity.Member;
import kr.java.springtest.model.repository.MemberRepository;
import kr.java.springtest.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

// 3-1
//@SpringBootTest // <- 전체를 로딩하기 때문에 단위테스트할 땐 적절하지 X.
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    // Service -> Repository
    @Mock // 객체가 있는 척 (초기화, 주입된 척) 하는데 실제 데이터 X.
    private MemberRepository memberRepository;

    @InjectMocks // MemberService <- MemberRepository
    private MemberService memberService;

    // 테스트 샘플 데이터
    private Member sampleMember; // entity
    private MemberRequest sampleRequest; // DTO

    // 3-2
    // 샘플데이터에 대한 반복 처리 (테스트)
    @BeforeEach
    void setUp() {
        sampleMember = Member.builder()
                .email("test@example.com")
                .name("테스터")
                .point(1000)
                .build();
        // ID -> 직접 설정 X. (JPA나 DB 알아서 만들어주던 것)
        ReflectionTestUtils.setField(sampleMember, "id", 1L);

        sampleRequest = MemberRequest.builder()
                .email("test@example.com")
                .name("테스터")
                .build();
    }

    // 3-3
    // 회원가입 테스트
    @Test
    @DisplayName("회원 가입 성공")
    void register_success() {
        // given
        // import static org.mockito.BDDMockito.given;
        given(memberRepository.existsByEmail(anyString()))
                .willReturn(false);
        // import static org.mockito.ArgumentMatchers.any;
        given(memberRepository.save(any(Member.class)))
                .willReturn(sampleMember);
        // when
        MemberResponse response = memberService.register(sampleRequest);

        // then
        // import static org.assertj.core.api.Assertions.assertThat;
        assertThat(response).isNotNull(); // 응답이 있는지
        assertThat(response.getEmail()).isEqualTo(sampleMember.getEmail());
        assertThat(response.getName()).isEqualTo(sampleMember.getName());

        // verfiy : 메서드 호출 여부
        then(memberRepository).should().existsByEmail(sampleMember.getEmail());
        then(memberRepository).should().save(any(Member.class));
    }

    // 3-4
    @Test
    @DisplayName("이메일 중복 시 예외 발생")
    void register_duplicateEmail_throwsException() {
        // given
        given(memberRepository.existsByEmail(anyString()))
                .willReturn(true); // 실패 유도 (exception)

        // when & then
        assertThatThrownBy(() -> memberService.register(sampleRequest))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("이미 사용 중인 이메일");

        // verfiy : 메서드 호출 여부
        then(memberRepository).should().existsByEmail(sampleMember.getEmail());
        then(memberRepository).should(never()).save(any(Member.class)); // save가 호출되면 안되는 상황
    }

    // Mockito 고급 기능 (...)
}
