package org.simple.twitter.user;

import org.simple.twitter.dao.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;

@ActiveProfiles("SQL")
public class SqlUserDaoTest extends BaseUserDaoTest {
    @Autowired
    private UserDao userDao;
    
    @PostConstruct
    public void init() {
        super.userDao = this.userDao;
    }

}
