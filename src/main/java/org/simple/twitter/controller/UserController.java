package org.simple.twitter.controller;

import org.simple.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAllUsers() {
        Map<String, Object> map = new HashMap();
        map.put("users", userService.findAll());
        map.put("technology", "Spring MVC");
        return new ModelAndView("userView", map);
    }
}
