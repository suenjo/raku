package rakuproject.raku.domain.move.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import rakuproject.raku.domain.move.entity.MoveCompanyEntity;
import rakuproject.raku.domain.move.exception.ResourceNotFoundException;
import rakuproject.raku.domain.move.repository.MoveCompanyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoveCompanyService {

    private final MoveCompanyRepository moveCompanyRepository;

    public void save(MoveCompanyEntity company) {
        moveCompanyRepository.save(company);
    }

    // 添加或更新公司信息
    public MoveCompanyEntity saveOrUpdateCompany(MoveCompanyEntity company) {
        return moveCompanyRepository.save(company);
    }

    // 获取所有公司信息
    public List<MoveCompanyEntity> getAllCompanies() {
        return moveCompanyRepository.findAll();
    }

    // 根据城市分页获取公司信息
    public Page<MoveCompanyEntity> getCompaniesByCity(String city, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return moveCompanyRepository.findByMoveCityContaining(city, pageRequest);
    }


    // 根据 ID 获取公司信息
    public MoveCompanyEntity getCompanyById(Integer id) {
        return moveCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("会社が見つかりませんでした。ID: " + id));
    }

    // 删除公司信息
    public void deleteCompany(Integer id) {
        if (!moveCompanyRepository.existsById(id)) {
            throw new ResourceNotFoundException("会社が見つかりませんでした。ID: " + id);
        }
        moveCompanyRepository.deleteById(id);
    }
}
