package com.microservice.auth.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "uri")
public class Uri implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "method", length = 10)
    private String method;

    @Column(name = "uri", length = 128)
    private String uri;

    @Column(name = "description", length = 128)
    private String description;

    @CreatedDate
    @Column(name = "create_date")
    private Date createDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private Date updateDate;
}
