package rakuproject.raku.domain.member.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import rakuproject.raku.domain.member.entity.enums.MemberRole;

@Builder
@Getter
@Setter
public class MemberDTO {
    private Long userKey;
    private String id;
    private String pwd;
    private String nick;
    private int address;
    private MemberRole role;
    private int alarm;
    private String bookmark;
    private String recent;

}
