package com.cloudkitchens.orderdelivery.dao;

public interface BaseDao<T> {
    public int save(T object);

    public int delete(T object);

    public int update(T object);

    public T findById(String id);
}
