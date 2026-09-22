package com.example.service;

import com.example.dto.UserRequest;
import com.example.dto.UserResponse;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<UserResponse> getAllUsers() {
        List<User> listUser = userRepository.findAll();
        return listUser.stream().map(x -> new UserResponse(x)).toList();
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }
}
