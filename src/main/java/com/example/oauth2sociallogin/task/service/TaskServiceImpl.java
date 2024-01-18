package com.example.oauth2sociallogin.task.service;

import com.example.oauth2sociallogin.exceptions.CrustInterviewProjectException;
import com.example.oauth2sociallogin.task.data.dto.TaskRequest;
import com.example.oauth2sociallogin.task.data.model.Priority;
import com.example.oauth2sociallogin.task.data.model.Task;
import com.example.oauth2sociallogin.task.data.model.TaskStatus;
import com.example.oauth2sociallogin.task.repository.TaskRepository;
import com.example.oauth2sociallogin.user.data.dto.response.Response;
import com.example.oauth2sociallogin.user.data.model.User;
import com.example.oauth2sociallogin.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Response createTask(String emailAddress, TaskRequest request) {
        User existingUser = this.findExistingUser(emailAddress);
        Task task = objectMapper.convertValue(request, Task.class);
        task.setCreatedAt(LocalDateTime.now());
        task.setTaskPriority(Priority.valueOf(request.getTaskPriority()));
        task.setTaskStatus(TaskStatus.ACTIVE);
        task.setUser(existingUser);
        this.taskRepository.save(task);
        return new Response("Task created successfully!!");
    }

    public List<Task> getAllTasks(String emailAddress) {
        User existingUser = this.findExistingUser(emailAddress);
        return this.taskRepository.findAllByTaskIdAndTaskStatus(existingUser.getId(), TaskStatus.ACTIVE);
    }

    public Response updateTask(Long taskId, String emailAddress, TaskRequest request) {
        User existingUser = this.findExistingUser(emailAddress);
        Task foundTask = taskRepository.findByTaskIdAndUser(taskId, existingUser).orElseThrow(() ->
            new CrustInterviewProjectException("Task Id Does not Exist"));
        foundTask.setTitle(request.getTitle() != null && !request.getTitle().equals("") ? request.getTitle() : foundTask.getTitle());
        foundTask.setDescription(request.getDescription() != null && !request.getDescription().equals("") ? request.getDescription() : foundTask.getDescription());
        foundTask.setTaskPriority(request.getTaskPriority() != null && !request.getTaskPriority().equals("") ? Priority.valueOf(request.getTaskPriority()) : foundTask.getTaskPriority());
        foundTask.setModifiedAt(LocalDateTime.now());
        foundTask.setCompleted(true);
        this.taskRepository.save(foundTask);
        return new Response("Task updated successfully!!");
    }

    public Response deleteTask(Long taskId, String emailAddress) {
        User existingUser = this.findExistingUser(emailAddress);
        Task foundTask = taskRepository.findByTaskIdAndUser(taskId, existingUser).orElseThrow(() ->
            new CrustInterviewProjectException("Task Id Does not Exist"));
        foundTask.setDeletedAt(LocalDateTime.now());
        foundTask.setTaskStatus(TaskStatus.DELETED);
        this.taskRepository.save(foundTask);
        return new Response("Task Successfully Deleted");
    }

    private User findExistingUser(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress).orElseThrow(() ->
                new CrustInterviewProjectException("User not found!!"));
    }
}

