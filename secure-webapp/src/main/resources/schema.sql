CREATE DATABASE workshop_app;

USE workshop_app;
SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE user (
  user_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(500) NOT NULL,
  password VARCHAR(5000) NOT NULL,
  user_role VARCHAR(50) NOT NULL
);

INSERT INTO user (user_id, username, password, user_role)
VALUES (1, "admin", "password1", "ADMIN");

INSERT INTO user (user_id, username, password, user_role)
VALUES (2, "user1", "password2", "PARTICIPANT");

INSERT INTO user (user_id, username, password, user_role)
VALUES (3, "user2", "password3", "MANAGER");

