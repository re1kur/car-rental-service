--liquibase formatted sql

--changeset re1kur:1
ALTER TABLE car_images
ALTER COLUMN image_url TYPE text;


--changeset re1kur:2
ALTER TABLE cars
ADD COLUMN title_image_id int;

ALTER TABLE cars
ADD FOREIGN KEY (title_image_id) REFERENCES car_images;