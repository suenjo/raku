package rakuproject.raku.domain.move.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rakuproject.raku.domain.move.entity.ReactionEntity;
import rakuproject.raku.domain.move.entity.ReviewReactionCountEntity;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {
}

