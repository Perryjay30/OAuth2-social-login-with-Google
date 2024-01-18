package com.example.oauth2sociallogin.task.controller;

import com.example.oauth2sociallogin.task.data.dto.TaskRequest;
import com.example.oauth2sociallogin.task.data.model.Task;
import com.example.oauth2sociallogin.task.service.TaskService;
import com.example.oauth2sociallogin.user.data.dto.response.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"api/v1/task"})
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping({"/createTask/{emailAddress}"})
    @PreAuthorize("hasAuthority('USER')")
    public Response createTask(@PathVariable String emailAddress, @RequestBody TaskRequest taskRequest) {
        return taskService.createTask(emailAddress, taskRequest);
    }

    @GetMapping({"/getTasks/{emailAddress}"})
    @PreAuthorize("hasAuthority('USER')")
    public List<Task> getAllTasks(@PathVariable String emailAddress) {
        return taskService.getAllTasks(emailAddress);
    }

    @PutMapping({"/updateTask/{taskId}/{emailAddress}"})
    @PreAuthorize("hasAuthority('USER')")
    public Response updateTask(@PathVariable Long taskId, @PathVariable String emailAddress, @RequestBody TaskRequest taskRequest) {
        return this.taskService.updateTask(taskId, emailAddress, taskRequest);
    }

    @DeleteMapping({"/deleteTask/{taskId}/{emailAddress}"})
    @PreAuthorize("hasAuthority('USER, ADMIN, SUPER_ADMIN')")
    public Response deleteTask(@PathVariable Long taskId, @PathVariable String emailAddress) {
        return this.taskService.deleteTask(taskId, emailAddress);
    }
}
