package rakuproject.raku.global.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rakuproject.raku.domain.member.dto.MemberDTO;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final MemberDTO memberDTO;
    private final Collection<? extends GrantedAuthority> authorities;

    private CustomUserDetails(MemberDTO memberDTO, Collection<? extends  GrantedAuthority> authorities) {
        this.memberDTO = memberDTO;
        this.authorities=authorities;
    }

    public static CustomUserDetails create(MemberDTO memberDTO)
    {
        return new CustomUserDetails(memberDTO, Collections.singleton(
                new SimpleGrantedAuthority(memberDTO.getRole().getKey()
                )
        )) ;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return memberDTO.getPwd();
    }

    @Override
    public String getUsername() {
        return memberDTO.getId();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
