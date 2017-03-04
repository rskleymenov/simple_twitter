package org.simple.twitter.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.simple.twitter.dao.UserDao;
import org.simple.twitter.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository(value = "NoSQL")
public class UserDaoNoSQLImpl implements UserDao {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ENABLED = "enabled";
    private static final String CREATION_DATE = "creation_date";
    private static final String ID = "_id";
    private static final String USER_TABLE = "user";

    @Value("${mongodb.name}")
    private String dbName;
    @Value("${mongodb.host}")
    private String localhost;
    @Value("${mongodb.port}")
    private String port;

    @Override
    public String createUser(User user) {
        DBCollection table = getUserTable();
        BasicDBObject userDocument = new BasicDBObject();
        userDocument.put(USERNAME, user.getUsername());
        userDocument.put(PASSWORD, user.getPassword());
        userDocument.put(ENABLED, user.isEnabled());
        userDocument.put(CREATION_DATE, user.getCreationDate());
        table.insert(userDocument);
        return userDocument.get(ID).toString();
    }

    @Override
    public User findUser(String userId) {
        DBCollection table = getUserTable();
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(ID, new ObjectId(userId));
        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            DBObject dbUser = cursor.next();
            return mapToUser(dbUser);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        DBCollection table = getUserTable();
        DBCursor cursor = table.find();
        while (cursor.hasNext()) {
            DBObject dbUser = cursor.next();
            users.add(mapToUser(dbUser));
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        DBCollection table = getUserTable();
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(ID, new ObjectId(user.getId()));

        BasicDBObject fieldsForUpdate = new BasicDBObject();
        fieldsForUpdate.put(USERNAME, user.getUsername());
        fieldsForUpdate.put(PASSWORD, user.getPassword());
        fieldsForUpdate.put(ENABLED, user.isEnabled());
        fieldsForUpdate.put(CREATION_DATE, user.getCreationDate());

        BasicDBObject updatedUserDocument = new BasicDBObject();
        updatedUserDocument.append("$set", fieldsForUpdate);

        table.update(searchQuery, updatedUserDocument);


    }

    @Override
    public void deleteUser(String userId) {
        DBCollection table = getUserTable();
        BasicDBObject deleteQuery = new BasicDBObject();
        deleteQuery.put(ID, new ObjectId(userId));
        table.remove(deleteQuery);
    }

    private DBCollection getUserTable() {
        DBCollection table = null;
        try {
            MongoClient mongoClient = new MongoClient(localhost, Integer.valueOf(port));
            DB db = mongoClient.getDB(dbName);
            table = db.getCollection(USER_TABLE);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return table;
    }

    private User mapToUser(DBObject dbUser) {
        User user = new User();
        user.setId(dbUser.get(ID) == null ? null : (dbUser.get(ID)).toString());
        user.setUsername(dbUser.get(USERNAME) == null ? null : (dbUser.get(USERNAME)).toString());
        user.setPassword(dbUser.get(PASSWORD) == null ? null : (dbUser.get(PASSWORD)).toString());
        user.setEnabled(Boolean.valueOf(dbUser.get(ENABLED) == null ? null : (dbUser.get(ENABLED)).toString()));
        if (dbUser.get(CREATION_DATE) != null) {
            user.setCreationDate(new Timestamp(((Date) dbUser.get(CREATION_DATE)).getTime()));
        } else {
            user.setCreationDate(null);
        }
        return user;
    }
}
