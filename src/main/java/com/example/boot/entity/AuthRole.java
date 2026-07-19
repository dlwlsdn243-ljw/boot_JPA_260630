package com.example.boot.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthRole {
    // 권한을 생성시 열거형으로 만드는 것이 일반적
    USER("ROLE_USER"), // 일반회원
    ADMIN("ROLE_ADMIN"); // 관리자

    private final String roleName;
}
