package org.simple.twitter.dao.impl;

import com.mysql.jdbc.Statement;
import lombok.extern.log4j.Log4j;
import org.simple.twitter.dao.UserDao;
import org.simple.twitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@Log4j
@Repository(value = "SQL")
public class UserDaoSQLImpl implements UserDao {

    private static final String INSERT_USER_SQL = "insert into user(username,password,enabled,creation_date)"
                                                  + " values(?, ?, ? ,?)";
    private static final String SELECT_USER_SQL = "select id, username, password, enabled, creation_date from"
                                                  + " user where id = ?";
    private static final String SELECT_ALL_USERS_SQL = "select id, username, password, enabled, creation_date from"
                                                       + " user";
    private static final String UPDATE_USER_SQL = "update user set "
                                                  + "username = ?,"
                                                  + "password = ?,"
                                                  + "enabled = ?,"
                                                  + "creation_date = ? "
                                                  + "where id = ?";
    private static final String DELETE_USER_SQL = "delete user where id = ?";

    //User fields
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ENABLED = "enabled";
    private static final String CREATION_DATE = "creation_date";
    private static final String ID = "id";


    @Autowired
    private DataSource dataSource;

    @Override
    public String createUser(User user) {
        int generatedKey = -1;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            preparedStatement.setString(i++, user.getUsername());
            preparedStatement.setString(i++, user.getPassword());
            preparedStatement.setBoolean(i++, user.isEnabled());
            preparedStatement.setTimestamp(i++, user.getCreationDate());

            preparedStatement.executeUpdate();

            ResultSet generatedKeyRS = preparedStatement.getGeneratedKeys();
            if (generatedKeyRS.next()) {
                generatedKey = generatedKeyRS.getInt(1);
            }
            log.info("Generated key for user " + user + " is: " + generatedKey);
        } catch (SQLException e) {
            throw new RuntimeException("Can't create user: " + user, e);
        }
        return String.valueOf(generatedKey);
    }

    @Override
    public User findUser(String id) {
        User user = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_SQL);
            preparedStatement.setInt(1, Integer.valueOf(id));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(String.valueOf(rs.getInt(ID)));
                user.setUsername(rs.getString(USERNAME));
                user.setPassword(rs.getString(PASSWORD));
                user.setEnabled(rs.getBoolean(ENABLED));
                user.setCreationDate(rs.getTimestamp(CREATION_DATE));
            }
            log.info("Retrieved user is: " + user);
        } catch (SQLException e) {
            throw new RuntimeException("Can't find user by id: " + id, e);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_SQL);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(String.valueOf(rs.getInt(ID)));
                user.setUsername(rs.getString(USERNAME));
                user.setPassword(rs.getString(PASSWORD));
                user.setEnabled(rs.getBoolean(ENABLED));
                user.setCreationDate(rs.getTimestamp(CREATION_DATE));
                users.add(user);
            }
            log.info("Retrieved users are: " + users);
        } catch (SQLException e) {
            throw new RuntimeException("Can't retrieve users from db", e);
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL);
            int i = 1;
            preparedStatement.setString(i++, user.getUsername());
            preparedStatement.setString(i++, user.getPassword());
            preparedStatement.setBoolean(i++, user.isEnabled());
            preparedStatement.setTimestamp(i++, user.getCreationDate());
            preparedStatement.setInt(i++, Integer.valueOf(user.getId()));
            preparedStatement.executeUpdate();
            log.info("Updated user is: " + user);
        } catch (SQLException e) {
            throw new RuntimeException("Can't update user: " + user, e);
        }
    }

    @Override
    public void deleteUser(String userId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL);
            preparedStatement.setInt(1, Integer.valueOf(userId));
            preparedStatement.executeUpdate();
            log.info("Deleted user with id: " + userId);
        } catch (SQLException e) {
            throw new RuntimeException("Cant delete user with id: " + userId, e);
        }
    }
}
