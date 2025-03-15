--liquibase formatted sql

--changeset re1kur:1
ALTER TABLE cars
ALTER COLUMN license_plate TYPE VARCHAR(6);

ALTER TABLE car_details
ALTER COLUMN transmission TYPE VARCHAR(30);