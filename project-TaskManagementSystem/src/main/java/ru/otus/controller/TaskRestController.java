package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.dto.TaskDto;
import ru.otus.model.Task;
import ru.otus.service.TaskService;

import java.util.List;

@RestController
public class TaskRestController {

    private static final Logger logger = LoggerFactory.getLogger(TaskRestController.class);

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @GetMapping("/api/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable(name = "id") Long id) {
        var task = taskService.get(id);
        return ResponseEntity.of(task);
    }

    @PostMapping("/api/tasks")
    public ResponseEntity<Task> saveTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.of(taskService.save(taskDto));
    }

    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<Boolean> deleteTasks(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(taskService.delete(id));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private String handleMessageException(Exception e) {
        logger.error("Exception: {}", e.getMessage());
        return "Oops! Something went wrong. Error: " + e.getClass().getSimpleName();
    }

}