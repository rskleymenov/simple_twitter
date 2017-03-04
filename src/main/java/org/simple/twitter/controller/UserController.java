package org.simple.twitter.controller;

import org.simple.twitter.model.User;
import org.simple.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public ModelAndView indexPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("users", userService.getAllStoredUsers());
        model.addAttribute("user", new User());
        return modelAndView;
    }

    @RequestMapping(path = "/index", method = RequestMethod.POST)
    public ModelAndView saveUser(@ModelAttribute("user") User user, Model model) {
        userService.saveUser(user);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("users", userService.getAllStoredUsers());
        model.addAttribute("user", new User());
        return modelAndView;
    }
}
