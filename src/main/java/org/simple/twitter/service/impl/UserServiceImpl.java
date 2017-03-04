package org.simple.twitter.service.impl;

import org.simple.twitter.dao.UserDao;
import org.simple.twitter.model.User;
import org.simple.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("SQL")
    private UserDao userDao;

    @Override
    public List<User> getAllStoredUsers() {
        return userDao.findAll();
    }

    @Override
    public void saveUser(User user) {
        user.setCreationDate(new Timestamp(new Date().getTime()));
        userDao.createUser(user);
    }
}
