package rakuproject.raku.domain.move.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rakuproject.raku.domain.move.entity.MoveCompanyEntity;
import rakuproject.raku.domain.move.entity.MoveReviewEntity;

import java.util.List;

@Repository
public interface MoveReviewRepository extends JpaRepository<MoveReviewEntity, Long> {
    List<MoveReviewEntity> findByCompany(MoveCompanyEntity company);
}