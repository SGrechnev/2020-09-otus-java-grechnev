package ru.otus.service;

import ru.otus.dto.TaskDto;
import ru.otus.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task save(Task task);

    Optional<Task> get(Long id);

    List<Task> getAll();

    List<Task> getAuthorized();

    Optional<Task> save(TaskDto taskDto);

    boolean delete(Long id);

    void updateProgress(Long id, int progress);
}