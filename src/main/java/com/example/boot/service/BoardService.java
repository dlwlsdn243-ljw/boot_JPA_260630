package com.example.boot.service;

import com.example.boot.dto.BoardDTO;
import com.example.boot.entity.Board;

import java.util.List;

public interface BoardService {
    // interface 추상메서드만 가능한 객체
    // default method : 인터페이스에서 규칙을 잡거나, 로직을 잡거나 할 때 사용
    // 호환성 유지

    // DTO (화면용) =>  Entity (DB저장용) 바꿔줘야 함
    // convert DTO -> Entity / Entity -> DTO
    // BoardDTO => board(Entity) 변환
    // BoardDTO : bno, title, content, readCount, cmtQty, fileQty, regDate, modDate
    // Board(entity) : bno, title, content, readCount, cmtQty, fileQty
    default Board convertDtoToEntity(BoardDTO boardDTO){
        return Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .writer(boardDTO.getWriter())
                .content(boardDTO.getContent())
                .readCount(boardDTO.getReadCount())
                .cmtQty(boardDTO.getCmtQty())
                .fileQty(boardDTO.getFileQty())
                .build();
    }

    /*반대 케이스 DB -> 화면*/
    default BoardDTO convertEntityToDot(Board board){
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .readCount(board.getReadCount())
                .cmtQty(board.getCmtQty())
                .fileQty(board.getFileQty())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
    }

    Long insert(BoardDTO boardDTO);

    List<BoardDTO> getList();

    BoardDTO getDetail(Long bno);
}
