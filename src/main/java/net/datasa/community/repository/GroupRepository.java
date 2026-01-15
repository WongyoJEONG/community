package net.datasa.community.repository;

import net.datasa.community.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    // 특정 게시글에 달린 신청 목록 조회 (작성자가 관리할 때 필요)
    List<Group> findByBoard_BoardNum(Integer boardNum);

    // 내가 참여(승인된)한 모임 조회 (마이페이지용)
    // status가 JOINED이고 memberId가 내 아이디인 것
    List<Group> findByMember_MemberIdAndStatus(String memberId, String status);

    List<Group> findByMember_MemberIdAndRole(String memberId, String role);

    // 중복 신청 방지용 체크 (boardNum과 memberId로 조회)
    boolean existsByBoard_BoardNumAndMember_MemberId(Integer boardNum, String memberId);
}