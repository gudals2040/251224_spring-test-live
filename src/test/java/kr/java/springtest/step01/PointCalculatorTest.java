package kr.java.springtest.step01;

import kr.java.springtest.util.PointCalculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    // 1-3-1
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

    // 1-3-2
    @Test
    @DisplayName("SILVER 등급은 3% 적립된다")
    void earnPoint_silver() {
        // given : 조건
        int paymentAmount = 10_000;
        String grade = "SILVER";
        // when : 실행
        int earnPoint = calculator.calculateEarnPoint(paymentAmount, grade);
        // then : 결과
        // import static org.assertj.core.api.Assertions.assertThat;
        assertThat(earnPoint).isEqualTo(300);
    }

    // 1-3-3
    @Test
    @DisplayName("GOLD 등급은 5% 적립된다")
    void earnPoint_gold() {
        // given : 조건
        int paymentAmount = 10_000;
        String grade = "GOLD";
        // when : 실행
        int earnPoint = calculator.calculateEarnPoint(paymentAmount, grade);
        // then : 결과
        // import static org.assertj.core.api.Assertions.assertThat;
        assertThat(earnPoint).isEqualTo(500);
    }

    // 1-4-1
    @Test
    @DisplayName("결제 금액이 음수면 예외가 발생한다")
    void earnPoint_negativeAmount_throwsException() {
        // given : 조건
        int paymentAmount = -10_000;
        String grade = "BRONZE";
        // when & then
        assertThatThrownBy(() -> calculator.calculateEarnPoint(paymentAmount, grade))
                .isInstanceOf(IllegalArgumentException.class) // throw된 exception의 타입 종류
//                .hasMessage("0 이상");
                .hasMessageContaining("0 이상");
    }

    // 1-4-2
    @Test
    @DisplayName("알 수 없는 등급이면 예외가 발생한다")
    void earnPoint_unknownGrade_throwsException() {
        // given : 조건
        int paymentAmount = 10_000;
        String grade = "DIAMOND";
        // when & then
        assertThatThrownBy(() -> calculator.calculateEarnPoint(paymentAmount, grade))
                .isInstanceOf(IllegalArgumentException.class) // throw된 exception의 타입 종류
                .hasMessageContaining("알 수 없는 등급");
    }

    // ...
    // https://github.com/kimjava911/251224_spring-test/blob/main/src/test/java/kr/java/springtest/step01/PointCalculatorTest.java

}
