package org.simple.twitter.service;

import org.simple.twitter.dao.user.UserDao;
import org.simple.twitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;
    
    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
