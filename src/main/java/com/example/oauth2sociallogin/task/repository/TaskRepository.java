package com.example.oauth2sociallogin.task.repository;

import com.example.oauth2sociallogin.task.data.model.Task;
import com.example.oauth2sociallogin.task.data.model.TaskStatus;
import com.example.oauth2sociallogin.user.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTaskIdAndUser(Long taskId, User user);

    List<Task> findAllByTaskIdAndTaskStatus(long id, TaskStatus status);
}
