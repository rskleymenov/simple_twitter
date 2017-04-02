package org.simple.twitter.controller;

import org.simple.twitter.model.User;
import org.simple.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserServlet extends HttpServlet {

    @Autowired
    private UserService userService;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        List<User> allUsers = userService.findAll();
        request.setAttribute("users", allUsers);
        request.setAttribute("technology", "Plain Servlet");
        request.getRequestDispatcher("/WEB-INF/views/jsp/userView.jsp").forward(request, response);
    }
}
