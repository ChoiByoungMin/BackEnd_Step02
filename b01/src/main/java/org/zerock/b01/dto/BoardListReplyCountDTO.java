package org.zerock.b01.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {

    // 이 2개 테이블로부터 데이터를 가져오려면 join이 필요하다.

    // Board 테이블로부터 가져온다.
    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;

    // Reply 테이블로부터 가져온다.
    private Long replyCount;
}
