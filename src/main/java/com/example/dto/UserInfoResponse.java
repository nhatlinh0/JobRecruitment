package com.example.dto;
import com.example.entity.UserInfo;
import lombok.Data;

@Data
public class UserInfoResponse {
    private String salary;
    private String workArea;
    private String exp;
    private String image;
    private String domainName;
    private String username;

    public UserInfoResponse (UserInfo userInfo) {
        if(userInfo != null) {
            this.salary = userInfo.getSalary();
            this.workArea = userInfo.getWorkArea();
            this.exp = userInfo.getExp();
            this.image = userInfo.getImage();
            this.domainName = userInfo.getDomain().getName();
            this.username = userInfo.getUsername();
        }
    }
}
