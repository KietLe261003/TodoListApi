package com.example.TaskService.Controller;

import com.example.TaskService.Dto.Request.TaskRequest.TaskRequest;
import com.example.TaskService.Dto.Request.TaskRequest.TaskUpdateRequest;
import com.example.TaskService.Dto.Response.ApiResponse;
import com.example.TaskService.Model.Task;
import com.example.TaskService.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task/")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("OK")
                .data(taskService.getAllTasks(page, size, status))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTaskById(@PathVariable("id") String id) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("OK")
                .data(taskService.getTaskById(id))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("")
    public ResponseEntity<ApiResponse> addTask(@RequestBody TaskRequest taskRequest) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("OK")
                .data(taskService.addTask(taskRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTask(@PathVariable("id") String id, @RequestBody TaskUpdateRequest taskUpdateRequest) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("OK")
                .data(taskService.updateTask(id, taskUpdateRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable("id") String id) {
        taskService.deleteTask(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("OK")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("{id}/dependencies/{dependencyId}")
    public ResponseEntity<ApiResponse> addDependencies(@PathVariable("id") String id, @PathVariable("dependencyId") String dependencyId) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("OK")
                .data(taskService.addDependency(id, dependencyId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/getalldependsontask/{id}")
    public ResponseEntity<ApiResponse> getAllDependsOnTask(@PathVariable("id") String id) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("OK")
                .data(taskService.getAllDependencies(id))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
