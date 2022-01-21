## spring-micro-service
这是一个springboot的微服务脚手架。脚手架注册中心使用nacos，网关使用spring cloud gateway。接口完成统一返回、统一异常处理等。
登录、退出、权限校验的具体实现，只包含演示代码，大家需要根据自身项目情况调整。

## 包含
* ms-rest-starter: springboot restfull接口基础配置，包括统一返回、统一异常处理、controller用户参数注入（部分实现，可根据实际调整AuthContext中内容）、
* ms-gateway: API网关，包含访问统一转发，权限控制(过滤)，网关模块独立实现统一异常处理。
* ms-auth: 用户认证，登录、退出、权限校验接口具体实现，
* ms-test: 测试模块

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
