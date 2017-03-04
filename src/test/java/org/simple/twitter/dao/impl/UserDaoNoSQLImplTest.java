package org.simple.twitter.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.simple.twitter.TestRunner;
import org.simple.twitter.dao.UserDao;
import org.simple.twitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Ignore
public class UserDaoNoSQLImplTest extends TestRunner {

    private static final String PASSWORD = "password";
    private static final String USER_NAME_1 = "userName-1";
    private static final String USER_NAME_2 = "userName-2";

    @Autowired
    @Qualifier("NoSQL")
    private UserDao userDao;

    @After
    public void clearTableData() {
        userDao.findAll().forEach(user -> userDao.deleteUser(user.getId()));
    }

    @Test
    public void shouldCreateUser() {
        String userID = userDao.createUser(createUser(USER_NAME_1));
        User user = userDao.findUser(userID);
        assertEquals(USER_NAME_1, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
        assertTrue(user.isEnabled());
    }

    @Test
    public void shouldRetrieveAllUsersFromDb() {
        userDao.createUser(createUser(USER_NAME_1));
        userDao.createUser(createUser(USER_NAME_2));

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    public void shouldDeleteUserFromDb() {
        String id = userDao.createUser(createUser(USER_NAME_1));
        userDao.deleteUser(id);
        User user = userDao.findUser(id);
        assertNull(user);
    }

    @Test
    public void shouldUpdateUserInDb() {
        String id = userDao.createUser(createUser(USER_NAME_1));
        User user = userDao.findUser(id);
        user.setUsername(USER_NAME_2);
        userDao.updateUser(user);
        user = userDao.findUser(id);
        assertEquals(USER_NAME_2, user.getUsername());
    }


    private User createUser(String userName) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(PASSWORD);
        user.setEnabled(true);
        user.setCreationDate(new Timestamp(new Date().getTime()));
        return user;
    }
}