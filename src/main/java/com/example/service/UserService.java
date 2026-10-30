package com.example.service;

import com.example.dto.PageResponse;
import com.example.dto.UserRequest;
import com.example.dto.UserResponse;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.enums.UserStatus;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(UserRequest userRequest) {
        Role role = roleRepository.findById(userRequest.getRoleId()).orElseThrow(()->new RuntimeException("Role không tồn tại"));
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setPassword(
                passwordEncoder.encode(userRequest.getPassword())
        );
        user.setRole(role);

        return userRepository.save(user);
    }

    public User updateUser(String userName, User user) {
        user.setUsername(userName);
        return userRepository.save(user);
    }

    public User updateUserPassword(String password, User user) {
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
        return PageResponse.from(userRepository.findAll(pageable).map(UserResponse::new)) ;
    }

    public User getUserByEmail(String email) {
        System.out.println("Email nhận được: ["+ email + "]");
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy User"));
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

// ADMIN------------------------
    public User resetPassword(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User không tồn tại"));
        user.setPassword(passwordEncoder.encode("123456"));
        return userRepository.save(user);
    }

    public User setInactiveUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User không tồn tại"));
        user.setStatus(UserStatus.INACTIVE);
        return userRepository.save(user);
    }

    public User setActiveUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User không tồn tại"));
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }


}
