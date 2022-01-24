package com.microservice.auth.repository;

import com.microservice.auth.entity.RoleUri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleUriRepository extends JpaRepository<RoleUri, Long> {

}
