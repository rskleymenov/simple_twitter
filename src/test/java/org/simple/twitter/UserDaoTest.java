package org.simple.twitter;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simple.twitter.dao.user.UserDao;
import org.simple.twitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class UserDaoTest extends AbstractSpringRunner {
    @Autowired
    @Qualifier("SQL")
    private UserDao userDao;
    
    private final User testUser = new User(0l, "ameks", "12345", true);

    @Before
    @After
    public void clearTableData() {
        userDao.findAll().forEach(user -> userDao.delete(user.getId()));
    }
    
    @Test
    public void testCreate() {
        System.out.println("!!!!!!! Hello, It's testCreate !!!!!!!");
        userDao.create(testUser);
        Assert.assertTrue(userDao.findAll().size() == 1);
    }

    @Test
    public void testRead() {
        System.out.println("!!!!!!! Hello, It's testRead !!!!!!!");
        userDao.create(testUser);
        User readUser = userDao.read(testUser.getId());
        Assert.assertNotNull(readUser);
        Assert.assertEquals(testUser.getLogin(), readUser.getLogin());
    }

    @Test
    public void testDelete() {
        System.out.println("!!!!!!! Hello, It's testDelete !!!!!!!");
        userDao.create(testUser);
        boolean wasDeleted = userDao.delete(testUser.getId());
        Assert.assertTrue(wasDeleted);
        Assert.assertTrue(userDao.findAll().size() == 0);
    }

    @Test
    public void testFindAll() {
        System.out.println("!!!!!!! Hello, It's testFindAll !!!!!!!");
        userDao.create(testUser);
        Assert.assertTrue(userDao.findAll().size() == 1);
    }

    @Test
    public void testUpdate() {
        System.out.println("!!!!!!! Hello, It's testUpdate !!!!!!!");
        final String newPass = "1111";
        userDao.create(testUser);
        testUser.setPassword(newPass);
        userDao.update(testUser);
        User readUser = userDao.read(testUser.getId());
        Assert.assertNotNull(readUser);
        Assert.assertEquals(readUser.getPassword(), newPass);
    }
    
    
}
