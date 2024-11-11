package rakuproject.raku.domain.move.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoveCompanyDTO {
    private Integer id;  // 如果在创建时不需要 ID，则可以忽略该字段
    private String name;
    private String ceo;
    private String phone;
    private String email;
    private String postalCode;
    private String address;
    private String detailedAddress;
    private String businessNumber;
    private Long fileId;
    private String service;
    private String moveCity;
    private String imgUrl;
    private String introduction;
}
