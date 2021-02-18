package ru.otus.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.service.DbServiceUser;
import ru.otus.services.TemplateProcessor;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UsersServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(UsersServlet.class);

    private static final String USERS_PAGE_TEMPLATE = "users.ftlh";
    private static final String TEMPLATE_ATTR_ALL_USERS = "allUsers";
    private static final String ERROR_PAGE_URL = "error.html";

    private final DbServiceUser dbServiceUser;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, DbServiceUser dbServiceUser) {
        this.templateProcessor = templateProcessor;
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        var allUsers = dbServiceUser.getAllUsers();
        paramsMap.put(TEMPLATE_ATTR_ALL_USERS, allUsers);
        response.setContentType("text/html;charset=UTF-8");
        String page;
        try {
            page = templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap);
        } catch (Exception e) {
            logger.error("Error occurred during template processing");
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", ERROR_PAGE_URL);
            return;
        }
        response.getWriter().println(page);
    }

}
