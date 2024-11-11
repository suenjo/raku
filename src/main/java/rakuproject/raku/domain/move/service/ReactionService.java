package rakuproject.raku.domain.move.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rakuproject.raku.domain.move.entity.ReactionEntity;
import rakuproject.raku.domain.move.entity.ReviewReactionCountEntity;
import rakuproject.raku.domain.move.entity.MoveReviewEntity;
import rakuproject.raku.domain.move.entity.enums.ReactionType;
import rakuproject.raku.domain.move.repository.ReactionRepository;
import rakuproject.raku.domain.move.repository.ReviewReactionCountRepository;
import rakuproject.raku.domain.move.repository.MoveReviewRepository;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final ReviewReactionCountRepository reviewReactionCountRepository;
    private final MoveReviewRepository moveReviewRepository;

    public ReactionService(ReactionRepository reactionRepository, ReviewReactionCountRepository reviewReactionCountRepository, MoveReviewRepository moveReviewRepository) {
        this.reactionRepository = reactionRepository;
        this.reviewReactionCountRepository = reviewReactionCountRepository;
        this.moveReviewRepository = moveReviewRepository;
    }

    public List<ReactionEntity> getAllReactions() {
        return reactionRepository.findAll();
    }

    public ReactionEntity getReactionById(Long id) {
        Optional<ReactionEntity> reaction = reactionRepository.findById(id);
        return reaction.orElseThrow(() -> new RuntimeException("Reaction not found with id: " + id));
    }

    @Transactional
    public ReactionEntity createReaction(ReactionEntity reaction) {
        ReactionEntity savedReaction = reactionRepository.save(reaction);
        updateReactionCount(reaction, true);
        return savedReaction;
    }


    @Transactional
    public ReactionEntity updateReaction(Long id, ReactionEntity reactionDetails) {
        ReactionEntity reaction = getReactionById(id);
        updateReactionCount(reaction, false);
        reaction.setReviewId(reactionDetails.getReviewId());
        reaction.setUserKey(reactionDetails.getUserKey());
        reaction.setReactionType(reactionDetails.getReactionType());
        ReactionEntity updatedReaction = reactionRepository.save(reaction);
        updateReactionCount(updatedReaction, true);
        return updatedReaction;
    }

    @Transactional
    public void deleteReaction(Long id) {
        ReactionEntity reaction = getReactionById(id);
        updateReactionCount(reaction, false);
        reactionRepository.delete(reaction);
    }

    private void updateReactionCount(ReactionEntity reaction, boolean isIncrement) {
        MoveReviewEntity review = moveReviewRepository.findById(reaction.getReviewId().getReviewId())
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reaction.getReviewId().getReviewId()));
        ReviewReactionCountEntity countEntity = reviewReactionCountRepository.findByReviewId(review)
                .orElse(new ReviewReactionCountEntity());

        if (reaction.getReactionType() == ReactionType.LIKE) {
            if (isIncrement) {
                countEntity.setLikeCount(countEntity.getLikeCount() + 1);
            } else {
                countEntity.setLikeCount(countEntity.getLikeCount() - 1);
            }
        } else if (reaction.getReactionType() == ReactionType.DISLIKE) {
            if (isIncrement) {
                countEntity.setDislikeCount(countEntity.getDislikeCount() + 1);
            } else {
                countEntity.setDislikeCount(countEntity.getDislikeCount() - 1);
            }
        }

        reviewReactionCountRepository.save(countEntity);
    }
}
