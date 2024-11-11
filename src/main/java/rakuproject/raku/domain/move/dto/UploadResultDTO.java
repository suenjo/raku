package rakuproject.raku.domain.move.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {

    private String fileName;    // 文件名
    private String uuid;        // 唯一标识符
    private String folderPath;  // 文件保存路径
}
