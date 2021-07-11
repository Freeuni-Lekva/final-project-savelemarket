CREATE TABLE IF NOT EXISTS locations;

CREATE TABLE locations (
            `location_name` VARCHAR(64) NOT NULL,
            `sess` TINYINT NOT NULL,
            `location_id` INT AUTO_INCREMENT PRIMARY KEY
);

-- select distinct location_name from locations;

INSERT INTO locations (location_name,sess) VALUES ('pirveli',2);
INSERT INTO locations (location_name,sess) VALUES ('meore',1);
INSERT INTO locations (location_name,sess) VALUES ('mesame',1);