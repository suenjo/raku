package rakuproject.raku.domain.move.dto;

import lombok.*;
import rakuproject.raku.domain.move.entity.enums.ReactionType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReactionDTO {
    private Long userId;
    private Long reviewId;
    private ReactionType reactionType;
}
