-- 1. 회원 정보 테이블 (community_member)
CREATE TABLE community_member (
                                  member_id VARCHAR(20) PRIMARY KEY COMMENT '회원 아이디',
                                  member_pw VARCHAR(100) NOT NULL COMMENT '비밀번호 (암호화 저장)',
                                  name VARCHAR(20) NOT NULL COMMENT '이름/닉네임',
                                  email VARCHAR(200) COMMENT '이메일'
);

-- 2. 모집 글 테이블 (community_board)
CREATE TABLE community_board (
                                 board_num INT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 고유 번호',
                                 member_id VARCHAR(20) COMMENT '작성자 아이디',
                                 category VARCHAR(50) NOT NULL COMMENT '모임 분류 (STUDY/HEALTHY/TRIP)',
                                 title VARCHAR(200) NOT NULL COMMENT '제목',
                                 contents VARCHAR(2000) NOT NULL COMMENT '내용',
                                 headcnt INT DEFAULT 0 COMMENT '현재 승인된 인원 수',
                                 capacity INT DEFAULT 5 COMMENT '최대 정원 수',
                                 status VARCHAR(100) DEFAULT 'OPEN' COMMENT '모집 상태 (OPEN/CLOSED)',
                                 create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
                                 FOREIGN KEY (member_id) REFERENCES community_member(member_id) ON DELETE SET NULL
);

-- 3. 모임 정보 테이블 (community_group)
CREATE TABLE community_group (
                                 group_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '참여 기록 고유 번호',
                                 board_num INT COMMENT '참여 대상 모집 글 번호',
                                 member_id VARCHAR(20) COMMENT '참여 회원 아이디',
                                 role VARCHAR(20) DEFAULT 'MEMBER' COMMENT '역할 (LEADER/MEMBER)',
                                 status VARCHAR(20) DEFAULT 'PENDING' COMMENT '참여 상태 (PENDING/JOINED/REJECTED)',
                                 joined_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '신청/승인 일시',
                                 FOREIGN KEY (board_num) REFERENCES community_board(board_num) ON DELETE CASCADE,
                                 FOREIGN KEY (member_id) REFERENCES community_member(member_id) ON DELETE CASCADE
);

