package com.example.TaskService.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private int priority; // 1-5 scale
    private String status; // "TODO", "IN_PROGRESS", "COMPLETED"
    private List<String> dependsOn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
