package com.example.boot.repository;

import com.example.boot.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

// JapReqository<entity, id type>
public interface CommentReqository extends JpaRepository<Comment, Long> {

}
