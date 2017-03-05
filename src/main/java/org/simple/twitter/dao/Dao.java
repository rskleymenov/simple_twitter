package org.simple.twitter.dao;

import org.simple.twitter.model.ModelEntity;

import java.io.Serializable;
import java.util.List;

public interface Dao<EntityType extends ModelEntity, ID> extends Serializable {
    List<EntityType> findAll();
    ID create(EntityType entity);
    EntityType read(ID id);
    void update(EntityType entity);
    boolean delete(ID id);
}
