package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUser;

import java.util.List;

@Controller
public class UserController {

    private final DbServiceUser userService;

    public UserController(DbServiceUser userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/user/list"})
    public String usersListView(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "usersList.html";
    }

    @GetMapping("/user/create")
    public String userCreateView(Model model) {
        model.addAttribute("user", new User());
        return "userCreate.html";
    }

    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute User user) {
        userService.save(user);
        return new RedirectView("/", true);
    }

}
