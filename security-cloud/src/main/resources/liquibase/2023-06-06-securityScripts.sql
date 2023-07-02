--changeSet sasha:1
CREATE SCHEMA IF NOT EXISTS final_task;

--changeSet sasha:2
CREATE TABLE final_task.users
(
    id       BIGSERIAL PRIMARY KEY,
    role     VARCHAR(20) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(70) NOT NULL
);

--changeSet sasha:3
INSERT INTO final_task.users (role, username, password)
VALUES ('ADMIN', 'Vasia', '$2a$10$uZ/KPLUvV6u4HgOVBmLBquYc3eCFrJncn/lglB02Sgj9.WehyDePS'),
       ('SUBSCRIBER', 'Vale', '$2a$10$uZ/KPLUvV6u4HgOVBmLBquYc3eCFrJncn/lglB02Sgj9.WehyDePS'),
       ('JOURNALIST', 'Vsia', '$2a$10$uZ/KPLUvV6u4HgOVBmLBquYc3eCFrJncn/lglB02Sgj9.WehyDePS'),
       ('JOURNALIST', 'Vaia', '$2a$10$uZ/KPLUvV6u4HgOVBmLBquYc3eCFrJncn/lglB02Sgj9.WehyDePS');

