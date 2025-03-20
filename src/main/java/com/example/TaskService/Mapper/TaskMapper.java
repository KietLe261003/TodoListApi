package com.example.TaskService.Mapper;

import com.example.TaskService.Dto.Request.TaskRequest.TaskRequest;
import com.example.TaskService.Dto.Request.TaskRequest.TaskUpdateRequest;
import com.example.TaskService.Model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toTask(TaskRequest taskRequest);
    void updateTask(@MappingTarget Task task, TaskUpdateRequest taskUpdateRequest);
}
