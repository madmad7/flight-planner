--liquibase formatted sql

--changeset mk:2
CREATE TABLE Flight (
    id INT AUTO_INCREMENT PRIMARY KEY,
    from_airport_id INT,
    to_airport_id INT,
    carrier VARCHAR(255),
    departure_time TIMESTAMP,
    arrival_time TIMESTAMP,
    FOREIGN KEY (from_airport_id) REFERENCES Airport(id),
    FOREIGN KEY (to_airport_id) REFERENCES Airport(id)
);