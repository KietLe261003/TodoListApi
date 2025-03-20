package com.example.TaskService.Service;

import com.example.TaskService.Base_Exception.AppException;
import com.example.TaskService.Base_Exception.ErrorCode;
import com.example.TaskService.Dto.Request.TaskRequest.TaskRequest;
import com.example.TaskService.Dto.Request.TaskRequest.TaskUpdateRequest;
import com.example.TaskService.Dto.Response.TaskDependsOn;
import com.example.TaskService.Mapper.TaskMapper;
import com.example.TaskService.Model.Task;
import com.example.TaskService.Repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TaskMapper taskMapper;

    public List<Task> getAllTasks(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size);
        if (status != null) {
            return taskRepository.findByStatus(status);
        }
        return taskRepository.findAll(pageable).getContent();
    }
    public Task getTaskById(String id) {
        return taskRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NotFoundTask));
    }
    public Task addTask(TaskRequest taskRequest) {
        Task task = taskMapper.toTask(taskRequest);
        task.setDependsOn(new ArrayList<String>());
        task.setStatus("TODO");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
    public Task updateTask(String id, TaskUpdateRequest taskUpdateRequest) {
        Task task = getTaskById(id);
        taskMapper.updateTask(task, taskUpdateRequest);
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
    public void deleteTask(String id) {
        Task taskDelete = taskRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NotFoundTask));
        List<Task> allTasks = taskRepository.findAll();
        for (Task task : allTasks) {
            if (task.getDependsOn().contains(id)) {
                task.getDependsOn().remove(id);
                task.setUpdatedAt(LocalDateTime.now());
                taskRepository.save(task);
            }
        }
        taskRepository.deleteById(id);
    }
    public Task addDependency(String taskId, String dependencyId) {
        Task task = getTaskById(taskId);
        Task dependencyTask = taskRepository.findById(dependencyId).orElseThrow(()-> new AppException(ErrorCode.NotFoundDependencyTask));
        if(task.getDependsOn().contains(dependencyId))
        {
            throw new AppException(ErrorCode.DependenciesCreated);
        }
        if(dfs(dependencyId,taskId, new HashSet<>(), new Stack<>()))
        {
            throw new AppException(ErrorCode.WrongCircularDependencies);
        }
        task.getDependsOn().add(dependencyTask.getId());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    //Using bfs to find dependcies task and create level
    public List<TaskDependsOn> getAllDependencies(String taskId) {
        List<TaskDependsOn> result = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        Task initialTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.NotFoundTask));
        TaskDependsOn initTaskDependsOn = new TaskDependsOn(initialTask,0);
        result.add(initTaskDependsOn);
        queue.addAll(initialTask.getDependsOn());
        for (String depId : initialTask.getDependsOn()) {
            Task task = taskRepository.findById(depId).orElseThrow(() -> new AppException(ErrorCode.NotFoundTask));
            TaskDependsOn taskDependsOn = new TaskDependsOn(task,1);
            result.add(taskDependsOn);
        }
        visited.add(taskId);

        while (!queue.isEmpty()) {
            String currentTaskId = queue.poll();
            if (visited.contains(currentTaskId)) {
                continue;
            }
            Task currentTask = taskRepository.findById(currentTaskId)
                    .orElseThrow(() -> new AppException(ErrorCode.NotFoundDependencyTask));
            visited.add(currentTaskId);
            Integer currentLevel = null;
            for (TaskDependsOn tdo : result) {
                if (tdo.getTask().getId().equals(currentTaskId)) {
                    currentLevel = tdo.getStoryPoints();
                    break;
                }
            }
            if (currentLevel == null) {
                continue;
            }
            for (String depId : currentTask.getDependsOn()) {
                if (!visited.contains(depId)) {
                    queue.add(depId);
                    Task task = taskRepository.findById(depId).orElseThrow(() -> new AppException(ErrorCode.NotFoundTask));
                    TaskDependsOn taskDependsOn = new TaskDependsOn(task,currentLevel+1);
                    result.add(taskDependsOn);
                }
            }
        }
        return result;
    }

    //detect circular dependencies using dfs
    private Boolean dfs(String current, String target, Set<String> visited, Stack<String> path){
        if(visited.contains(current)){
            return false;
        }
        if(current.equals(target)){
            return true;
        }
        visited.add(current);
        path.push(current);
        Task task = taskRepository.findById(current).orElse(null);
        for(String dependsOn : task.getDependsOn()){
            if(dfs(dependsOn, target, visited, path)){
                return true;
            }
        }
        while (!path.empty())
        {
            log.info(path.pop()+"-> ");
        }
        return false;
    }
    public List<Task> getUpcomingAndOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime upcomingThreshold = now.plusHours(24);

        return taskRepository.findAll().stream()
                .filter(task -> {
                    LocalDateTime dueDate = task.getDueDate();
                    return dueDate != null &&
                            ("TODO".equals(task.getStatus()) || "IN_PROGRESS".equals(task.getStatus())) &&
                            (dueDate.isBefore(now) || (dueDate.isAfter(now) && dueDate.isBefore(upcomingThreshold)));
                })
                .collect(Collectors.toList());
    }

    public void notifyUpcomingAndOverdueTasks() {
        List<Task> tasks = getUpcomingAndOverdueTasks();
        for (Task task : tasks) {
            String message = task.getDueDate().isBefore(LocalDateTime.now())
                    ? "Task hết hạn nhưng chưa hoàn thành"
                    : "Còn 24h nữa là hết hạn";
            notificationService.sendNotification(
                    task.getId(),
                    task.getTitle(),
                    message
            );
        }
    }
    @Scheduled(fixedRate = 3600000) // Mỗi giờ kiểm tra
    public void checkTask() {
        notifyUpcomingAndOverdueTasks();
    }

}
