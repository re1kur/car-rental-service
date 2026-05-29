--liquibase formatted sql

--changeset re1kur:1
CREATE TABLE IF NOT EXISTS push_subscriptions
(
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(512)             NOT NULL UNIQUE,
    user_id    VARCHAR(128),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
