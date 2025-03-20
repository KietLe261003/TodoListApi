package com.example.TaskService.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Notification {
    private String taskId;
    private String title;
    private String message;
    private LocalDateTime timestamp;
}