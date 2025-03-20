package com.example.TaskService.Dto.Request.TaskRequest;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class TaskRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private int priority; // 1-5 scale
}
