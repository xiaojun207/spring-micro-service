package com.microservice.auth.entity;

import com.microservice.starter.model.IUser;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable, IUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "nickname", length = 32)
    private String nickName;

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "salt", length = 64)
    private String salt;

    @Column(name = "dd_openid", length = 64)
    private String ddOpenId;

    @Column(name = "wx_openid", length = 64)
    private String wxOpenId;

    @Column(name = "avatar", length = 256)
    private String avatar;

    @CreatedDate
    @Column(name = "create_date")
    private Date createDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private Date updateDate;

}
