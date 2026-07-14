package com.example.boot.repository;

import com.example.boot.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.boot.entity.QBoard.board;

@Slf4j
public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public BoardCustomRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> searchBoard(String type, String keyword, Pageable pageable) {
        /*
        검색
        * select * from board  서치를 안한 케이스
        * where title like '%aaa%' 값이 1개이면
        * where title like '%aaa%' or content like '%aaa%' 값 이 2개이면
        * where title like '%aaa%' or content like '%aaa%' or writer like '%aaa%' 값 이 3개이면
        * BooleanExpression : 동적 퀴리를 사용할 때 실행여부 확인 객체 (필수)
        * */

        BooleanExpression condition = null;

        // 동적 검색 조건
        if (type != null && keyword != null){
            // 타입이 여러개 들어올 경우 배열로 처리
            String[] typeArr = type.split("");
            for (String t : typeArr){
                switch (t){
                    case "t" :
                        condition = (condition == null) ?
                                // 처음 조건이라면...
                                board.title.containsIgnoreCase(keyword) :
                                // 이전 조건이 있었다면...
                                condition.or(board.title.containsIgnoreCase(keyword));
                        break;
                    case "w" :
                        condition = (condition == null) ?
                                // 처음 조건이라면...
                                board.writer.containsIgnoreCase(keyword) :
                                // 이전 조건이 있었다면...
                                condition.or(board.writer.containsIgnoreCase(keyword));
                        break;
                    case "c" :
                        condition = (condition == null) ?
                                // 처음 조건이라면...
                                board.content.containsIgnoreCase(keyword) :
                                // 이전 조건이 있었다면...
                                condition.or(board.content.containsIgnoreCase(keyword));
                        break;
                    default: break;
                }
            }
        }
        // 쿼리 작성
        List<Board> result = jpaQueryFactory
                .selectFrom(board).where(condition)
                .orderBy(board.bno.desc())
                .offset(pageable.getOffset()) // limit 번지, 개수
                .limit(pageable.getPageSize()) // 개수
                .fetch();
        log.info("condition >> {}", condition);
        log.info("offset >> {}", pageable.getOffset());
        log.info("result >> {}", result);

        // 갬색 데이터를 반영한 결과 전체 개수 조회
        long total = jpaQueryFactory.selectFrom(board)
                .where(condition)
                .fetch()
                .size();
        return new PageImpl<>(result, pageable, total);
    }
}
