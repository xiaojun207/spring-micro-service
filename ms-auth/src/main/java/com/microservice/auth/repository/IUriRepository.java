package com.microservice.auth.repository;

import com.microservice.auth.entity.Uri;
import com.microservice.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUriRepository extends JpaRepository<Uri, Long> {

    @Query("select b from Uri b where b.method = ?1 and b.uri= ?2")
    Uri findUriByMethodAndAndUri(String method, String uri);
}
