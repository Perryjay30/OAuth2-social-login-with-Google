package com.example.oauth2sociallogin.task.data.model;

import com.example.oauth2sociallogin.user.data.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority taskPriority;

    private boolean completed;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "modified_at", insertable = false, updatable = false)
    private LocalDateTime modifiedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;
}
