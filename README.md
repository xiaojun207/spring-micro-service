## spring-micro-service
这是一个springboot的微服务脚手架


## 登录接口测试
```
POST http://127.0.0.1:5010/ms-auth/login

Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}
```

## User信息接口测试
```
GET http://127.0.0.1:5010/ms-auth/user/info

Header: authorization=TestToken

```
