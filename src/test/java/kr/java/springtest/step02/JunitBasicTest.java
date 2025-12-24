package kr.java.springtest.step02;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// 2-1
public class JunitBasicTest {

    // 2-1-1
    // 테스트 간 공유하는 필드 -> 테스트 간 독립성
    private List<String> testList;

    // 2-1-2
    // 모든 테스트 실행 시 일괄적으로 실행되어야하는 것
    @BeforeAll
    static void beforeAll() {
        // 각각의 테스트는 별도의 객체로 취급하는게 맞음
        // -> 전체에 한 번만 1) static 2) @BeforeAll
        System.out.println("=== 테스트 클래스 시작 ===");
    }

    // 2-1-3
    @AfterAll // 테스트 이후에 한 번씩 실행
    static void afterAll() {
        System.out.println("=== 테스트 클래스 종료 ===");
    }

    @Test
    void test1() {
        System.out.println("테스트1");
    }

    @Test
    void test2() {
        System.out.println("테스트2");
    }

    // 2-1-4
    @BeforeEach
    void setUp() { // '개별' 테스트 전에
        System.out.println("테스트 준비");
        testList = new ArrayList<>();
    }

    // 2-1-5
    @AfterEach
    void tearDown() { // '개별' 테스트 후에
        System.out.println("테스트 정리");
        testList.clear();
    }

    @Test
    @DisplayName("기본 assertEquals 테스트")
    void basicAssertion() {
        // given
        int a = 1;
        int b = 2;
        // when
        int result = a + b;
//        int result = a - b;
        // then
        assertEquals(3, result); // assertEquals(expected:예상값, actual:실제값)
    }


    @Test
    @DisplayName("AssertJ를 사용한 가독성 좋은 테스트")
    void assertJTest() {
        // given
        String actual = "Hello Spring Test";

        // then: AssertJ의 fluent API
        assertThat(actual)
                .isNotNull()                      // null이 아님
                .startsWith("Hello")              // "Hello"로 시작
                .contains("Spring")               // "Spring" 포함
                .endsWith("Test")                 // "Test"로 끝남
                .hasSize(17);                     // 길이가 17
    }

    @Test
    @DisplayName("컬렉션 테스트")
    void collectionTest() {
        // given
        testList.add("apple");
        testList.add("banana");
        testList.add("cherry");

        // then
        assertThat(testList)
                .hasSize(3)                       // 크기가 3
                .contains("apple", "banana")      // apple, banana 포함
                .doesNotContain("grape")          // grape 미포함
                .containsExactly("apple", "banana", "cherry");  // 순서까지 일치
    }

    @Test
    @DisplayName("예외 테스트 - assertThrows")
    void exceptionTest_junitStyle() {
        // given
        List<String> emptyList = List.of();

        // when & then: JUnit5 스타일
        assertThrows(IndexOutOfBoundsException.class, () -> {
            emptyList.get(0);
        });
    }

    @Test
    @DisplayName("예외 테스트 - AssertJ 스타일")
    void exceptionTest_assertJStyle() {
        // given
        List<String> emptyList = List.of();

        // when & then: AssertJ 스타일 (더 상세한 검증 가능)
        assertThatThrownBy(() -> emptyList.get(0))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @Disabled("이 테스트는 건너뜁니다 - 데모용")
    @DisplayName("비활성화된 테스트")
    void disabledTest() {
        // 이 테스트는 실행되지 않음
        fail("이 코드는 실행되면 안 됨");
    }
}
