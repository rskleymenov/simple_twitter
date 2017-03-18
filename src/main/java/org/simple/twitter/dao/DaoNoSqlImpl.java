package org.simple.twitter.dao;

import com.mongodb.*;
import org.simple.twitter.model.ModelEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Profile("NoSQL")
public class DaoNoSqlImpl <EntityType extends ModelEntity, ID> implements Dao<EntityType, ID> {

    @Value("${mongodb.name}")
    private String dbName;
    @Value("${mongodb.host}")
    private String localhost;
    @Value("${mongodb.port}")
    private String port;

    protected Class<EntityType> entityClass;

    public DaoNoSqlImpl(Class<EntityType> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public List<EntityType> findAll() {
        List<EntityType> entities = new ArrayList<>();
        Mongo mongo = null;
        try {
            mongo = getMongo();
            DB db = mongo.getDB(dbName);
            DBCursor cursor = db.getCollection(entityClass.getSimpleName()).find();
            while (cursor.hasNext()) {
                DBObject dbUser = cursor.next();
                try {
                    entities.add(mapObjectToEntity(dbUser));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (mongo != null) {
                mongo.close();
            }
        }
        return entities;
    }

    @Override
    public void create(EntityType entity) {
        Mongo mongo = null;
        try {
            mongo = getMongo();
            DB db = mongo.getDB(dbName);
            DBCollection collection = db.getCollection(entityClass.getSimpleName());
            DBObject basicDBObject = mapEntityToObject(entity);
            collection.insert(basicDBObject);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mongo != null) {
                mongo.close();
            }
        }
    }

    @Override
    public EntityType read(ID id) {
        Mongo mongo = null;
        EntityType entityType = null;
        try {
            mongo = getMongo();
            DB db = mongo.getDB(dbName);
            DBCollection collection = db.getCollection(entityClass.getSimpleName());
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put(EntityType.ID, id);
            DBCursor cursor = collection.find(searchQuery);
            if (cursor.hasNext()) {
                DBObject object = cursor.next();
                entityType = mapObjectToEntity(object);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mongo != null) {
                mongo.close();
            }
        }
        return entityType;
    }

    @Override
    public void update(EntityType entity) {
        Mongo mongo = null;
        try {
            mongo = getMongo();
            DB db = mongo.getDB(dbName);
            DBCollection collection = db.getCollection(entityClass.getSimpleName());
            DBObject searchQuery = new BasicDBObject();
            searchQuery.put(EntityType.ID, entity.getId());

            DBObject newEntityObject = null;
            try {
                newEntityObject = mapEntityToObject(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }

            BasicDBObject updatedEntityObject = new BasicDBObject();
            updatedEntityObject.append("$set", newEntityObject);

            collection.update(searchQuery, updatedEntityObject);
        } finally {
            if (mongo != null) {
                mongo.close();
            }
        }
    }

    @Override
    public boolean delete(ID id) {
        Mongo mongo = null;
        boolean wasRemoved = false;
        try {
            mongo = getMongo();
            DB db = mongo.getDB(dbName);
            DBCollection collection = db.getCollection(entityClass.getSimpleName());
            DBObject deleteQuery = new BasicDBObject();
            deleteQuery.put(EntityType.ID, id);
            String err = collection.remove(deleteQuery).getError();
            if (err == null || err.isEmpty()) {
                wasRemoved = true;
            }
        } finally {
            if(mongo != null) {
                mongo.close();
            }
        }
        return wasRemoved;
    }

    private Mongo getMongo() {
        try {
            return new MongoClient(localhost, Integer.valueOf(port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private EntityType mapObjectToEntity(DBObject dbUser) throws IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
        EntityType entity = entityClass.newInstance();
        for (PropertyDescriptor field : Introspector.getBeanInfo(entityClass).getPropertyDescriptors()) {
            if (field.getName().equals("class"))
                continue;
            field.getWriteMethod().invoke(entity, field.getPropertyType().cast(dbUser.get(field.getName())) );
        }
        return entity;
    }

    private DBObject mapEntityToObject(EntityType entity) throws IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
        DBObject dbObject = new BasicDBObject();
        for (PropertyDescriptor field : Introspector.getBeanInfo(entityClass).getPropertyDescriptors()) {
            if (field.getName().equals("class"))
                continue;
            dbObject.put(field.getName(), field.getReadMethod().invoke(entity));
        }
        return dbObject;
    }

}