package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.model.Role;
import ru.otus.service.AuthenticatedUserInfoService;
import ru.otus.service.TaskService;
import ru.otus.service.UserService;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@Controller
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    private final UserService userService;

    private final TaskService taskService;

    private final AuthenticatedUserInfoService authenticatedUserInfoService;

    public WebController(UserService userService, TaskService taskService, AuthenticatedUserInfoService authenticatedUserInfoService) {
        this.userService = userService;
        this.taskService = taskService;
        this.authenticatedUserInfoService = authenticatedUserInfoService;
    }

    @GetMapping("/")
    public RedirectView mainPage() {
        var user = authenticatedUserInfoService.getAuthenticatedUser();
        if (user.getRole() == Role.ROLE_ADMIN) {
            return new RedirectView("/users", true);
        }
        return new RedirectView("/tasks", true);
    }

    @GetMapping("/tasks")
    public String tasksView(Model model, final HttpServletResponse response) {
        model.addAttribute("tasks", taskService.getAuthorized());
        model.addAttribute("ROLE_MANAGER", Role.ROLE_MANAGER);
        model.addAttribute("allPerformers", userService.getByRole(Role.ROLE_PERFORMER));
        addUserInfo(model);
        return "tasks.html";
    }

    @GetMapping("/users")
    public String usersView(Model model) {
        model.addAttribute("users", userService.getAll());
        addUserInfo(model);
        return "users.html";
    }

    private void addUserInfo(Model model) {
        model.addAttribute("me", authenticatedUserInfoService.getAuthenticatedUser());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private String handleMessageException(Exception e) {
        logger.error("Exception: {}", e.getMessage());
        return "Oops! Something went wrong. Error: " + e.getClass().getSimpleName();
    }
}
