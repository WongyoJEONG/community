package net.datasa.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 소모임 신청 정보 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private Integer groupId;    // PK

    private Integer boardNum;   // 게시글 번호
    private String boardTitle;  // 게시글 제목 (마이페이지 출력용)
    private String boardStatus; // 게시글 모집 상태 (마이페이지 출력용)

    private String memberId;    // 신청자 아이디
    private String memberName;  // 신청자 이름 (관리자 화면용)

    private String role;        // 역할 (LEADER/MEMBER)
    private String status;      // 신청 상태 (PENDING/JOINED/REJECTED)
    private LocalDateTime joinedDate; // 신청일
}