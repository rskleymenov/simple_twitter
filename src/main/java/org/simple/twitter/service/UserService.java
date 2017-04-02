package org.simple.twitter.service;

import org.simple.twitter.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
}
