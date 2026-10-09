package com.example.service;

import com.example.dto.UserInfoRequest;
import com.example.entity.Domain;
import com.example.entity.Skill;
import com.example.entity.User;
import com.example.entity.UserInfo;
import com.example.repository.DomainRepository;
import com.example.repository.SkillRepository;
import com.example.repository.UserInfoRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private SkillRepository skillRepository;

    public UserInfo saveUserInfo(UserInfoRequest userInfoRequest, String email) {
        validateUserInfo(userInfoRequest);

//        BASIC
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại\""));
        Domain domain = domainRepository.findById(userInfoRequest.getDomainId()).orElseThrow(()->new RuntimeException("Role không tồn tại"));
        List<Skill> list = skillRepository.findAllById(userInfoRequest.getSkillIds());

        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setSalaryMax(userInfoRequest.getSalaryMax());
        userInfo.setSalaryMin(userInfoRequest.getSalaryMin());
        userInfo.setSalaryNegotiable(userInfoRequest.isSalaryNegotiable());
        userInfo.setCity(userInfoRequest.getCity());
        userInfo.setWorkingType(userInfoRequest.getWorkingType());
        userInfo.setExperienceMax(userInfoRequest.getExperienceMax());
        userInfo.setExperienceMin(userInfoRequest.getExperienceMin());
        userInfo.setImage(userInfoRequest.getImage());
        userInfo.setDomain(domain);
        userInfo.setUsername(userInfoRequest.getUsername());
        userInfo.setSkills(new HashSet<>(list));

        return userInfoRepository.save(userInfo);
    }

    public UserInfo updateUserInfo(UserInfoRequest userInfoRequest, String email) {
        validateUserInfo(userInfoRequest);

        //        BASIC
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại\""));
        Domain domain = domainRepository.findById(userInfoRequest.getDomainId()).orElseThrow(()->new RuntimeException("Role không tồn tại"));
        List<Skill> list = skillRepository.findAllById(userInfoRequest.getSkillIds());

        UserInfo userInfo = user.getUserInfo();
        userInfo.setSalaryMax(userInfoRequest.getSalaryMax());
        userInfo.setSalaryMin(userInfoRequest.getSalaryMin());
        userInfo.setSalaryNegotiable(userInfoRequest.isSalaryNegotiable());
        userInfo.setCity(userInfoRequest.getCity());
        userInfo.setWorkingType(userInfoRequest.getWorkingType());
        userInfo.setExperienceMax(userInfoRequest.getExperienceMax());
        userInfo.setExperienceMin(userInfoRequest.getExperienceMin());
        userInfo.setImage(userInfoRequest.getImage());
        userInfo.setDomain(domain);
        userInfo.setUsername(userInfoRequest.getUsername());
        userInfo.setSkills(new HashSet<>(list));

        return userInfoRepository.save(userInfo);
    }

    public UserInfo findUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy User"));

        return  user.getUserInfo();
    }

    public void validateUserInfo(UserInfoRequest userInfoRequest) {
        boolean isNegotiable = userInfoRequest.isSalaryNegotiable();
        if (isNegotiable) {
            if (userInfoRequest.getSalaryMin() != null || userInfoRequest.getSalaryMax() != null) {
                throw new IllegalArgumentException("Khi chọn thỏa thuận lương, không được nhập Lương tối thiểu và Lương tối đa.");
            }
        } else if (!isNegotiable) {
            if (userInfoRequest.getSalaryMin() == null || userInfoRequest.getSalaryMax() == null) {
                throw new IllegalArgumentException("Vui lòng nhập đầy đủ Lương tối thiểu và Lương tối đa.");
            }

            if (userInfoRequest.getSalaryMin() > userInfoRequest.getSalaryMax()) {
                throw new IllegalArgumentException("Lương tối thiểu không được lớn hơn Lương tối đa.");
            }
        }
        if (userInfoRequest.getExperienceMin() > userInfoRequest.getExperienceMax()) {
            throw new IllegalArgumentException("Kinh nghiệm tối thiểu không được lớn hơn kinh nghiệm tối đa.");
        }
    }
}
