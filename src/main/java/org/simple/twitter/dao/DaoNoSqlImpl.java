package org.simple.twitter.dao;

import com.mongodb.*;
import org.simple.twitter.model.ModelEntity;
import org.springframework.beans.factory.annotation.Value;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoNoSqlImpl <EntityType extends ModelEntity, ID> implements Dao<EntityType, ID> {

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
        DBCursor cursor = getEntityCollection().find();
        while (cursor.hasNext()) {
            DBObject dbUser = cursor.next();
            try {
                entities.add(mapObjectToEntity(dbUser));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entities;
    }

    @Override
    public ID create(EntityType entity) {
        DBCollection collection = getEntityCollection();
        ID id = null;
        try {
            DBObject basicDBObject = mapEntityToObject(entity);
            id = (ID) collection.insert(basicDBObject).getField(EntityType.ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public EntityType read(ID id) {
        DBCollection collection = getEntityCollection();
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(EntityType.ID, id);
        DBCursor cursor = collection.find(searchQuery);
        if (cursor.hasNext()) {
            DBObject object = cursor.next();
            try {
                return mapObjectToEntity(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void update(EntityType entity) {
        DBCollection collection = getEntityCollection();
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
    }

    @Override
    public boolean delete(ID id) {
        DBCollection collection = getEntityCollection();
        DBObject deleteQuery = new BasicDBObject();
        deleteQuery.put(EntityType.ID, id);
        return collection.remove(deleteQuery).getError().isEmpty();
    }

    private DBCollection getEntityCollection() {
        DBCollection collection = null;
        try {
            MongoClient mongoClient = new MongoClient(localhost, Integer.valueOf(port));
            DB db = mongoClient.getDB(dbName);
            collection = db.getCollection(entityClass.getSimpleName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return collection;
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