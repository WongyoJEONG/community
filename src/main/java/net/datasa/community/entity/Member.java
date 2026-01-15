package net.datasa.community.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "community_member")
public class Member {

    @Id
    @Column(name = "member_id", length = 20)
    private String memberId; // 아이디 (PK)

    @Column(name = "member_pw", nullable = false, length = 100)
    private String memberPw; // 비밀번호

    @Column(name = "name", nullable = false, length = 20)
    private String name;     // 이름

    @Column(name = "email", length = 200)
    private String email;    // 이메일
}