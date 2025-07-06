--liquibase formatted sql

--changeset re1kur:2
INSERT INTO makes (name) VALUES ('Toyota');
INSERT INTO makes (name) VALUES ('BMW');
INSERT INTO makes (name) VALUES ('Tesla');

--changeset re1kur:3
INSERT INTO make_information (make_id, country, description, founded_at, founder, owner)
VALUES
    (1, 'Japan', 'Japanese automotive manufacturer', 1937, 'Kiichiro Toyoda', 'Toyota Group'),
    (2, 'Germany', 'German luxury vehicle manufacturer', 1916, 'Franz Josef Popp', 'BMW AG'),
    (3, 'USA', 'American electric car manufacturer', 2003, 'Elon Musk', 'Tesla Inc.');

--changeset re1kur:4
INSERT INTO car_types (name) VALUES ('Sedan');
INSERT INTO car_types (name) VALUES ('SUV');
INSERT INTO car_types (name) VALUES ('Hatchback');

--changeset re1kur:5
INSERT INTO engines (name) VALUES ('V6');
INSERT INTO engines (name) VALUES ('V8');
INSERT INTO engines (name) VALUES ('Electric Motor');

--changeset re1kur:6
INSERT INTO cars (make_id, car_type_id, engine_id, model, year, license_plate, is_available)
VALUES
    (1, 1, 1, 'Camry', 2020, 'A123BC', TRUE),
    (2, 2, 2, 'X5', 2021, 'B456DE', FALSE),
    (3, 1, 3, 'Model S', 2022, 'C789FG', TRUE);

--changeset re1kur:7
INSERT INTO car_details (car_id, description, color, seats, mileage, fuel_type, transmission)
VALUES
    (1, 'Comfortable family sedan', 'White', 5, 15000, 'Petrol', 'Automatic'),
    (2, 'Luxury SUV for city and offroad', 'Black', 5, 8000, 'Diesel', 'Automatic'),
    (3, 'Fully electric premium sedan', 'Red', 5, 5000, 'Electric', 'Single-speed');