package com.example.TaskService.Dto.Response;

import com.example.TaskService.Model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskDependsOn {
    Task task;
    Integer storyPoints;
}
