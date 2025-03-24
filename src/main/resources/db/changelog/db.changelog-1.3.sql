--liquibase formatted sql

--changeset re1kur:1
ALTER TABLE car_images
ALTER COLUMN image_url TYPE text;