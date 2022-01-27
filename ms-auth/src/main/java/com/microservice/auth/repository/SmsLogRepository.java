package com.microservice.auth.repository;

import com.microservice.auth.entity.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsLogRepository extends JpaRepository<SmsLog, Long> {

}
