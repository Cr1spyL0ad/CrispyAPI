package com.crispy.crispyapi.service;

public interface ServiceInterface<O> {
    boolean create(O object);
    O read(Long id) throws Exception;
    boolean update(O object);
    boolean delete(Long id) throws Exception;
}
