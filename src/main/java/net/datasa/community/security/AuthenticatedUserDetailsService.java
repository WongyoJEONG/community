package net.datasa.community.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.community.entity.Member;
import net.datasa.community.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증 처리 (DB 조회)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticatedUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        log.info("로그인 시도 : {}", id);

        // 1. DB에서 회원 정보 조회
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id + " : 없는 ID입니다."));

        // 2. 조회된 정보를 기반으로 UserDetails(AuthenticatedUser) 객체 생성
        // DB 테이블(community_member)에 role이나 enabled 컬럼이 없으므로 기본값을 설정합니다.
        AuthenticatedUser user = AuthenticatedUser.builder()
                .id(member.getMemberId())
                .password(member.getMemberPw()) // DB의 암호화된 비밀번호
                .name(member.getName())         // 사용자 이름
                .role("ROLE_USER")              // 권한 (DB에 없으므로 기본값 부여)
                .build();

        log.debug("인증 객체 생성 완료 : {}", user);

        return user;
    }
}