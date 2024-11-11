package rakuproject.raku.domain.move.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rakuproject.raku.domain.move.entity.MoveCompanyEntity;
import rakuproject.raku.domain.move.entity.UploadFileEntity;

import java.util.List;

public interface UploadFileRepository extends JpaRepository<UploadFileEntity, Long> {
    List<UploadFileEntity> findByCompanyId(MoveCompanyEntity companyId);
}
