package net.datasa.community.repository;


import net.datasa.community.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    // 카테고리로 필터링해서 최신순 조회 (명세서 요구사항)
    List<Board> findByCategoryOrderByCreateDateDesc(String category);

    // 전체 목록 최신순 조회
    List<Board> findAllByOrderByCreateDateDesc();
}