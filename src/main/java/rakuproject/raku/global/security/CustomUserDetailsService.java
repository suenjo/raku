package rakuproject.raku.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rakuproject.raku.domain.member.dto.MemberDTO;
import rakuproject.raku.domain.member.exception.NotFoundMemberException;
import rakuproject.raku.domain.member.mapper.MemberMapper;
import rakuproject.raku.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDTO memberDTO=memberRepository.
                findById(username)
                .map(MemberMapper::createDTO)
                .orElseThrow(()-> NotFoundMemberException.EXCEPTION);
        return CustomUserDetails.create(memberDTO);
    }
}
