package net.datasa.community.service;

import lombok.RequiredArgsConstructor;
import net.datasa.community.entity.Member;
import net.datasa.community.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입 처리
    public void join(Member member) {
        // 비밀번호 암호화
        String encodedPw = passwordEncoder.encode(member.getMemberPw());
        member.setMemberPw(encodedPw);

        // DB 저장
        memberRepository.save(member);
    }
}