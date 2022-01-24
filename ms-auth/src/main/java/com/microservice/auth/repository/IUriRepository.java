package com.microservice.auth.repository;

import com.microservice.auth.entity.Uri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUriRepository extends JpaRepository<Uri, Long> {

}
