--liquibase formatted sql

--changeset re1kur:1
CREATE TABLE IF NOT EXISTS chat_messages
(
    id          BIGSERIAL PRIMARY KEY,
    room        VARCHAR(64)              NOT NULL,
    sender_id   VARCHAR(128),
    sender_name VARCHAR(128)             NOT NULL,
    guest       BOOLEAN                  NOT NULL DEFAULT FALSE,
    text        VARCHAR(2000)            NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

--changeset re1kur:2
CREATE INDEX IF NOT EXISTS idx_chat_messages_room_created_at
    ON chat_messages (room, created_at);
