package rakuproject.raku.domain.move;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import rakuproject.raku.RakuApplication;
import rakuproject.raku.domain.move.entity.MoveCompanyEntity;
import rakuproject.raku.domain.move.entity.UploadFileEntity;
import rakuproject.raku.domain.move.repository.MoveCompanyRepository;
import rakuproject.raku.domain.move.repository.UploadFileRepository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RakuApplication.class)
@AutoConfigureMockMvc
public class MoveCompanyRepositoryTest {

    @Autowired
    private MoveCompanyRepository moveCompanyRepository;

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @Rollback(false)
    void testCreateCompanyWithFiles() throws Exception {
        // 准备地区和服务列表
        List<String> japanRegions = Arrays.asList("全地域", "東京都", "大阪府", "愛知県", "埼玉県", "千葉県", "兵庫県", "北海道", "福岡県",
                "静岡県", "茨城県", "広島県", "京都府", "宮城県", "新潟県", "長野県", "岐阜県", "群馬県",
                "栃木県", "岡山県", "神奈川県");
        List<String> services = Arrays.asList("梱包サービス", "清掃サービス", "保管サービス", "ピアノ運搬", "家具組み立て", "安全設置");

        Random random = new Random();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            // 随机选择 5～10 个地区
            Collections.shuffle(japanRegions);
            int regionCount = random.nextInt(6) + 5; // 随机选择数量，范围是 5 到 10
            String selectedRegions = japanRegions.stream().limit(regionCount).collect(Collectors.joining(","));

            // 随机选择 4～7 个服务
            Collections.shuffle(services);
            int serviceCount = random.nextInt(4) + 4; // 随机选择数量，范围是 4 到 7
            String selectedServices = services.stream().limit(serviceCount).collect(Collectors.joining(","));

            try {
                // 每次循环生成新的随机数和文件名
                int randomNum = random.nextInt(13) + 1;
                String fileName = "imgs" + randomNum + ".png";  // 生成文件名
                byte[] imageData = Files.readAllBytes(Paths.get("src/test/resources/" + fileName));

                // 创建 MockMultipartFile 对象，模拟文件上传
                MockMultipartFile file = new MockMultipartFile("uploadFiles", fileName, "image/png", imageData);

                // 生成唯一的公司名称和邮箱
                String companyName = "Test Company " + i;
                String email = "contact" + i + "@testcompany.com";

                // 模拟创建公司并上传文件请求
                mockMvc.perform(MockMvcRequestBuilders.multipart("/move/company/createWithFiles")
                                .file(file)
                                .param("name", companyName)
                                .param("ceo", "John Doe " + i)
                                .param("phone", "123-456-789" + i)
                                .param("email", email)
                                .param("postalCode", "123-456" + i)
                                .param("address", "Tokyo, Japan")
                                .param("detailedAddress", "Shibuya District, Building 5-8")
                                .param("businessNumber", "56789-123" + i)
                                .param("service", selectedServices)
                                .param("city", selectedRegions)
                                .param("introduction", "Company " + i + " introduction")
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().isCreated());

                // 获取刚刚保存的公司实体
                MoveCompanyEntity createdCompany = moveCompanyRepository.findAll()
                        .stream()
                        .filter(company -> companyName.equals(company.getName()))
                        .findFirst()
                        .orElse(null);
                assertNotNull(createdCompany, "未找到刚刚创建的公司实体");

                // 使用 ID 进行文件上传验证
                mockMvc.perform(MockMvcRequestBuilders.multipart("/move/company/uploadFiles/" + createdCompany.getId())
                                .file(file)
                                .param("updateIcon", "true")
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().isOk());

                // 获取公司关联的上传文件列表并验证
                List<UploadFileEntity> uploadedFiles = uploadFileRepository.findByCompanyId(createdCompany);
                assertFalse(uploadedFiles.isEmpty(), "Uploaded files should be associated with the created company");

                // 使用保存的 fileName 变量进行断言，确保文件名一致
                assertEquals(fileName, uploadedFiles.get(0).getFileName(), "Uploaded file name should match");

            } catch (Exception e) {
                e.printStackTrace(); // 捕获并打印异常
            }
        });
    }
}

