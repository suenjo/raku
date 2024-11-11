package rakuproject.raku.domain.move.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rakuproject.raku.domain.member.entity.MemberEntity;
import rakuproject.raku.domain.move.dto.MoveReviewDTO;
import rakuproject.raku.domain.move.entity.MoveCompanyEntity;
import rakuproject.raku.domain.move.entity.MoveReviewEntity;
import rakuproject.raku.domain.move.exception.NotFoundMemberException;
import rakuproject.raku.domain.move.repository.MoveCompanyRepository;
import rakuproject.raku.domain.move.repository.MoveReviewRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoveReviewService {

    private final MoveReviewRepository moveReviewRepository;

    private MoveCompanyRepository moveCompanyRepository;

    @Autowired
    public MoveReviewService(MoveReviewRepository moveReviewRepository, MoveCompanyRepository moveCompanyRepository) {
        this.moveReviewRepository = moveReviewRepository;
        this.moveCompanyRepository = moveCompanyRepository;
    }

    public void addReview(Integer companyId, MemberEntity user, String comment) {
        // 查找公司实体
        MoveCompanyEntity company = moveCompanyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundMemberException("Company not found"));

        MoveReviewEntity moveReview = new MoveReviewEntity();
        moveReview.setCompany(company); // 设置评论的公司
        moveReview.setUserId(user); // 设置评论的用户
        moveReview.setComment(comment);
        moveReview.setCreatedAt(LocalDateTime.now()); // 设置评论创建时间

        moveReviewRepository.save(moveReview); // 保存评论到数据库
    }

    // 根据公司对象获取评论
    public List<MoveReviewDTO> getReviewsByCompany(MoveCompanyEntity company) {
        List<MoveReviewEntity> reviews = moveReviewRepository.findByCompany(company);
        return reviews.stream()
                .map(review -> new MoveReviewDTO(
                        review.getReviewId(),
                        review.getUserId().getId(), // 提取用户ID为String
                        review.getComment()))
                .collect(Collectors.toList());
    }

//    // 根据公司ID获取评论
//    public List<MoveReview> getReviewsByCompanyId(MoveCompany company) {
//        return moveReviewRepository.findByCompany(company);
//    }
}
