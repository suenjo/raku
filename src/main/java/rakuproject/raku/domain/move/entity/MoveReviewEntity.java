package rakuproject.raku.domain.move.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import rakuproject.raku.domain.member.entity.MemberEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // 保留无参构造函数
@Entity
@Table(name = "move_reviews")
public class MoveReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private MoveCompanyEntity company;

    @ManyToOne
    @JoinColumn(name = "user_key", referencedColumnName = "user_key")
    private MemberEntity userId;

    @Column(name = "service_rating")
    private Long rating;

    @Column(name = "price")
    private String price;

    @Column(name = "region")
    private String region;//이사 지역...

    @Column(name = "service_date")
    private LocalDate serviceDate;

    @Column(name = "review_comment")
    private String comment;

    @Column(name = "review_created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}

