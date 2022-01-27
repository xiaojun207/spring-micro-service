package com.microservice.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JpaService<T, ID> {

    Page<T> getList(Pageable pageable);
    T findById(ID id);
    void add(T entity);
    void update(T entity);
    void deleteById(ID id);
}
