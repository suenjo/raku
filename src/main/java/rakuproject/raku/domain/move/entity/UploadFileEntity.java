package rakuproject.raku.domain.move.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "upload_file")
public class UploadFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;  // 文件记录ID（主键）

    @Column(name = "file_name")
    private String fileName;  // 文件名

    @Column(name = "uuid")
    private String uuid;  // 唯一标识符

    @Column(name = "folder_path")
    private String folderPath;  // 文件保存路径

    @Column(name = "upload_date")
    private LocalDate uploadDate;  // 上传日期

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "company_id", foreignKey = @ForeignKey(name = "fk_company_information"), nullable = true)
    private MoveCompanyEntity companyId;

}