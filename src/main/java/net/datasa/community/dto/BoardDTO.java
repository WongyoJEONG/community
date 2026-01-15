package net.datasa.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 게시글 정보 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Integer boardNum;   // 글 번호
    private String memberId;    // 작성자 아이디 (Entity -> DTO 변환 시 꺼내서 담음)
    private String memberName;  // 작성자 이름 (필요 시)
    private String category;    // 분류
    private String title;       // 제목
    private String contents;    // 내용
    private Integer headcnt;    // 현재 인원
    private Integer capacity;   // 정원
    private String status;      // 상태 (OPEN/CLOSED)
    private LocalDateTime createDate; // 작성일
}