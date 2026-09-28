package com.example.service;

import com.example.dto.UserInfoRequest;
import com.example.entity.Domain;
import com.example.entity.User;
import com.example.entity.UserInfo;
import com.example.repository.DomainRepository;
import com.example.repository.UserInfoRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private DomainRepository domainRepository;

    public UserInfo saveUserInfo(UserInfoRequest userInfoRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại\""));
        Domain domain = domainRepository.findById(userInfoRequest.getDomainId()).orElseThrow(()->new RuntimeException("Role không tồn tại"));

        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setSalary(userInfoRequest.getSalary());
        userInfo.setWorkArea(userInfoRequest.getWorkArea());
        userInfo.setExp(userInfoRequest.getExp());
        userInfo.setImage(userInfoRequest.getImage());
        userInfo.setDomain(domain);
        userInfo.setUsername(userInfoRequest.getUsername());

        return userInfoRepository.save(userInfo);
    }

    public UserInfo findUserInfo(Integer id) {
        UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy UserInfo"));
        return userInfo;
    }

    public void deleteUserInfo (UserInfo userInfo) {
        int id = userInfo.getUser().getId();
        userRepository.deleteById(id);
    }
}
