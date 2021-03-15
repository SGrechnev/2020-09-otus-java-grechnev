package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping({"/", "/user/list"})
    public String usersListView() {
        return "usersList.html";
    }

    @GetMapping("/user/save")
    public String userCreateView() {
        return "userSave.html";
    }
}
