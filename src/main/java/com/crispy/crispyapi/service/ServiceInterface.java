package com.crispy.crispyapi.service;

import java.util.List;

public interface ServiceInterface<O> {
    boolean create(O object);
    O read(Long id) throws Exception;
    List<O> readAll();
    boolean update(O object, Long id);
    boolean delete(Long id);
    boolean deleteAll();
}
