package kr.java.springtest.service;

import kr.java.springtest.model.dto.MemberRequest;
import kr.java.springtest.model.dto.MemberResponse;
import kr.java.springtest.model.entity.Member;
import kr.java.springtest.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
// import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public MemberResponse register(MemberRequest request) {
//        return null;
//        return MemberResponse.from(request.toEntity());

//        memberRepository.save(request.toEntity());

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다");
//            throw new IllegalArgumentException("중복 유저입니다");
        }

        Member member = request.toEntity();
//        return MemberResponse.from(member);
        Member saved = memberRepository.save(member);

        return MemberResponse.from(saved);
    }

    // ID로 회원 조회
    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + id));

        return MemberResponse.from(member);
    }

    // 이메일로 회원 조회
    public MemberResponse findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + email));

        return MemberResponse.from(member);
    }

    // 전체 회원 조회
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    // 포인트 충전
    @Transactional
    public MemberResponse addPoint(Long id, int amount) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + id));

        member.addPoint(amount);

        return MemberResponse.from(member);
    }

    // 회원 삭제
    @Transactional
    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다: " + id);
        }
        memberRepository.deleteById(id);
    }
}
