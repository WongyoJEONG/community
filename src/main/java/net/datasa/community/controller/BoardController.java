package net.datasa.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.community.entity.Board;
import net.datasa.community.entity.Group;
import net.datasa.community.security.AuthenticatedUser; // [중요] 우리가 만든 UserDetails
import net.datasa.community.service.BoardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 1. 게시판 전체 페이지 이동 (화면 자체를 로딩)
    @GetMapping("/list")
    public String list(@RequestParam(value = "category", defaultValue = "ALL") String category,
                       Model model) {
        List<Board> list = boardService.getList(category);
        model.addAttribute("list", list);
        return "board/list"; // templates/board/list.html
    }

    // 2. [Ajax용] 게시글 목록 조각만 리턴 (카테고리 선택 시 호출됨)
    @GetMapping("/list-ajax")
    public String listAjax(@RequestParam(value = "category", defaultValue = "ALL") String category,
                           Model model) {
        List<Board> list = boardService.getList(category);
        model.addAttribute("list", list);
        // 전체 html이 아니라 테이블 조각(Fragment)만 교체
        return "board/listFragment :: boardListFragment";
    }

    // 3. 글쓰기 폼 이동
    @GetMapping("/writeForm")
    public String writeForm() {
        return "board/writeForm";
    }

    // 4. 글쓰기 처리
    @PostMapping("/write")
    public String write(@ModelAttribute Board board,
                        @AuthenticationPrincipal AuthenticatedUser user) {

        boardService.write(board, user.getId());

        // [수정] 글 쓴 뒤에는 홈(/)이 아니라 게시판 목록(/board/list)으로 가야 확인 가능
        return "redirect:/board/list";
    }

    // 5. 글 상세보기 (핵심 기능)
    @GetMapping("/read")
    public String read(@RequestParam("boardNum") Integer boardNum,
                       Model model,
                       @AuthenticationPrincipal AuthenticatedUser user) {
        try {
            // 게시글 정보
            Board board = boardService.read(boardNum);
            model.addAttribute("board", board);

            // [중요] '내가 이 모임에 신청했는지' 상태 정보 (PENDING, JOINED 등)
            // 이게 있어야 화면에서 "신청하기" 버튼을 보여줄지 "승인대기"를 보여줄지 결정함
            Group myGroup = boardService.findMyGroup(boardNum, user.getId());
            model.addAttribute("myGroup", myGroup);

            return "board/read";
        } catch (Exception e) {
            log.error("글 읽기 실패", e);
            return "redirect:/board/list";
        }
    }

    // 6. 글 삭제
    @GetMapping("/delete")
    public String delete(@RequestParam("boardNum") Integer boardNum,
                         @AuthenticationPrincipal AuthenticatedUser user) {
        try {
            boardService.delete(boardNum, user.getId());
        } catch (Exception e) {
            log.error("삭제 실패", e);
        }
        return "redirect:/board/list"; // 목록으로 이동
    }

    // 7. 소모임 가입 신청
    @PostMapping("/apply")
    public String apply(@RequestParam("boardNum") Integer boardNum,
                        @AuthenticationPrincipal AuthenticatedUser user) {
        try {
            boardService.apply(boardNum, user.getId());
        } catch (Exception e) {
            log.error("신청 실패", e); // 중복 신청 등의 경우
        }
        return "redirect:/board/read?boardNum=" + boardNum; // 다시 상세보기로
    }

    // 8. 승인/거절 처리 (리더 전용)
    @PostMapping("/manage")
    public String manage(@RequestParam("groupId") Integer groupId,
                         @RequestParam("action") String action, // APPROVE or REJECT
                         @RequestParam("boardNum") Integer boardNum) {

        boardService.processApplication(groupId, action);

        return "redirect:/board/read?boardNum=" + boardNum; // 처리 후 다시 상세보기로
    }
}