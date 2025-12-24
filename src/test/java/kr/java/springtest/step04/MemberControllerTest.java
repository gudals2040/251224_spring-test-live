package kr.java.springtest.step04;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.java.springtest.controller.MemberController;
import kr.java.springtest.model.dto.MemberRequest;
import kr.java.springtest.model.dto.MemberResponse;
import kr.java.springtest.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 4-1
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired // 필드 주입
    private MockMvc mockMvc; // 가상의 servlet에 controller

    // @Autowired // X
    @MockitoBean // 3.4+
    // @MockBean <- 3.3.x 부터 없어짐
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper; // 객체 -> JSON Serializer (문자열)

    private MemberResponse sampleResponse; // <- memberService;

    // 4-2
    @BeforeEach
    void setUp() {
        sampleResponse = MemberResponse.builder() // service에서 나왔다고 가정
                .id(1L)
                .email("test@example.com")
                .name("테스터")
                .point(1000)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 성공 & 400 에러
    // 4-3
    @Test
    @DisplayName("POST /api/members - 회원 가입 성공")
    void register_success() throws Exception {
        // given
        MemberRequest request = MemberRequest.builder()
                .email(sampleResponse.getEmail())
                .name(sampleResponse.getName())
                .build(); // 직접 입력도 괜찮음

        given(memberService.register(any(MemberRequest.class)))
                .willReturn(sampleResponse);

        // when & then -> mockMvc -> perform
        mockMvc.perform(
                post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ) // when
            // then
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value(sampleResponse.getEmail()))
            .andExpect(jsonPath("$.name").value(sampleResponse.getName()))
            .andExpect(jsonPath("$.point").value(sampleResponse.getPoint()))
            .andDo(print())
        ;
    }

    // 4-4
    @Test
    @DisplayName("POST /api/members - 이메일 누락 시 400 에러")
    void register_emailMissing_badRequest() throws Exception {
        // given
        MemberRequest request = MemberRequest.builder()
//                .email(sampleResponse.getEmail()) // 이메일 요청 없음
                // -> controller -> valid
                .name(sampleResponse.getName())
                .build(); // 직접 입력도 괜찮음

        given(memberService.register(any(MemberRequest.class)))
                .willReturn(sampleResponse);

        // when & then -> mockMvc -> perform
        mockMvc.perform(
                        post("/api/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ) // when
                // then
                .andExpect(status().isBadRequest())
                // ErrorResponse
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다"))
                .andDo(print())
        ;
    }
}
