package rakuproject.raku.domain.move.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import rakuproject.raku.domain.member.entity.MemberEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "review_reaction_counts")
public class ReviewReactionCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "count_id")
    private Long countId;

    @OneToOne
    @JoinColumn(name = "review_id", referencedColumnName = "review_id", nullable = false)
    private MoveReviewEntity reviewId; // 评论ID，外键关联到MoveReview

    @ManyToOne
    @JoinColumn(name = "user_key", referencedColumnName = "user_key", nullable = false)
    private MemberEntity userKey; // 用户的ID，外键关联到MemberEntity

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0; // 点赞数量，初始为0

    @Column(name = "dislike_count", nullable = false)
    private Integer dislikeCount = 0; // 点踩数量，初始为0

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 反应的创建时间

}


