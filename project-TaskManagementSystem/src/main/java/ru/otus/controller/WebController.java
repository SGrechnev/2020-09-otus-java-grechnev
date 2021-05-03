package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.model.Role;
import ru.otus.service.AuthenticatedUserInfoService;
import ru.otus.service.TaskService;
import ru.otus.service.UserService;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AuthenticatedUserInfoService authenticatedUserInfoService;

    @GetMapping({"/", "/tasks"})
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
}
