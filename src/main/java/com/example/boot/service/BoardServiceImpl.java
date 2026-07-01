package com.example.boot.service;

import com.example.boot.dto.BoardDTO;
import com.example.boot.entity.Board;
import com.example.boot.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoardServiceImpl implements BoardService{
    private  final BoardRepository boardRepository;

    @Override
    public Long insert(BoardDTO boardDTO) {
        // CRUD에 해당하는 메서드 제공
        // save() : 저장
        // 저장하는 객체는 Entity(Board)
        Board board = convertDtoToEntity(boardDTO);
        Long bno = boardRepository.save(board).getBno(); // 저장 후 bno 가져가기
        return bno;
    }

    @Override
    public List<BoardDTO> getList() {
        // findAll() => 전체 값 리턴
        // select * from board order by bno desc
        // 정렬 : Sort.by(Sort.direction.DESC, "bno(정렬기준 칼럼명")
        // DB에서 가져오는 return List<<Board> => List<BoardDTO>로 변환
        List<Board> boardList = boardRepository.findAll(
                Sort.by(Sort.Direction.DESC, "bno"));
        List<BoardDTO> boardDTOList = boardList
                .stream()
                .map(board -> convertEntityToDot(board))
                .toList();

        return boardDTOList;
    }

    @Override
    public BoardDTO getDetail(Long bno) {
        // findOne() => 기본키를 이용하여 원하는 객체 검색 where bno
        // findBy() => 원하는 칼럼명을 이용하여 검색
        // findById = findOne

        // Optional<T> : nullPointException이 발생하지 않도록 도와줌
        // Optional.isEmpty() : null 이면 true / false
        // Optional.isPresent() : 값이 있는지 없는지 확인 true / false
        // Optional.get() : 객체 가져오기
        Optional<Board> optional = boardRepository.findById(bno);
        if (optional.isPresent()){
            Board board = optional.get();
            BoardDTO boardDTO = convertEntityToDot(board);
            // 조회수 올리기 readCount + 1
            // update board set read_count = read_count+1 where bno = bno
            // save()
            // 변경된 객체로 객체를 수정 => 저장
            board.setReadCount(board.getReadCount()+1);
            boardRepository.save(board);

            return boardDTO;
        }
        return null;
    }
}
