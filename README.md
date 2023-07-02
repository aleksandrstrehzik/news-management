<h1 align="center">News management system</h1>

# Список используемых технологий

- **Spring Boot 3.1.0**
    - spring-cloud-starter-openfeign:4.0.3
    - springdoc-openapi-starter-webmvc-ui:2.1.0
    - spring-boot-starter-validation
    - spring-boot-starter-security
    - spring-boot-starter-data-jpa
    - spring-boot-starter-web
    - spring-boot-starter-test
    - spring-boot-starter-aop
    - spring-cloud-config-server
- **liquibase**
- **lombok**
- **postgresql**

# Для запуска приложения

- **exception-handler: gradle build**
- **logging-starter: gradle build**
- **config-cloud: gradle build**
- **news: gradle build**
- **security-cloud: gradle build**
- **docker compose up**

# EndPoints -

### Для работы с новостями

### http://localhost:8080

| Метод  | URL                           | Принимаемые параметры                    |
|--------|-------------------------------|------------------------------------------|
| GET    | /api/v1/news/{id}             | Long id                                  |
| DELETE | /api/v1/news/{id}             | Long id                                  |
| POST   | /api/v1/news                  |                                          |
| PATCH  | /api/v1/news                  |                                          |
| GET    | /api/v1/news                  | int size, String sort, int page          |
| GET    | /api/v1/newsWithComments/{id} | Long id, int size, String sort, int page |
| GET    | /api/v1/getByFilter/news      | int size, String sort, int page          |

### Для работы с комментариями

### http://localhost:8080

| Метод  | URL                           | Принимаемые параметры                    |
|--------|-------------------------------|------------------------------------------|
| GET    | /api/v1/comment/{id}          | Long id                                  |
| DELETE | /api/v1/comment/{id}          | Long id                                  |
| POST   | /api/v1/comment               | Long newsId                              |
| PATCH  | /api/v1/comment               |                                          |
| GET    | /api/v1/getByFilter/news      | int size, String sort, int page          |

### Для аутентификации

### http://localhost:8080

| Метод | URL       | Принимаемые параметры |
|-------|-----------|-----------------------|
| POST  | /login    |                       |
| POST  | /logout-r |                       |

### Для работы с базой хранения данных пользователя

### http://localhost:8081
| Метод | URL                 | Принимаемые параметры |
|-------|---------------------|-----------------------|
| GET   | /api/v1/user/{name} | String name           |

## Подробная документация доступна по адресу
- **http://localhost:8080/swagger-ui/index.html#/**
