package com.example.boot.service;

import com.example.boot.dto.CommentDTO;
import com.example.boot.dto.UserDTO;
import com.example.boot.entity.AuthRole;
import com.example.boot.entity.Comment;
import com.example.boot.entity.User;
import com.example.boot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String insert(UserDTO userDTO) {
        // pwd 암호화해서 DB에 넣기
        userDTO.setPwd(passwordEncoder.encode((userDTO.getPwd())));
        User user = convertDtoToEntity(userDTO);
        user.addAuth(AuthRole.USER);

        // cascade.ALL
        return userRepository.save(user).getEmail();
    }

    @Transactional
    @Override
    public void lastloginUpdate(String name) {
        // findById 값을 가져와서 현재 시간으로 lastlogin set => update
        User user = userRepository.findById(name)
                .orElseThrow(()->new EntityNotFoundException("해당 사용자가 없습니다."));
        // transactional dirty checking 발생 => update
        user.setLastLogin(LocalDate.now());

    }

    @Transactional
    @Override
    public void grantAdminRole(String adminEmail) {

        User user = userRepository.findById(adminEmail)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 회원입니다."));
        // 이미 abmin 권한이 있는지 확인
        boolean hasAdmin = user.getAuthList().stream()
                .anyMatch(auth-> auth.getAuth() == AuthRole.ADMIN);
        if (!hasAdmin){
            // 영속상태 이므로 dirty checking에 의해 자동 저장됨
            user.addAuth(AuthRole.ADMIN);
        }else{
            // 이미 있으면 ADMIN 제거
            user.removeAuth(AuthRole.ADMIN);
        }
    }

    @Override
    public UserDTO getDetail(String name) {
        User user = userRepository.findByEmailWithAuth(name)
                .orElseThrow(()-> new EntityNotFoundException("해당 유저가 없습니다."));
        return convertEntityToDto(user);
    }

    @Transactional
    @Override
    public String modify(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getEmail())
                .orElseThrow(()->new EntityNotFoundException("해당 유저가 없습니다."));

        log.info(">>> userDTO >> {}", userDTO);
        //pwd가 있으면 pwd를 encode 한 다음 같이 저장
        if (!userDTO.getPwd().isEmpty() && userDTO.getPwd() != null){
            String changePwd = passwordEncoder.encode(userDTO.getPwd());
            user.setPwd(changePwd);
        }
        // 없으면 nickname만 저장
        user.setNickName(userDTO.getNickName());
        log.info(">>> changeUser>> {}", user);
        return user.getEmail();
    }

    @Override
    public List<UserDTO> getList() {
        List<User> list = userRepository.findAll();
        List<UserDTO> UserDTOList = list.stream()
                .map(this::convertEntityToDto)
                .toList();
        return UserDTOList;
    }

    @Transactional
    @Override
    public String remove(String name) {
        User user = userRepository.findById(name)
                .orElseThrow(()-> new EntityNotFoundException("해당 유저가 없습니다."));
        userRepository.deleteById(name);
        return user.getEmail();
    }

    @Override
    public boolean emailCheck(String email) {
        return userRepository.existsByEmail(email);
    }
}
