package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.dto.TaskDto;
import ru.otus.model.Report;
import ru.otus.model.Role;
import ru.otus.model.Task;
import ru.otus.repository.TaskRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    private final UserService userService;

    private final AuthenticatedUserInfoService authenticatedUserInfoService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, AuthenticatedUserInfoService authenticatedUserInfoService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.authenticatedUserInfoService = authenticatedUserInfoService;
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> get(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public List<Task> getAuthorized() {
        List<Task> authTasks;

        var user = authenticatedUserInfoService.getAuthenticatedUser();
        logger.debug("Authenticated user: {}", user);

        if (user == null || user.getRole().equals(Role.ROLE_ADMIN)) { // админу ничего
            authTasks = new ArrayList<>();
        } else if (user.getRole().equals(Role.ROLE_MANAGER)) { // руководителю всё
            authTasks = taskRepository.findAll();
        } else {
            authTasks = taskRepository.findByPerformer(user); // исполнителю своё
        }
        authTasks.sort(Comparator.comparingLong(Task::getId));
        return authTasks;
    }

    @Override
    public Optional<Task> save(TaskDto taskDto) {
        logger.info("save taskDto: {}", taskDto);
        var creator = authenticatedUserInfoService.getAuthenticatedUser();
        var performer = userService.get(taskDto.getPerformerId());

        if (performer.isEmpty() || performer.get().getRole() != Role.ROLE_PERFORMER) {
            return Optional.empty();
        }

        var task = new Task.Builder()
                .creator(creator)
                .performer(performer.get())
                .description(taskDto.getDescription())
                .expectedResult(taskDto.getExpectedResult())
                .expectedDueDate(taskDto.getExpectedDueDate())
                .build();

        logger.info("save task: {}", task);

        return Optional.of(taskRepository.save(task));
    }

    @Override
    public void updateProgress(Long taskId, int progress) {
        var oTask = taskRepository.findById(taskId);
        if (oTask.isEmpty()) {
            logger.warn("updateProgress() was called for non-existing task");
            return;
        }
        var task = oTask.get();
        task.setProgress(progress);
        if (progress == Report.PROGRESS_MAX_VALUE) {
            task.setActualDueDate(LocalDate.now());
        }
        taskRepository.save(task);
    }
}