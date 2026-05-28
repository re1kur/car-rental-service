--liquibase formatted sql

--changeset re1kur:1
INSERT INTO files (id, media_type)
VALUES
  ('toyota.png', 'image/png'),
  ('tesla.png', 'image/png'),
  ('bmw.png', 'image/png'),
  ('audi.png', 'image/png'),
  ('camry.png', 'image/png'),
  ('corolla.png', 'image/png'),
  ('model-3.png', 'image/png'),
  ('model-x.png', 'image/png'),
  ('m4.png', 'image/png'),
  ('x5.png', 'image/png'),
  ('a4.png', 'image/png'),
  ('q7.png', 'image/png'),
  ('tt.png', 'image/png');

--changeset re1kur:2
INSERT INTO makes (name, title_image_id)
VALUES
  ('TOYOTA', 'toyota.png'),
  ('TESLA', 'tesla.png'),
  ('BMW', 'bmw.png'),
  ('AUDI', 'audi.png');

--changeset re1kur:3
INSERT INTO make_information (make_id, country, description, founded_at, founder, owner)
VALUES
  ((SELECT id FROM makes WHERE name = 'TOYOTA'), 'Japan', 'Leading Japanese automaker known for reliability.', '1937-08-28', 'Kiichiro Toyoda', 'Toyota Motor Corporation'),
  ((SELECT id FROM makes WHERE name = 'TESLA'), 'USA', 'Electric vehicle manufacturer based in California.', '2003-07-01', 'Elon Musk', 'Tesla Inc.'),
  ((SELECT id FROM makes WHERE name = 'BMW'), 'Germany', 'Luxury vehicle and motorcycle manufacturer.', '1916-03-07', 'Franz Josef Popp', 'BMW Group'),
  ((SELECT id FROM makes WHERE name = 'AUDI'), 'Germany', 'Premium German automaker, part of the Volkswagen Group.', '1909-07-16', 'August Horch', 'Volkswagen Group');

--changeset re1kur:4
INSERT INTO cars (make_id, car_type, engine, model, year, license_plate, is_available, cost, title_image_id)
VALUES
  ((SELECT id FROM makes WHERE name = 'TOYOTA'), 'SEDAN',     'V6',       'Camry',   2020, 'A123BC', true,  45, 'camry.png'),
  ((SELECT id FROM makes WHERE name = 'TOYOTA'), 'HATCHBACK', 'INLINE_4', 'Corolla', 2021, 'D012GH', true,  38, 'corolla.png'),
  ((SELECT id FROM makes WHERE name = 'TESLA'),  'SEDAN',     'ELECTRIC', 'Model 3', 2023, 'E345IJ', true,  70, 'model-3.png'),
  ((SELECT id FROM makes WHERE name = 'TESLA'),  'SUV',       'ELECTRIC', 'Model X', 2022, 'B456CD', true,  95, 'model-x.png'),
  ((SELECT id FROM makes WHERE name = 'BMW'),    'COUPE',     'HYBRID',   'M4',      2021, 'C789EF', false, 80, 'm4.png'),
  ((SELECT id FROM makes WHERE name = 'BMW'),    'SUV',       'V6',       'X5',      2022, 'F678KL', true,  88, 'x5.png'),
  ((SELECT id FROM makes WHERE name = 'AUDI'),   'SEDAN',     'INLINE_4', 'A4',      2021, 'G901MN', true,  55, 'a4.png'),
  ((SELECT id FROM makes WHERE name = 'AUDI'),   'SUV',       'V6',       'Q7',      2023, 'H234OP', true,  90, 'q7.png'),
  ((SELECT id FROM makes WHERE name = 'AUDI'),   'COUPE',     'HYBRID',   'TT',      2020, 'I567QR', false, 75, 'tt.png');

--changeset re1kur:5
INSERT INTO car_information (car_id, description, color, seats, mileage, fuel_type, transmission)
VALUES
  ((SELECT id FROM cars WHERE license_plate = 'A123BC'), 'Comfortable and fuel-efficient mid-size sedan.', 'White', 5, 15000, 'Gasoline', 'Automatic'),
  ((SELECT id FROM cars WHERE license_plate = 'D012GH'), 'Compact and economical hatchback for the city.', 'Silver', 5, 22000, 'Gasoline', 'Automatic'),
  ((SELECT id FROM cars WHERE license_plate = 'E345IJ'), 'Sporty all-electric sedan with long range.', 'Red', 5, 6000, 'Electric', 'Automatic'),
  ((SELECT id FROM cars WHERE license_plate = 'B456CD'), 'Luxury electric SUV with Falcon Wing doors.', 'Black', 7, 8000, 'Electric', 'Automatic'),
  ((SELECT id FROM cars WHERE license_plate = 'C789EF'), 'High-performance sports coupe.', 'Blue', 4, 12000, 'Hybrid', 'Manual'),
  ((SELECT id FROM cars WHERE license_plate = 'F678KL'), 'Spacious premium SUV for the whole family.', 'Grey', 5, 18000, 'Gasoline', 'Automatic'),
  ((SELECT id FROM cars WHERE license_plate = 'G901MN'), 'Elegant and refined executive sedan.', 'White', 5, 14000, 'Gasoline', 'Automatic'),
  ((SELECT id FROM cars WHERE license_plate = 'H234OP'), 'Large luxury SUV with seven seats.', 'Black', 7, 9000, 'Gasoline', 'Automatic'),
  ((SELECT id FROM cars WHERE license_plate = 'I567QR'), 'Stylish compact sports coupe.', 'Yellow', 2, 16000, 'Hybrid', 'Manual');

--changeset re1kur:6
INSERT INTO cars (make_id, car_type, engine, model, year, license_plate, is_available, cost, title_image_id)
VALUES
  ((SELECT id FROM makes WHERE name = 'TOYOTA'), 'SEDAN',     'V6',       'Camry',   2019, 'J890ST', true, 42, 'camry.png'),
  ((SELECT id FROM makes WHERE name = 'TOYOTA'), 'HATCHBACK', 'INLINE_4', 'Corolla', 2022, 'K123UV', true, 40, 'corolla.png'),
  ((SELECT id FROM makes WHERE name = 'TESLA'),  'SEDAN',     'ELECTRIC', 'Model 3', 2024, 'L456WX', true, 75, 'model-3.png'),
  ((SELECT id FROM makes WHERE name = 'TESLA'),  'SUV',       'ELECTRIC', 'Model X', 2021, 'M789YZ', true, 92, 'model-x.png'),
  ((SELECT id FROM makes WHERE name = 'BMW'),    'SUV',       'V6',       'X5',      2023, 'N012AB', true, 90, 'x5.png'),
  ((SELECT id FROM makes WHERE name = 'AUDI'),   'SEDAN',     'INLINE_4', 'A4',      2022, 'O345CD', true, 57, 'a4.png'),
  ((SELECT id FROM makes WHERE name = 'AUDI'),   'SUV',       'V6',       'Q7',      2021, 'P678EF', true, 85, 'q7.png'),
  ((SELECT id FROM makes WHERE name = 'BMW'),    'COUPE',     'HYBRID',   'M4',      2023, 'Q901GH', true, 82, 'm4.png'),
  ((SELECT id FROM makes WHERE name = 'AUDI'),   'COUPE',     'HYBRID',   'TT',      2022, 'R234IJ', true, 78, 'tt.png');
