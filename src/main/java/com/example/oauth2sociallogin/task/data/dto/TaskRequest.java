package com.example.oauth2sociallogin.task.data.dto;

import lombok.Data;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private String taskPriority;
}
