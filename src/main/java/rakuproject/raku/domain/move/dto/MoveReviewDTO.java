package rakuproject.raku.domain.move.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoveReviewDTO {
    private Long reviewId;
    private Integer companyId;
    private String userId;
    private String comment;
    private String price;
    private String region;
    private LocalDate serviceDate;
    private LocalDateTime createdAt;

    public MoveReviewDTO(Long reviewId, String userId, String comment) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.comment = comment;
    }

}
