package net.datasa.community.service;

import lombok.RequiredArgsConstructor;
import net.datasa.community.entity.Group;
import net.datasa.community.entity.Member;
import net.datasa.community.entity.Board;
import net.datasa.community.repository.BoardRepository;
import net.datasa.community.repository.GroupRepository;
import net.datasa.community.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 목록 조회 (카테고리 필터링) [cite: 74, 80]
     */
    public List<Board> getList(String category) {
        if (category == null || category.isEmpty() || category.equals("ALL")) {
            return boardRepository.findAllByOrderByCreateDateDesc();
        } else {
            return boardRepository.findByCategoryOrderByCreateDateDesc(category);
        }
    }

    /**
     * 게시글 작성 + 작성자 자동 참여(LEADER) 처리 [cite: 112, 156]
     */
    public void write(Board board, String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        // 1. 게시글 저장
        board.setMember(member);
        board.setStatus("OPEN");
        board.setHeadcnt(1); // 리더 포함 1명부터 시작 (명세서 문맥상 리더도 인원에 포함인지 확인 필요, 보통 포함)
        Board savedBoard = boardRepository.save(board);

        // 2. 작성자를 그룹 리더로 자동 등록 (JOINED 상태)
        Group group = Group.builder()
                .board(savedBoard)
                .member(member)
                .role("LEADER")
                .status("JOINED")
                .build();

        groupRepository.save(group);
    }

    /**
     * 게시글 상세보기
     */
    public Board read(Integer boardNum) {
        return boardRepository.findById(boardNum)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
    }

    /**
     * 게시글 삭제 [cite: 182]
     */
    public void delete(Integer boardNum, String memberId) {
        Board board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        if (!board.getMember().getMemberId().equals(memberId)) {
            throw new RuntimeException("삭제 권한 없음");
        }
        boardRepository.delete(board); // Cascade로 인해 Group 데이터도 삭제됨
    }

    /**
     * 모임 신청 (PENDING)
     */
    public void apply(Integer boardNum, String memberId) {
        // 중복 신청 방지
        if (groupRepository.existsByBoard_BoardNumAndMember_MemberId(boardNum, memberId)) {
            throw new RuntimeException("이미 신청했습니다.");
        }

        Board board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        Group group = Group.builder()
                .board(board)
                .member(member)
                .role("MEMBER")
                .status("PENDING") // 기본값 대기
                .build();

        groupRepository.save(group);
    }

    /**
     * 승인/거절 처리 (핵심 로직)
     */
    public void processApplication(Integer groupId, String action) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("신청 정보 없음"));
        Board board = group.getBoard();

        if ("APPROVE".equals(action)) {
            // 승인 로직
            if (board.getHeadcnt() >= board.getCapacity()) {
                throw new RuntimeException("정원이 초과되었습니다.");
            }

            group.setStatus("JOINED");
            board.setHeadcnt(board.getHeadcnt() + 1);

            // 정원 마감 체크
            if (board.getHeadcnt() >= board.getCapacity()) {
                board.setStatus("CLOSED");
            }

        } else if ("REJECT".equals(action)) {
            // 거절 로직
            String prevStatus = group.getStatus();
            group.setStatus("REJECTED"); // [cite: 170]

            // 이미 승인되었던 사람을 거절하는 경우 인원 감소
            if ("JOINED".equals(prevStatus)) {
                board.setHeadcnt(board.getHeadcnt() - 1);
                // 인원이 줄었으므로 다시 OPEN 될 수 있음 (명세엔 없지만 논리적 추론)
                if (board.getStatus().equals("CLOSED") && board.getHeadcnt() < board.getCapacity()) {
                    board.setStatus("OPEN");
                }
            }
        }
    }

    // 게시글 상세보기에서 '나의 신청 현황'을 파악하기 위한 메서드
    public Group findMyGroup(Integer boardNum, String memberId) {
        Board board = boardRepository.findById(boardNum).orElse(null);
        if(board == null) return null;

        // 게시글에 달린 모든 신청 내역(groupList) 중에서
        // 내 아이디(memberId)와 일치하는 것을 찾아서 리턴
        return board.getGroupList().stream()
                .filter(g -> g.getMember().getMemberId().equals(memberId))
                .findFirst()
                .orElse(null); // 없으면 null 리턴
    }

    // [추가] 마이페이지용 - 내가 만든 모임 (LEADER)
    public List<Group> getMyCreatedGroups(String memberId) {
        return groupRepository.findByMember_MemberIdAndRole(memberId, "LEADER");
    }

    // [추가] 마이페이지용 - 내가 신청한 모임 (MEMBER)
    public List<Group> getMyJoinedGroups(String memberId) {
        return groupRepository.findByMember_MemberIdAndRole(memberId, "MEMBER");
    }


}