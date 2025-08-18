--liquibase formatted sql

--changeset re1kur:1
INSERT INTO makes (name)
VALUES
  ('TOYOTA'),
  ('TESLA'),
  ('BMW');

--changeset re1kur:2
INSERT INTO make_information (make_id, country, description, founded_at, founder, owner)
VALUES
  ((SELECT id FROM makes WHERE name = 'TOYOTA'), 'Japan', 'Leading Japanese automaker', '1937-08-28', 'Kiichiro Toyoda', 'Toyota Motor Corporation'),
  ((SELECT id FROM makes WHERE name = 'TESLA'), 'USA', 'Electric vehicle manufacturer based in California', '2003-07-01', 'Elon Musk', 'Tesla Inc.'),
  ((SELECT id FROM makes WHERE name = 'BMW'), 'Germany', 'Luxury vehicle and motorcycle manufacturer', '1916-03-07', 'Franz Josef Popp', 'BMW Group');

--changeset re1kur:3
INSERT INTO car_types (name)
VALUES
  ('SEDAN'),
  ('SUV'),
  ('COUPE');

--changeset re1kur:4
INSERT INTO engines (name)
VALUES
  ('Electric'),
  ('V6'),
  ('Hybrid');

--changeset re1kur:5
INSERT INTO cars (make_id, car_type_id, engine_id, model, year, license_plate, is_available)
VALUES
  ((SELECT id FROM makes WHERE name = 'TOYOTA'), (SELECT id FROM car_types WHERE name = 'SEDAN'), (SELECT id FROM engines WHERE name = 'V6'), 'Camry', 2020, 'A123BC', true),
  ((SELECT id FROM makes WHERE name = 'TESLA'), (SELECT id FROM car_types WHERE name = 'SUV'), (SELECT id FROM engines WHERE name = 'Electric'), 'Model X', 2022, 'B456CD', true),
  ((SELECT id FROM makes WHERE name = 'BMW'), (SELECT id FROM car_types WHERE name = 'COUPE'), (SELECT id FROM engines WHERE name = 'Hybrid'), 'M4', 2021, 'C789EF', false);

--changeset re1kur:6
INSERT INTO car_information (car_id, description, color, seats, mileage, fuel_type, transmission)
VALUES
  ((SELECT id FROM cars WHERE license_plate = 'A123BC'), 'Comfortable and fuel-efficient mid-size sedan.', 'White', 5, 15000, 'Gasoline', 'Automatic'),
  ((SELECT id FROM cars WHERE license_plate = 'B456CD'), 'Luxury electric SUV with Falcon Wing doors.', 'Black', 7, 8000, 'Electric', 'Automatic'),
  ((SELECT id FROM cars WHERE license_plate = 'C789EF'), 'High-performance sports coupe.', 'Blue', 4, 12000, 'Hybrid', 'Manual');

