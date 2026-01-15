package net.datasa.community.controller;

import lombok.RequiredArgsConstructor;
import net.datasa.community.entity.Member;
import net.datasa.community.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.datasa.community.service.BoardService;
import net.datasa.community.entity.Group;
import net.datasa.community.security.AuthenticatedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member") // [★핵심] 이 줄이 있어야 주소 앞에 /member가 붙습니다.
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;

    // 이제 주소는 /member/loginForm 이 됩니다.
    @GetMapping("/loginForm")
    public String loginForm() {
        return "member/loginForm";
    }

    // 주소: /member/joinForm
    @GetMapping("/joinForm")
    public String joinForm() {
        return "member/joinForm";
    }

    // 주소: /member/join
    @PostMapping("/join")
    public String join(@ModelAttribute Member member) {
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/myPage")
    public String myPage(Model model,
                         @AuthenticationPrincipal AuthenticatedUser user) {

        // 1. 내가 만든 모임 (LEADER)
        List<Group> leaderList = boardService.getMyCreatedGroups(user.getId());

        // 2. 내가 참여/신청한 모임 (MEMBER)
        List<Group> memberList = boardService.getMyJoinedGroups(user.getId());

        model.addAttribute("leaderList", leaderList);
        model.addAttribute("memberList", memberList);

        return "member/myPage";
    }
}