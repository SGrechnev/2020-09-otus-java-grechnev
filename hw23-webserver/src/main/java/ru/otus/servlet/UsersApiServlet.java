package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUser;

import java.io.BufferedReader;
import java.io.IOException;

public class UsersApiServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(UsersApiServlet.class);

    private final DbServiceUser dbServiceUser;
    private final Gson gson;

    public UsersApiServlet(DbServiceUser dbServiceUser, Gson gson) {
        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        User user = dbServiceUser.getUser(login).orElse(null);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=UTF-8");
        BufferedReader reader = request.getReader();
        User user = gson.fromJson(reader, User.class);
        logger.info("Request to create user {}", user.getLogin());

        if (user.getLogin().equals("")) {
            printErrorToResponse(response, "Empty login is forbidden");
            return;
        }

        if (dbServiceUser.getUser(user.getLogin()).isPresent()) {
            printErrorToResponse(response, "User " + user.getLogin() + " exists");
            return;
        }

        dbServiceUser.saveUser(user);
        logger.info("User created: {}", user);
        printSuccessToResponse(response, "User " + user.getLogin() + " created");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json;charset=UTF-8");
        BufferedReader reader = request.getReader();
        User user = gson.fromJson(reader, User.class);
        logger.info("Request to change user {}", user.getLogin());

        if (dbServiceUser.getUser(user.getLogin()).isEmpty()) {
            printErrorToResponse(response, "User " + user.getLogin() + " not exists");
            return;
        }

        dbServiceUser.saveUser(user);
        logger.info("User changed: {}", user);
        printSuccessToResponse(response, "User " + user.getLogin() + " changed");
    }

    private void printSuccessToResponse(HttpServletResponse response, String msg) throws IOException {
        printToResponse(response, HttpServletResponse.SC_OK, msg, ApiResponse.Status.SUCCESS);
    }

    private void printErrorToResponse(HttpServletResponse response, String msg) throws IOException {
        printToResponse(response, HttpServletResponse.SC_BAD_REQUEST, msg, ApiResponse.Status.ERROR);
    }

    private void printToResponse(HttpServletResponse response, int status, String msg, ApiResponse.Status apiStatus)
            throws IOException {
        response.setStatus(status);
        var apiResponse = new ApiResponse(apiStatus, msg);
        response.getOutputStream().print(gson.toJson(apiResponse, ApiResponse.class));
    }
}
