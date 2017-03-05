package org.simple.twitter.dao.user;

import org.simple.twitter.dao.DaoImpl;
import org.simple.twitter.model.User;
import org.springframework.stereotype.Repository;

@Repository("SQL")
public class UserDaoImpl extends DaoImpl<User, Long> implements UserDao {
    public UserDaoImpl() {
        super(User.class);
    }
}
