package com.example.TaskService.Service;

import com.example.TaskService.Dto.Response.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@Slf4j
public class NotificationService {
    public void sendNotification(String taskId, String title, String message) {
        Notification notification = new Notification(taskId, title, message, LocalDateTime.now());
        log.info("Notification sent - Task ID: {}, Title: {}, Message: {}",
                notification.getTaskId(), notification.getTitle(), notification.getMessage());
    }
}
