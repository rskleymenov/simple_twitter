package org.simple.twitter.dao;


import org.simple.twitter.model.User;

import java.util.List;

public interface UserDao {
    String createUser(User user);
    User findUser(String id);
    List<User> findAll();
    void updateUser(User user);
    void deleteUser(String userId);
}
