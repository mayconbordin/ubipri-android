package com.gppdi.ubipri.data.dao;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * @author mayconbordin
 */
public class AbstractDAO<T extends Model> {
    protected final Class<T> typeClass;

    public AbstractDAO(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    public void delete(Long id) {
        Model.delete(typeClass, id);
    }

    public void delete(T entity) {
        entity.delete();
    }

    public T createOrUpdate(T entity) {
        entity.save();
        return entity;
    }

    public boolean exists(Long id) {
        return new Select().from(typeClass).where("id = ?", id).exists();
    }

    public T find(Long id) {
        return Model.load(typeClass, id);
    }

    public T findByField(String field, String value) {
        return findByField(field, "=", value);
    }

    public T findByField(String field, String operator, String value) {
        String q = String.format("%s %s ?", field, operator);
        return new Select().from(typeClass).where(q, value).executeSingle();
    }

    public List<T> findAll() {
        return new Select().from(typeClass).execute();
    }

    public List<T> findAllByField(String field, String value) {
        return findAllByField(field, "=", value);
    }

    public List<T> findAllByField(String field, String operator, String value) {
        String q = String.format("%s %s ?", field, operator);
        return new Select().from(typeClass).where(q, value).execute();
    }

    public int count() {
        return new Select().from(typeClass).count();
    }


}
