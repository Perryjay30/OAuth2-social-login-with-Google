package com.example.oauth2sociallogin.task.service;

import com.example.oauth2sociallogin.task.data.dto.TaskRequest;
import com.example.oauth2sociallogin.task.data.model.Task;
import com.example.oauth2sociallogin.user.data.dto.response.Response;

import java.util.List;

public interface TaskService {
    Response createTask(String emailAddress, TaskRequest request);

    List<Task> getAllTasks(String emailAddress);

    Response updateTask(Long taskId, String emailAddress, TaskRequest request);

    Response deleteTask(Long taskId, String emailAddress);
}
