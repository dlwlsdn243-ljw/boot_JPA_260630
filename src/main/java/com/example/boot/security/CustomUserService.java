package com.example.boot.security;

import com.example.boot.entity.User;
import com.example.boot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class CustomUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // security context 객체가 username을 주고 해당 객체의 실제 값을 SB에서 가져와 userDetails객체로 리턴
        // userDetails => username, password, 권한

        User user = userRepository
                .findByEmailWithAuth(username)
                .orElseThrow(()-> new EntityNotFoundException("user not fount :" + username));
        log.info(">>> login User >>{}", user);
        return new CustomAuthUser(user);

    }
}