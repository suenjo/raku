package rakuproject.raku.domain.move.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rakuproject.raku.domain.move.dto.MoveCompanyDTO;
import rakuproject.raku.domain.move.dto.UploadResultDTO;
import rakuproject.raku.domain.move.entity.MoveCompanyEntity;
import rakuproject.raku.domain.move.entity.UploadFileEntity;
import rakuproject.raku.domain.move.repository.MoveCompanyRepository;
import rakuproject.raku.domain.move.repository.UploadFileRepository;
import rakuproject.raku.domain.move.service.MoveCompanyService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/move/company")
@CrossOrigin(origins = "http://localhost:3000")
public class MoveCompanyController {

    private final MoveCompanyService moveCompanyService;
    private final MoveCompanyRepository moveCompanyRepository;
    private final UploadFileRepository uploadFileRepository;

    @Value("${application.upload.path}")
    private String uploadPath;
    //创建公司信息。
    @PostMapping(value = "/createCompany", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MoveCompanyDTO> createCompany(@RequestBody MoveCompanyDTO companyDTO) {
        MoveCompanyEntity entity = convertToEntity(companyDTO);
        MoveCompanyEntity savedEntity = moveCompanyRepository.save(entity);
        return new ResponseEntity<>(convertToDTO(savedEntity), HttpStatus.CREATED);
    }

    @PostMapping(value = "/createWithFiles", consumes = {"multipart/form-data"})
    public ResponseEntity<MoveCompanyDTO> createCompanyWithFiles(
            @RequestPart("uploadFiles") List<MultipartFile> uploadFiles,
            @RequestParam("name") String name,
            @RequestParam("ceo") String ceo,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("postalCode") String postalCode,
            @RequestParam("address") String address,
            @RequestParam("detailedAddress") String detailedAddress,
            @RequestParam("businessNumber") String businessNumber,
            @RequestParam("service") String service,
            @RequestParam("city") String city,
            @RequestParam("introduction") String introduction){

        // 创建公司信息
        MoveCompanyEntity company = new MoveCompanyEntity();
        company.setName(name);
        company.setCeo(ceo);
        company.setPhone(phone);
        company.setEmail(email);
        company.setPostalCode(postalCode);
        company.setAddress(address);
        company.setDetailedAddress(detailedAddress);
        company.setBusinessNumber(businessNumber);
        company.setService(service);
        company.setMoveCity(city);
        company.setIntroduction(introduction);

        MoveCompanyEntity savedCompany = moveCompanyRepository.save(company);

        // 保存上传的文件
        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            for (MultipartFile file : uploadFiles) {
                if (!file.getContentType().startsWith("image")) {
                    log.warn("File is not an image type");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }

                String fileName = file.getOriginalFilename();
                String folderPath = makeFolder();
                String uuid = UUID.randomUUID().toString();
                String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

                try {
                    Path path = Paths.get(saveName);
                    file.transferTo(path);

                    String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;
                    File thumbnailFile = new File(thumbnailSaveName);
                    Thumbnailator.createThumbnail(path.toFile(), thumbnailFile, 200, 200);

                    // 保存文件信息到数据库
                    UploadFileEntity uploadFile = new UploadFileEntity();
                    uploadFile.setFileName(fileName);
                    uploadFile.setUuid(uuid);
                    uploadFile.setFolderPath(folderPath);
                    uploadFile.setUploadDate(LocalDate.now());
                    uploadFile.setCompanyId(savedCompany);

                    UploadFileEntity savedFile = uploadFileRepository.save(uploadFile);

                    // 设置公司图标字段
                    savedCompany.setFileId(savedFile);
                    moveCompanyRepository.save(savedCompany);

                } catch (IOException e) {
                    log.error("Error while saving file: ", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }

        return new ResponseEntity<>(convertToDTO(savedCompany), HttpStatus.CREATED);
    }


    @PostMapping(value = "/uploadFiles/{companyId}", consumes = {"multipart/form-data"})
    public ResponseEntity<List<UploadResultDTO>> uploadFiles(
            @PathVariable("companyId") int companyId,
            @RequestPart("uploadFiles") List<MultipartFile> uploadFiles,
            @RequestParam(value = "updateIcon", defaultValue = "false") boolean updateIcon) {

        List<UploadResultDTO> resultDTOList = new ArrayList<>();
        MoveCompanyEntity company = moveCompanyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        for (MultipartFile file : uploadFiles) {
            log.info("Uploading file: {}", file.getOriginalFilename());

            if (!file.getContentType().startsWith("image")) {
                log.warn("File is not an image type");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String fileName = file.getOriginalFilename();
            String folderPath = makeFolder();
            String uuid = UUID.randomUUID().toString();
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            try {
                Path path = Paths.get(saveName);
                file.transferTo(path);

                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;
                File thumbnailFile = new File(thumbnailSaveName);
                Thumbnailator.createThumbnail(path.toFile(), thumbnailFile, 200, 200);

                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));

                // 保存文件信息到数据库
                UploadFileEntity uploadFile = new UploadFileEntity();
                uploadFile.setFileName(fileName);
                uploadFile.setUuid(uuid);
                uploadFile.setFolderPath(folderPath);
                uploadFile.setUploadDate(LocalDate.now());
                uploadFile.setCompanyId(company);  // 设置文件与公司的关联

                UploadFileEntity savedFile = uploadFileRepository.save(uploadFile);

                // 如果 updateIcon 为 true，更新公司的 fileId 字段
                if (updateIcon) {
                    company.setFileId(savedFile);  // 设置公司与文件的关联
                    moveCompanyRepository.save(company);
                }

            } catch (IOException e) {
                log.error("Error while saving file: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.ok(resultDTOList);
    }


    // 创建文件夹方法
    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }


    // 获取所有公司信息
    @GetMapping("/companies")
    public ResponseEntity<List<MoveCompanyDTO>> getAllCompanies() {
        List<MoveCompanyEntity> companies = moveCompanyService.getAllCompanies();
        List<MoveCompanyDTO> companyDTOs = companies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(companyDTOs, HttpStatus.OK);
    }

    // 根据城市分页获取公司信息
    @GetMapping("/companies/cityFind")
    public ResponseEntity<Page<MoveCompanyDTO>> getCompaniesByCity(@RequestParam("city") String city,
                                                                   @RequestParam("page") int page,
                                                                   @RequestParam("size") int size) {
        Page<MoveCompanyEntity> companies = moveCompanyService.getCompaniesByCity(city, page, size);
        // 将实体转换为 DTO
        List<MoveCompanyDTO> companyDTOs = companies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        // 构建分页的 DTO 响应
        Page<MoveCompanyDTO> companyDTOPage = new PageImpl<>(companyDTOs, PageRequest.of(page, size), companies.getTotalElements());

        return new ResponseEntity<>(companyDTOPage, HttpStatus.OK);
    }

    // 根据 ID 获取公司信息
    @GetMapping("/companies/{id}")
    public ResponseEntity<MoveCompanyDTO> getCompanyById(@PathVariable("id") Integer id) {
        MoveCompanyEntity company = moveCompanyService.getCompanyById(id);
        return new ResponseEntity<>(convertToDTO(company), HttpStatus.OK);
    }

    // 删除公司信息
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable("id") Integer id) {
        try {
            moveCompanyService.deleteCompany(id);
            return new ResponseEntity<>("削除が成功しました。", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("削除する会社が見つかりません。", HttpStatus.NOT_FOUND);
        }
    }

    // DTO 转换为实体
    private MoveCompanyEntity convertToEntity(MoveCompanyDTO companyDTO) {
        MoveCompanyEntity entity = new MoveCompanyEntity();
        entity.setId(companyDTO.getId());
        entity.setName(companyDTO.getName());
        entity.setCeo(companyDTO.getCeo());
        entity.setPhone(companyDTO.getPhone());
        entity.setEmail(companyDTO.getEmail());
        entity.setPostalCode(companyDTO.getPostalCode());
        entity.setAddress(companyDTO.getAddress());
        entity.setDetailedAddress(companyDTO.getDetailedAddress());
        entity.setBusinessNumber(companyDTO.getBusinessNumber());
        entity.setService(companyDTO.getService());
        entity.setMoveCity(companyDTO.getMoveCity());
        entity.setIntroduction(companyDTO.getIntroduction());
        // 根据 imgIconId 查找对应的 UploadFileEntity 并设置
        if (companyDTO.getFileId() != null) {
            UploadFileEntity imgIcon = uploadFileRepository.findById(companyDTO.getFileId())
                    .orElseThrow(() -> new RuntimeException("Image not found with id: " + companyDTO.getFileId()));
            entity.setFileId(imgIcon);
        }
        return entity;
    }

    // 实体转换为 DTO
    private MoveCompanyDTO convertToDTO(MoveCompanyEntity entity) {
        MoveCompanyDTO companyDTO = new MoveCompanyDTO();
        companyDTO.setId(entity.getId());
        companyDTO.setName(entity.getName());
        companyDTO.setCeo(entity.getCeo());
        companyDTO.setPhone(entity.getPhone());
        companyDTO.setEmail(entity.getEmail());
        companyDTO.setPostalCode(entity.getPostalCode());
        companyDTO.setAddress(entity.getAddress());
        companyDTO.setDetailedAddress(entity.getDetailedAddress());
        companyDTO.setBusinessNumber(entity.getBusinessNumber());
        companyDTO.setService(entity.getService());
        companyDTO.setMoveCity(entity.getMoveCity());
        companyDTO.setIntroduction(entity.getIntroduction());
        // 如果 imgIcon 不为 null，设置 imgIconId
        // 设置 fileId 以便可以引用具体的文件
        if (entity.getFileId() != null) {
            companyDTO.setFileId(entity.getFileId().getId());
            // 设置 imgUrl，假设文件通过 `/uploads/` 路径访问
            String imgUrl = "http://localhost:8080/uploads/" + entity.getFileId().getFolderPath() + "/" + entity.getFileId().getUuid() + "_" + entity.getFileId().getFileName();
            companyDTO.setImgUrl(imgUrl);
        } else {
            // 设置默认图片 URL
            companyDTO.setImgUrl("/Users/sueunjo/Documents/S/img/2.png");
        }

        return companyDTO;
    }
}
