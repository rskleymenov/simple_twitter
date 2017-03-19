package org.simple.twitter.controller;

import org.simple.twitter.dao.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Profile("SQL")
@Controller
public class UserController {
    @Autowired
    private UserDao userDao;
    
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ModelAndView getAllUsers() {
        return new ModelAndView("userView", "users", userDao.findAll());
    }
}
