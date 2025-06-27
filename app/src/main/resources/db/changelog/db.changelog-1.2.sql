--liquibase formatted sql

--changeset re1kur:1
INSERT INTO roles(name) VALUES ('USER'), ('ADMIN');


