package rakuproject.raku.domain.move.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rakuproject.raku.domain.move.entity.MoveCompanyEntity;

import java.util.Optional;

@Repository
public interface MoveCompanyRepository extends JpaRepository<MoveCompanyEntity, Integer> {
    @Query("SELECT m FROM MoveCompanyEntity m WHERE m.moveCity LIKE %:city%")
    Page<MoveCompanyEntity> findByMoveCityContaining(@Param("city") String city, Pageable pageable);
    public Optional<MoveCompanyEntity> findByBusinessNumber(String businessNumber);

}


