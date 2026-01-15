package net.datasa.community.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "community_group") // [중요] 예약어 충돌 방지
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;    // PK

    // 신청 대상 글 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_num")
    @ToString.Exclude // [중요] 순환 참조 방지
    private Board board;

    // 신청자 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @ToString.Exclude // [중요] 순환 참조 방지
    private Member member;

    @Column(length = 20)
    private String role;        // 역할 (LEADER/MEMBER)

    @Column(length = 20)
    private String status;      // 상태 (PENDING/JOINED/REJECTED)

    @CreationTimestamp
    @Column(name = "joined_date")
    private LocalDateTime joinedDate; // 신청일
}