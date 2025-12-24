package kr.java.springtest.util;

/**
 * TDD 실습을 위한 순수 Java 클래스
 * Spring 의존성 없이 포인트 계산 로직만 담당
 */
public class PointCalculator {

    // 등급별 적립률 (%)
//    private static final int BRONZE_RATE = 2;
    private static final int BRONZE_RATE = 1;
    private static final int SILVER_RATE = 3;
    private static final int GOLD_RATE = 5;

    /**
     * 결제 금액에 따른 적립 포인트 계산
     * @param paymentAmount 결제 금액
     * @param grade 회원 등급 (BRONZE, SILVER, GOLD)
     * @return 적립될 포인트
     */
    public int calculateEarnPoint(int paymentAmount, String grade) {
        if (paymentAmount < 0) {
            throw new IllegalArgumentException("결제 금액은 0 이상이어야 합니다");
        }

        int rate = switch (grade.toUpperCase()) {
            case "BRONZE" -> BRONZE_RATE;
            case "SILVER" -> SILVER_RATE;
            case "GOLD" -> GOLD_RATE;
            default -> throw new IllegalArgumentException("알 수 없는 등급입니다: " + grade);
        };

        return paymentAmount * rate / 100;
//        return paymentAmount * rate / 1000;
    }

    /**
     * 누적 포인트에 따른 등급 결정
     * @param totalPoint 누적 포인트
     * @return 회원 등급
     */
    public String determineGrade(int totalPoint) {
        if (totalPoint < 0) {
            throw new IllegalArgumentException("포인트는 0 이상이어야 합니다");
        }

        if (totalPoint >= 100_000) {
            return "GOLD";
        } else if (totalPoint >= 10_000) {
            return "SILVER";
        } else {
            return "BRONZE";
        }
    }
}
