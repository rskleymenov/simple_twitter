package org.simple.twitter.dao.user;

import org.simple.twitter.dao.DaoSqlImpl;
import org.simple.twitter.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository("SQL")
@Profile("SQL")
public class UserDaoSqlImpl extends DaoSqlImpl<User, Long> implements UserDao {
    public UserDaoSqlImpl() {
        super(User.class);
    }
}
