DROP DATABASE workshop_app;
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
    course_id             INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    host_id               INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (host_id) REFERENCES person (person_id),
    course_name           VARCHAR(500)    NOT NULL,
    course_summary        VARCHAR(5000)   NOT NULL,
    course_description    VARCHAR(5000)   NOT NULL,
    max_participant_count INT(6) UNSIGNED NOT NULL
);

CREATE TABLE announcement
(
    announcement_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(5000)   NOT NULL,
    content VARCHAR(5000)   NOT NULL,
    publishing_date DATE            NOT NULL,
    course_id INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course (course_id)
);

CREATE TABLE course_person
(
    course_id INT(6) UNSIGNED NOT NULL,
    person_id INT(6) UNSIGNED NOT NULL,
    PRIMARY KEY CLUSTERED (course_id, person_id),
    FOREIGN KEY (course_id) REFERENCES course (course_id),
    FOREIGN KEY (person_id) REFERENCES person (person_id)
);



INSERT INTO user (user_id, username, password, role)
VALUES (1, "admin", "p", "ROLE_ADMIN");

INSERT INTO user (user_id, username, password, role)
VALUES (2, "manager", "p", "ROLE_MANAGER");

INSERT INTO user (user_id, username, password, role)
VALUES (3, "user", "p", "ROLE_USER");


INSERT INTO person (person_id, firstname, surname, email, user_id)
VALUES (100, "firstname1", "surname1", "workshop1234@gmx.de", 1);

INSERT INTO person (person_id, firstname, surname, email, user_id)
VALUES (101, "firstname2", "surname2", "workshop1234@gmx.de", 2);

INSERT INTO person (person_id, firstname, surname, email, user_id)
VALUES (102, "firstname3", "surname3", "workshop1234@gmx.de", 3);


INSERT INTO course (course_id, host_id, course_name, course_summary, course_description,
                    max_participant_count)
VALUES (200, 100, "course1", "summary 1", "description course 1", 5);

INSERT INTO course (course_id, host_id, course_name, course_summary, course_description,
                    max_participant_count)
VALUES (201, 101, "course2", "summary 2", "description course 2", 10);


INSERT INTO course_person(course_id, person_id)
VALUES (200, 100);

INSERT INTO course_person(course_id, person_id)
VALUES (201, 101);

INSERT INTO course_person(course_id, person_id)
VALUES (201, 102);


INSERT INTO meeting(meeting_id, meeting_date, address, course_id)
VALUES (300, '2018-01-12', "test address 1", 200);


INSERT INTO announcement(announcement_id, title, content, publishing_date, course_id)
VALUES (400, "title 1", "test content 1", '2018-01-12', 200);

INSERT INTO announcement(announcement_id, title, content, publishing_date, course_id)
VALUES (401, "title 2", "test content 2", '2018-01-12', 200);

INSERT INTO announcement(announcement_id, title, content, publishing_date, course_id)
VALUES (402, "title 3", "test content 3", '2018-01-12', 201);


