package com.microservice.auth.service.impl;

import com.microservice.auth.service.JpaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JpaServiceImpl<T, ID> implements JpaService<T, ID> {

    JpaRepository<T, ID> repository;

    @Override
    public Page<T> getList(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id).get();
    }

    @Override
    public void add(T entity) {
        repository.save(entity);
    }

    @Override
    public void update(T entity) {
        repository.save(entity);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
