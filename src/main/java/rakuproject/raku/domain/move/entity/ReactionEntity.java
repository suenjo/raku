package rakuproject.raku.domain.move.entity;

import jakarta.persistence.*;
import lombok.*;
import rakuproject.raku.domain.member.entity.MemberEntity;
import rakuproject.raku.domain.move.entity.enums.ReactionType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reactions")
public class ReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private Long reactionId;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "review_id", nullable = false)
    private MoveReviewEntity reviewId; // 评论的ID，外键关联到MoveReview

    @ManyToOne
    @JoinColumn(name = "user_key", referencedColumnName = "user_key", nullable = false)
    private MemberEntity userKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType reactionType; // 反应类型，like 或 dislike

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 反应的创建时间

}

