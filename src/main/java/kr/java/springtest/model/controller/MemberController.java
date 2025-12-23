package kr.java.springtest.model.controller;

import jakarta.validation.Valid;
import kr.java.springtest.model.dto.MemberRequest;
import kr.java.springtest.model.dto.MemberResponse;
import kr.java.springtest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody MemberRequest request) {
        MemberResponse response = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ID로 회원 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findById(@PathVariable Long id) {
        MemberResponse response = memberService.findById(id);
        return ResponseEntity.ok(response);
    }

    // 전체 회원 조회
    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAll() {
        List<MemberResponse> responses = memberService.findAll();
        return ResponseEntity.ok(responses);
    }

    // 포인트 충전
    @PatchMapping("/{id}/points")
    public ResponseEntity<MemberResponse> addPoint(
            @PathVariable Long id,
            @RequestParam int amount) {
        MemberResponse response = memberService.addPoint(id, amount);
        return ResponseEntity.ok(response);
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
