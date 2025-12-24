package kr.java.springtest.step06;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import kr.java.springtest.model.dto.MemberRequest;
import kr.java.springtest.model.dto.MemberResponse;
import kr.java.springtest.model.entity.Member;
import kr.java.springtest.model.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 6-1
// REST API 통합 테스트
@SpringBootTest // 실제 스프링 컨텍스트
@AutoConfigureMockMvc // REST API 호출
@ActiveProfiles("test")
@Transactional // 자동으로 한 번 테스트 후에 원상태로 롤백
public class MemberIntegrationTest {

    // 6-2
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository; // DB에 들어간 것 확인

    @Autowired
    // @SpringBootTest <- Entity 매니저를 직접 사용
    private EntityManager em;

    // 회원 가입
    // 예외 상황 통합 테스트
    // 트랜잭션 롤백 확인

    // 6-3
    @Test
    @DisplayName("회원가입 -> 조회 전체 흐름 테스트")
    void fullFlow_register_and_find() throws Exception {
        // Step1 : 회원가입
        MemberRequest request = MemberRequest.builder()
                .email("i@i.i")
//                .email("iii") // 400 에러
                .name("김통합")
                .build();
        String responseBody = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        em.flush();
        em.clear();

//        System.out.println(responseBody);
        // step 2 : DB로 get API를 조회
        MemberResponse created = objectMapper.readValue(responseBody, MemberResponse.class);
        Long memberId = created.getId();

        mockMvc.perform(get("/api/members/" + memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberId))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.point").value(0));
        // 생성 후 조회를 이어서 '함께"

        em.flush();
        em.clear();

        // step 3 : DB 직접 확인
        Member inDB = memberRepository.findById(memberId).orElseThrow();
        assertThat(inDB.getEmail()).isEqualTo(request.getEmail());
    }

    // 6-4
    // 예외 상황 통합 테스트
    @Test
    @DisplayName("중복 이메일 가입 시 에러")
    void fullFlow_duplicateEmail() throws Exception {
        // Step 1 : 첫 번째 가입 성공
        MemberRequest request = MemberRequest.builder()
                .email("i@i.i")
                .name("김통합")
                .build();

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());
        // Step 2 : 두 번째 가입 실패
        MemberRequest request2 = MemberRequest.builder()
                .email("i@i.i")
                .name("박통합")
                .build();

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2))
                )
//                .andExpect(status().isOk())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 사용 중인 이메일입니다"))
        ;
        // step 3: DB에 1개만 존재하는지 체크
        assertThat(memberRepository.count()).isEqualTo(1);
    }

    // 6-5
    // 트랜잭션 롤백
    @Test
    @DisplayName("1차 시도")
    void transactionRollback_test1() throws Exception {
        MemberRequest request = MemberRequest.builder()
                .email("t@t.t")
                .name("김트잭")
                .build();

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        assertThat(memberRepository.existsByEmail(request.getEmail())).isTrue();
        System.out.println("<<t@t.t가 존재 여부>>");
    }

    @Test
    @DisplayName("2차 시도")
    void transactionRollback_test2() throws Exception {
        assertThat(memberRepository.existsByEmail("t@t.t")).isFalse();

        MemberRequest request = MemberRequest.builder()
                .email("t@t.t")
                .name("김트잭")
                .build();

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated()); // badRequest 없이 잘 들어간다면 롤백이 된 것
    }
}
