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

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại\""));
        Domain domain = domainRepository.findById(userInfoRequest.getDomainId()).orElseThrow(()->new RuntimeException("Role không tồn tại"));

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

        return userInfoRepository.save(userInfo);
    }

    public UserInfo updateUserInfo(UserInfoRequest userInfoRequest, String email) {
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

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại\""));
        Domain domain = domainRepository.findById(userInfoRequest.getDomainId()).orElseThrow(()->new RuntimeException("Role không tồn tại"));

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
