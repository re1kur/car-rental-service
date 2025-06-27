--liquibase formatted sql

--changeset re1kur:1
ALTER TABLE cars
ALTER COLUMN license_plate TYPE VARCHAR(6);

ALTER TABLE car_details
ALTER COLUMN transmission TYPE VARCHAR(30);

--changeset re1kur:2
INSERT INTO makes (name, country, description)
VALUES
    ('Toyota', 'Japan', 'Japanese multinational automotive manufacturer known for its reliability and innovation in hybrid technology.'),
    ('Honda', 'Japan', 'Japanese multinational automaker renowned for its engines and motorcycles, as well as cars.'),
    ('Ford', 'USA', 'American multinational automaker with a long history of producing iconic vehicles like the Model T and the Mustang.'),
    ('Chevrolet', 'USA', 'American multinational automobile division of General Motors, famous for its wide range of vehicles including the Corvette and Silverado.'),
    ('BMW', 'Germany', 'German multinational luxury vehicle and motorcycle manufacturer known for its performance and engineering excellence.'),
    ('Mercedes-Benz', 'Germany', 'German multinational automotive manufacturer famous for its luxury vehicles and advanced engineering.'),
    ('Volkswagen', 'Germany', 'German multinational automotive manufacturer known for its compact and affordable vehicles.'),
    ('Audi', 'Germany', 'German multinational luxury vehicle manufacturer known for its design and advanced technology.'),
    ('Nissan', 'Japan', 'Japanese multinational automaker known for its affordable and reliable vehicles, including the Leaf electric car.'),
    ('Hyundai', 'South Korea', 'South Korean multinational automotive manufacturer known for its value and innovation in the global market.');

--changeset re1kur:3
INSERT INTO cars (make_id, model, year, license_plate, is_available)
VALUES
    ((SELECT id FROM makes WHERE name = 'Toyota'), 'Camry', 2022, 'ABC123', TRUE),
    ((SELECT id FROM makes WHERE name = 'Honda'), 'Civic', 2021, 'XYZ789', TRUE),
    ((SELECT id FROM makes WHERE name = 'Ford'), 'Mustang', 2020, 'DEF456', TRUE),
    ((SELECT id FROM makes WHERE name = 'BMW'), '3 Series', 2023, 'GHI789', TRUE);

--changeset re1kur:4
ALTER TABLE car_details
ADD UNIQUE (car_id);