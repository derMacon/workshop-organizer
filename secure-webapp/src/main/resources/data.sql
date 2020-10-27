CREATE DATABASE workshop_app;

USE workshop_app;
SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE user (
  user_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(500) NOT NULL,
  password VARCHAR(5000) NOT NULL,
  role VARCHAR(5000) NOT NULL
);

INSERT INTO user (user_id, username, password, role) VALUES (1, "admin", "p", "ROLE_ADMIN");
INSERT INTO user (user_id, username, password, role) VALUES (2, "manager", "p", "ROLE_MANAGER");
INSERT INTO user (user_id, username, password, role) VALUES (3, "user", "p", "ROLE_USER");

