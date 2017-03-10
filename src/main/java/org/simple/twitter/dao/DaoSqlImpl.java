package org.simple.twitter.dao;

import com.mysql.jdbc.Statement;
import org.simple.twitter.model.ModelEntity;
import org.simple.twitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.Reflection;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DaoSqlImpl<EntityType extends ModelEntity, ID> implements Dao<EntityType, ID> {

    @Autowired
    private DataSource dataSource;
    
    protected Class<EntityType> entityClass;

    public DaoSqlImpl(Class<EntityType> entityClass) {
        this.entityClass = entityClass;
    }
    
    @Override
    public List<EntityType> findAll() {
        List<EntityType> entities = new ArrayList<EntityType>();
        try (Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(
                    SELECT_FROM + " " + entityClass.getSimpleName());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                EntityType entity = entityClass.newInstance();
                for (PropertyDescriptor field : Introspector.getBeanInfo(entityClass).getPropertyDescriptors()) {
                    if (field.getName().equals("class"))
                        continue;
                    field.getWriteMethod().invoke(entity, field.getPropertyType().cast(rs.getObject(field.getName())) );
                }
                entities.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public ID create(EntityType entity) {
        ID id = null;
        try (Connection connection = getConnection()){
            PreparedStatement preparedStatement =
                    connection.prepareStatement(getCreateSQL(entity), 
                            Statement.RETURN_GENERATED_KEYS);

            preparedStatement.executeUpdate();

            ResultSet generatedKeyRS = preparedStatement.getGeneratedKeys();
            if (generatedKeyRS.next()) {
                id = (ID) generatedKeyRS.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    
    @Override
    public EntityType read(ID id) {
        EntityType entity = null;
        ResultSet rs;
        try (Connection connection = getConnection()){
            PreparedStatement pst = connection.prepareStatement(
                    SELECT_FROM + " " + 
                            entityClass.getSimpleName() + 
                            " " + WHERE + " " + 
                            EntityType.ID + " = ?");
            pst.setString(1, id.toString());
            rs = pst.executeQuery();
            
            if (rs.next()) {
                entity = entityClass.newInstance();
                for (PropertyDescriptor field : Introspector.getBeanInfo(entityClass).getPropertyDescriptors()) {
                    if (field.getName().equals("class"))
                        continue;
                    field.getWriteMethod().invoke(entity, field.getPropertyType().cast(rs.getObject(field.getName())) );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public void update(EntityType entity) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    UPDATE + " " + entityClass.getSimpleName() + " " +
                    SET + " " +
                    getUpdateFieldsAndValues(entity) + " " +
                    WHERE + " " + 
                    EntityType.ID + "=" + entity.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(ID id) {
        EntityType entity = null;
        ResultSet rs;
        try (Connection connection = getConnection()){
            PreparedStatement pst = connection.prepareStatement(
                    DELETE + " " + entityClass.getSimpleName() + " " + WHERE + " " + EntityType.ID
                            + "=?"
            );
            pst.setString(1, id.toString());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private String getCreateSQL(EntityType entity) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        // Get field's names
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (PropertyDescriptor field : Introspector.getBeanInfo(entityClass).getPropertyDescriptors()) {
            if (field.getName().equals("class"))
                continue;
            fields
                    .append(field.getName())
                    .append(",");
            values
                    .append("'")
                    .append(field.getReadMethod().invoke(entity))
                    .append("'")
                    .append(",");
        }
        fields.deleteCharAt(fields.lastIndexOf(","));
        values.deleteCharAt(values.lastIndexOf(","));

        return  INSERT_INTO + 
                " " +
                entityClass.getSimpleName() +
                "(" + fields.toString() + ") " +
                VALUES +
                "(" + values.toString() + ")";
    }
    
    private String getUpdateFieldsAndValues(EntityType entity) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        // Get field's names
        StringBuilder fieldsAndValues = new StringBuilder();
        for (PropertyDescriptor field : Introspector.getBeanInfo(entityClass).getPropertyDescriptors()) {
            if (field.getName().equals("class"))
                continue;
            fieldsAndValues
                    .append(field.getName())
                    .append("=")
                    .append("'")
                    .append(field.getReadMethod().invoke(entity))
                    .append("'")
                    .append(",");
        }
        fieldsAndValues.deleteCharAt(fieldsAndValues.lastIndexOf(","));
        return fieldsAndValues.toString();
    }

    /// CONSTANTS
    private static final String INSERT_INTO = "insert into";
    private static final String UPDATE = "update";
    private static final String SET = "set";
    private static final String SELECT_FROM = "select * FROM";
    private static final String WHERE = "where";
    private static final String VALUES = "values";
    private static final String DELETE = "delete";
    
    
}
