package com.example.dto;

import com.example.entity.Application;
import com.example.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationResponse {
    private Integer id;
    private String jobName;
    private String userName;
    private String cvUrl;
    private String coverLetter;
    private Status status;
    private String createAt;

    public ApplicationResponse(Application application) {
        this.id = application.getId();
        this.userName = application.getUser().getUsername();
        this.jobName = application.getJob().getTitle();
        this.cvUrl = application.getCvUrl();
        this.coverLetter = application.getCoverLetter();
        this.status = application.getStatus();
        this.createAt = application.getCreatedAt().toString();
    }

}
