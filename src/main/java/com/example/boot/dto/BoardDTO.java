package com.example.boot.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BoardDTO {
    /*
    *bno long
    title string
    writer string
    content string
    readCount int - 조회수
    cmtQty - 댓들 개수
    fileQty - 파일 개수
    regDate now()
    modDate now()
    * */
    private Long bno;
    private String title;
    private String writer;
    private String content;
    private int readCount;
    private int cmtQty;
    private int fileQty;
    private LocalDateTime regDate, modDate;
}
