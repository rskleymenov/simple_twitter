package org.simple.twitter.dao.user;

import org.simple.twitter.dao.DaoNoSqlImpl;
import org.simple.twitter.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository("NoSQL")
@Profile("NoSQL")
public class UserDaoNoSqlImpl extends DaoNoSqlImpl<User, Long> implements UserDao {
    public UserDaoNoSqlImpl() {
        super(User.class);
    }
}
