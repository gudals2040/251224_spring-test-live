package kr.java.springtest.step01;

import kr.java.springtest.util.PointCalculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// 1
// JUnit5 + AssertJ
// TDD 사이클
public class PointCalculatorTest {

    // 1-1
    // 테스트 대상 객체 -> 필드
    private PointCalculator calculator;

    // 1-2
    // 매번 테스트가 실행 될 때마다...
    @BeforeEach
    void setUp() {
        calculator = new PointCalculator(); // 매번 새로운 객체를 생성
    }

    // 1-3
    @Test
    @DisplayName("BRONZE 등급은 1% 적립된다")
    void earnPoint_bronze() {
        // given : 조건
        int paymentAmount = 10_000;
        String grade = "BRONZE";
        // when : 실행
        int earnPoint = calculator.calculateEarnPoint(paymentAmount, grade);
        // then : 결과
        // import static org.assertj.core.api.Assertions.assertThat;
        assertThat(earnPoint).isEqualTo(100);
    }
}
