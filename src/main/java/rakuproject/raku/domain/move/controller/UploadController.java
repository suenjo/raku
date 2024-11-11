package rakuproject.raku.domain.move.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rakuproject.raku.domain.move.dto.UploadResultDTO;
import rakuproject.raku.domain.move.entity.MoveCompanyEntity;
import rakuproject.raku.domain.move.entity.UploadFileEntity;
import rakuproject.raku.domain.move.repository.MoveCompanyRepository;
import rakuproject.raku.domain.move.repository.UploadFileRepository;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/move")
@Slf4j
public class UploadController {

    @Value("${application.upload.path}")
    private String uploadPath;

    private final UploadFileRepository uploadFileRepository;
    private final MoveCompanyRepository moveCompanyRepository;

    @PostMapping("/upload")
    public ResponseEntity<List<UploadResultDTO>> upload(@RequestParam("uploadFiles") List<MultipartFile> uploadFiles,
                                                        @RequestParam("companyId") int companyId) {

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

                // 使用缩略图名称而不是原始文件名称
                UploadFileEntity uploadFile = new UploadFileEntity();
                uploadFile.setFileName("s_" + fileName);  // 使用缩略图的文件名
                uploadFile.setUuid(uuid);
                uploadFile.setFolderPath(folderPath);
                uploadFile.setUploadDate(LocalDate.now());
                uploadFile.setCompanyId(company);

                uploadFileRepository.save(uploadFile);

            } catch (IOException e) {
                log.error("Error while saving file: ", e);
            }
        }
        return ResponseEntity.ok(resultDTOList);
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> remove(@RequestParam("fileName") String fileName) {
        log.info("Deleting file: {}", fileName);
        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);

            if (file.delete()) {
                File thumbnailFile = new File(file.getParent(), "s_" + file.getName());
                thumbnailFile.delete();
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Error decoding file name: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(@RequestParam("fileName") String fileName) {
        log.info("Displaying file: {}", fileName);
        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(file.toPath()));

            return new ResponseEntity<>(Files.readAllBytes(file.toPath()), headers, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            log.error("Error decoding file name: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            log.error("Error reading file: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }
}
