--liquibase formatted sql

--changeset re1kur:1
ALTER TABLE car_images
    ALTER COLUMN image_url TYPE text;


--changeset re1kur:2
ALTER TABLE cars
    ADD COLUMN title_image_id int;

ALTER TABLE cars
    ADD FOREIGN KEY (title_image_id) REFERENCES car_images;

--changeset re1kur:3
ALTER TABLE car_images
    ADD COLUMN expires_at TIMESTAMP;

--changeset re1kur:4
ALTER TABLE makes
    ADD COLUMN title_image_url TEXT;

--changeset re1kur:5
ALTER TABLE rentals
    ALTER COLUMN user_id TYPE VARCHAR(36),
    DROP CONSTRAINT rentals_user_id_fkey;

--changeset re1kur:6
ALTER TABLE users_roles
    DROP CONSTRAINT users_roles_user_id_fkey;

--changeset re1kur:7
ALTER TABLE users
    ALTER COLUMN id TYPE VARCHAR(36),
    ADD COLUMN email_verified BOOLEAN default false;

ALTER TABLE users
    RENAME username TO full_name;

