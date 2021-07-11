CREATE TABLE IF NOT EXISTS accounts;

CREATE TABLE accounts (
      `first_name` VARCHAR(64) NOT NULL,
      `last_name` VARCHAR(64) NOT NULL,
      `mail` VARCHAR(64) NOT NULL PRIMARY KEY,
      `location_id` INT NOT NULL,
      `pass` BLOB(64) NOT NULL
);

INSERT INTO accounts VALUES ('name','last','mail1',1, 12345);
INSERT INTO accounts VALUES ('name','last','mail2',1, 12346);
INSERT INTO accounts VALUES ('name','last','mail3',2, 12347);
INSERT INTO accounts VALUES ('name','last','mail4',3, 1234);

-- select * from accounts inner join locations using (location_id); query