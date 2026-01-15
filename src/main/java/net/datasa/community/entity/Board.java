package net.datasa.community.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "community_board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_num")
    private Integer boardNum;   // 글 번호 (PK)

    // 작성자 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @ToString.Exclude // [중요] 순환 참조 방지
    private Member member;

    @Column(nullable = false, length = 50)
    private String category;    // 분류

    @Column(nullable = false, length = 200)
    private String title;       // 제목

    @Column(nullable = false, length = 2000)
    private String contents;    // 내용

    @Column(name = "headcnt", columnDefinition = "integer default 0")
    private Integer headcnt;    // 현재 인원

    @Column(columnDefinition = "integer default 5")
    private Integer capacity;   // 정원

    @Column(length = 100)
    private String status;      // 상태 (OPEN/CLOSED)

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate; // 작성일

    // 게시글 삭제 시 신청 내역도 삭제 (Cascade)
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    @ToString.Exclude // [중요] 순환 참조 방지
    private List<Group> groupList;
}