
CREATE TABLE IF NOT EXISTS locations (
     `location_name` VARCHAR(64) NOT NULL,
     `sess` TINYINT NOT NULL,
     `location_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    `first_name` VARCHAR(64) NOT NULL,
    `last_name` VARCHAR(64) NOT NULL,
    `mail` VARCHAR(64) NOT NULL PRIMARY KEY,
    `location_id` INT NOT NULL,
    `pass` BLOB(64) NOT NULL,
     FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`)
);
