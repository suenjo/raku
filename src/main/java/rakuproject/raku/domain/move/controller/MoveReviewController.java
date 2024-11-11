package rakuproject.raku.domain.move.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rakuproject.raku.domain.member.entity.MemberEntity;
import rakuproject.raku.domain.member.exception.NotFoundMemberException;
import rakuproject.raku.domain.member.repository.MemberRepository;
import rakuproject.raku.domain.member.service.MemberService;
import rakuproject.raku.domain.move.dto.MoveReviewDTO;
import rakuproject.raku.domain.move.entity.MoveCompanyEntity;
import rakuproject.raku.domain.move.repository.MoveCompanyRepository;
import rakuproject.raku.domain.move.service.MoveCompanyService;
import rakuproject.raku.domain.move.service.MoveReviewService;
import rakuproject.raku.global.jwt.JwtUtil;

import java.util.List;


@RestController
@RequestMapping("/move/review")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MoveReviewController {

    @Autowired
    private MoveReviewService moveReviewService;
    @Autowired
    private MoveCompanyRepository moveCompanyRepository;

    @Autowired
    private MoveCompanyService moveCompanyService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 创建评论
    @PostMapping("/{companyId}/reviews")
    public ResponseEntity<String> createReview(
            @PathVariable("companyId") Integer companyId,
            @RequestBody MoveReviewDTO reviewDTO,
            @RequestHeader("Authorization") String token) {

        // 检查 Authorization header 是否存在
        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("Token is missing or malformed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need to log in first!");
        }

//        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // 去掉 "Bearer " 前缀
        }

// 检查 token 是否有效
        Claims claims = jwtUtil.getClaims(token);
        if (claims == null || jwtUtil.isTokenExpired(claims)) {
            System.out.println("Token is expired or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        // 打印出用户ID
        String userId = claims.getSubject();
        System.out.println("User ID from token: " + userId);

        // 查找用户
        MemberEntity user = memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundMemberException("User not found"));

        System.out.println("User found: " + user.getId());

        // 添加评论
        moveReviewService.addReview(companyId, user, reviewDTO.getComment());

        return ResponseEntity.status(HttpStatus.CREATED).body("Review added successfully");
    }


    @GetMapping("/{companyId}/reviews")
    public ResponseEntity<List<MoveReviewDTO>> getReviews(@PathVariable("companyId") Integer companyId) {
        // 根据公司 ID 查找公司
        MoveCompanyEntity company = moveCompanyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundMemberException("Company not found"));

        // 获取评论
        List<MoveReviewDTO> reviews = moveReviewService.getReviewsByCompany(company);
        return ResponseEntity.ok(reviews);
    }

}
