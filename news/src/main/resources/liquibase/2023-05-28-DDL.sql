--liquibase formatted sql

--changeSet sasha:1
CREATE SCHEMA IF NOT EXISTS final_task;

--changeSet sasha:2
CREATE TABLE final_task.news
(
    id    BIGSERIAL PRIMARY KEY,
    time  TIMESTAMP    NOT NULL,
    title VARCHAR(200) NOT NULL,
    text  TEXT         NOT NULL,
    create_by VARCHAR(50)
);

--changeSet sasha:3
CREATE TABLE final_task.comment
(
    id        BIGSERIAL PRIMARY KEY,
    time      TIMESTAMP    NOT NULL,
    text      VARCHAR(200) NOT NULL,
    username VARCHAR(50)  NOT NULL,
    news_id   BIGINT REFERENCES final_task.news (id)
);