package org.simple.twitter.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.simple.twitter.dto.UserDTO;
import org.simple.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = GET)
    public UserDTO getUser(@RequestParam int id) {
        return userService.findUser(id);
    }

    @RequestMapping(method = POST)
    public UserDTO saveUser(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }

    @RequestMapping(method = PUT)
    public UserDTO updateUser(@RequestBody UserDTO user) {
        return userService.updateUser(user);
    }

    @RequestMapping(path="/{id}", method = DELETE)
    public void updateUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
