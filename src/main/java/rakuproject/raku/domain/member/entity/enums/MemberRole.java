package rakuproject.raku.domain.member.entity.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberRole {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");
    private final String key;
}
