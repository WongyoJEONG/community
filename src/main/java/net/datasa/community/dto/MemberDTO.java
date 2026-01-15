package net.datasa.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MemberDTO {
    private String memberId;  // 아이디
    private String memberPw;  // 비밀번호
    private String name;      // 이름
    private String email;     // 이메일
}

