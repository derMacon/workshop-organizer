CREATE DATABASE workshop_app;

USE workshop_app;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE user
(
    user_id  INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(500)  NOT NULL,
    password VARCHAR(5000) NOT NULL,
    role     VARCHAR(5000) NOT NULL
);

CREATE TABLE person
(
    person_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(500)    NOT NULL,
    surname   VARCHAR(500)    NOT NULL,
    email     VARCHAR(500)    NOT NULL,
    user_id   INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

CREATE TABLE meeting
(
    meeting_id   INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    meeting_date DATE            NOT NULL,
    address      VARCHAR(50)     NOT NULL,
    course_id    INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course (course_id)
);

CREATE TABLE course
(
    course_id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    host_id            INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (host_id) REFERENCES person (person_id),
    course_name        VARCHAR(500)    NOT NULL,
    course_description VARCHAR(5000)   NOT NULL
);



INSERT INTO user (user_id, username, password, role)
VALUES (1, "admin", "p", "ROLE_ADMIN");

INSERT INTO user (user_id, username, password, role)
VALUES (2, "manager", "p", "ROLE_MANAGER");

INSERT INTO user (user_id, username, password, role)
VALUES (3, "user", "p", "ROLE_USER");


INSERT INTO person (person_id, firstname, surname, email, user_id)
VALUES (100, "firstname1", "surname1", "email1@mail.de", 1);

INSERT INTO person (person_id, firstname, surname, email, user_id)
VALUES (101, "firstname2", "surname2", "email2@mail.de", 2);

INSERT INTO person (person_id, firstname, surname, email, user_id)
VALUES (102, "firstname3", "surname3", "email3@mail.de", 3);


INSERT INTO course (course_id, host_id, course_name, course_description)
VALUES (200, 100, "course1", "description course 1");

INSERT INTO course (course_id, host_id, course_name, course_description)
VALUES (201, 101, "course2", "description course 2");

