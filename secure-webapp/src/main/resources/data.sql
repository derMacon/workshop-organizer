DROP DATABASE flat_app_db;
CREATE DATABASE flat_app_db;

USE flat_app_db;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE user
(
    user_id  INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(500)  NOT NULL,
    password VARCHAR(5000) NOT NULL,
    role     VARCHAR(5000) NOT NULL
);

CREATE TABLE room
(
    room_id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    room_number      INT(6) UNSIGNED NOT NULL,
    level            INT(6) UNSIGNED NOT NULL,
    room_description VARCHAR(5000)   NOT NULL
);

CREATE TABLE living_space
(
    living_space_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    bedroom_id      INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (bedroom_id) REFERENCES room (room_id),
    kitchen_id      INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (kitchen_id) REFERENCES room (room_id),
    bathroom_id     INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (bathroom_id) REFERENCES room (room_id),
    storage_id      INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (storage_id) REFERENCES room (room_id)
);

CREATE TABLE flatmate
(
    flatmate_id     INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id         INT(6) UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    living_space_id INT(6) UNSIGNED,
    FOREIGN KEY (living_space_id) REFERENCES living_space (living_space_id),
    firstname       VARCHAR(500) NOT NULL,
    surname         VARCHAR(500) NOT NULL,
    birthday        DATE         NOT NULL
);

CREATE TABLE item
(
    item_id        INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    item_count     INT(6) UNSIGNED NOT NULL,
    item_name      VARCHAR(500)    NOT NULL,
    destination_id INT(6) UNSIGNED,
    FOREIGN KEY (destination_id) REFERENCES room (room_id),
    status         BIT
);

CREATE TABLE task
(
    task_id         INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    task_title      VARCHAR(500)  NOT NULL,
    description     VARCHAR(5000) NOT NULL,
    task_status     BIT,
    publishing_date DATE          NOT NULL
);

CREATE TABLE task_flatmate
(
    task_id     INT(6) UNSIGNED NOT NULL,
    flatmate_id INT(6) UNSIGNED NOT NULL,
    PRIMARY KEY CLUSTERED (task_id, flatmate_id),
    FOREIGN KEY (task_id) REFERENCES task (task_id),
    FOREIGN KEY (flatmate_id) REFERENCES flatmate (flatmate_id)
);

CREATE TABLE item_preset
(
    preset_id       INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    preset_name     VARCHAR(500) NOT NULL,
    supply_category VARCHAR(30)  NOT NULL
);

create table if not exists persistent_logins
(
    username  VARCHAR(500) not null,
    series    varchar(64) primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);



INSERT INTO room (room_id, room_number, level, room_description)
VALUES (100, 1, 0, "Zimmer neben Eingangstür");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (101, 2, 0, "Zimmer gegenüber Eingangstür");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (102, 3, 0, "Zimmer gegenüber der Treppe");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (103, 4, 0, "Badezimmer Erdgeschoss");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (104, 4, 0, "Zimmer hinter Badezimmer Erdgeschoss");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (105, 5, 0, "Küche Erdgeschoss");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (106, 6, 0, "Gästeklo Erdgeschoss");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (107, 1, 1, "Badezimmer rechts von Treppe");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (108, 2, 1, "Zimmer gegenüber Verbindungstür");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (109, 3, 1, "Zimmer gegenüber Badezimmer (rechts)");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (110, 4, 1, "Zimmer gegenüber Treppe");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (111, 5, 1, "Zimmer Küche");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (112, 6, 1, "Zimmer neben Küche");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (113, 7, 1, "Zimmer neben Badezimmer");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (114, 8, 1, "Badezimmer links von Treppe");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (115, 1, 2, "Küche");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (116, 2, 2, "Zimmer links von der Treppe");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (117, 3, 2, "Zimmer in der Mitte");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (118, 4, 2, "Zimmer rechts von der Treppe");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (119, 0, 0, "Flur Erdgeschoss");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (120, 0, 1, "Flur");

INSERT INTO room (room_id, room_number, level, room_description)
VALUES (121, 0, 2, "Flur");


INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (200, 100, 105, 103, 103);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (201, 101, 105, 103, 103);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (202, 102, 105, 103, 103);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (203, 104, 105, 103, 103);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (205, 108, 111, 107, 120);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (206, 109, 111, 107, 120);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (207, 110, 111, 107, 120);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (208, 112, 111, 114, 120);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (209, 113, 111, 114, 120);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (210, 116, 115, 107, 120);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (211, 117, 115, 114, 120);

INSERT INTO living_space (living_space_id, bedroom_id, kitchen_id, bathroom_id, storage_id)
VALUES (212, 118, 115, 107, 120);


INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (500, "Klopapier", "BATHROOM_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (501, "Küchenpapier", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (502, "Backpapier", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (503, "Badreiniger", "BATHROOM_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (504, "Bioabfalltüten", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (505, "Desinfektionsspray", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (506, "Gelbe Müllsäcke", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (507, "Geschirrspülsalz", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (508, "Kalkreiniger", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (509, "Lappen", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (510, "Metallschwäme", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (511, "Olivenöl", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (512, "Pfeffer (Körner)", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (513, "Rapsöl", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (514, "Salz", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (515, "Scheuermilch", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (516, "Schwämme", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (517, "Seife", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (518, "Sonnenblumenöl", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (519, "Spüli", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (520, "Natron", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (521, "Backpulver", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (522, "Essigessenz", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (523, "Abflussreiniger", "CLEANING_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (524, "Bratöl", "KITCHEN_SUPPLY");

INSERT INTO item_preset (preset_id, preset_name, supply_category)
VALUES (525, "Bioabfalltüten", "KITCHEN_SUPPLY");
