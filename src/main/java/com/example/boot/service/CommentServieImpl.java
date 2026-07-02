package com.example.boot.service;

import com.example.boot.repository.CommentReqository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServieImpl implements CommentService{
    private final CommentReqository commentReqository;
}
