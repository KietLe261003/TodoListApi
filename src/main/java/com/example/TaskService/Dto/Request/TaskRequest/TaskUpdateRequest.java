package com.example.TaskService.Dto.Request.TaskRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateRequest {
    private String description;
    private String status;
}
