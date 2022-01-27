package com.microservice.auth.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "sms_log")
public class SmsLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mobile", length = 16)
    private String mobile;

    @Column(name = "msg", length = 140)
    private String msg;

    @CreatedDate
    @Column(name = "create_date")
    private Date createDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private Date updateDate;
}
