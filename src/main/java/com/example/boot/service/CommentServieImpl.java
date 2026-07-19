package com.example.boot.service;

import com.example.boot.dto.CommentDTO;
import com.example.boot.entity.Board;
import com.example.boot.entity.Comment;
import com.example.boot.repository.BoardRepository;
import com.example.boot.repository.CommentReqository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServieImpl implements CommentService{
    private final CommentReqository commentRepository;
    private final BoardRepository boardRepository;

    // 두개 이상의 명령어가 실행 될 때(두 명령중 하나라도 잘못되면 error)
    @Transactional
    @Override
    public long post(CommentDTO commentDTO) {
        // 저장 대상 => entity (commentDTO comment로 변환)
        // save()
        // 댓글이 등록되면 해당 bard의 cmt_qty update + 1
/*        Optional<Board> optional = boardRepository.findById(commentDTO.getBno());
        if (optional.isPresent()){
            Board board = optional.get();
            board.setCmtQty(board.getCmtQty()+1);
        }
*/
        // 내가 굳이 save()를 안해도 update 일어남

        Board board = boardRepository.findById(commentDTO.getBno())
                .orElseThrow(()->new EntityNotFoundException());
        board.setCmtQty(board.getCmtQty()+1);
        long cno = commentRepository.save(convertDtoToEntity(commentDTO)).getCno();
        return cno;
    }

    @Override
    public List<CommentDTO> getList(long bno) {
        // select * from comment where bno = #{bno}
        List<Comment> list = commentRepository.findByBno(bno);
        List<CommentDTO> commentDTOList = list.stream()
                .map(this::convertEntityToDto)
                .toList();
        return commentDTOList;
    }

    @Override
    public Page<CommentDTO> getList(long bno, int page) {
        // page된 값을 리턴 받으려면 pageabe값을 파라미터로 전송
        Pageable pageable = PageRequest.of(page-1, 5, Sort.by("cno").descending());
        Page<Comment> list = commentRepository.findByBno(bno, pageable);

        return list.map(this::convertEntityToDto);
    }

    // save() => id가 없으면 insert / id가 있으면 update
    // EntityNotFoundException where에서 검색한 조건의 값이 없을 경우 발생
    // 정보 유실 가능성이 커짐
    // dirty checking (변동감지)
    // findById(cno) 먼저 조회 => 영속상태를 만든 후 수정 => save


    @Transactional
    @Override
    public long modify(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getCno())
                .orElseThrow(()->new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
        comment.setContent(commentDTO.getContent());
        return comment.getCno();
    }

    @Transactional
    @Override
    public void remove(long cno) {
        Comment comment = commentRepository.findById(cno)
                .orElseThrow(()->new EntityNotFoundException());

        Board board = boardRepository.findById(comment.getBno())
                .orElseThrow(()->new EntityNotFoundException());
        board.setCmtQty(board.getCmtQty()-1);
        commentRepository.deleteById(cno);
    }





}
