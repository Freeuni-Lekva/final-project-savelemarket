
INSERT INTO accounts VALUES ('name','last','mail1',1, 12345);
INSERT INTO accounts VALUES ('name','last','mail2',1, 12346);
INSERT INTO accounts VALUES ('name','last','mail3',2, 12347);
INSERT INTO accounts VALUES ('name','last','mail4',3, 1234);
-- query

select * from accounts inner join locations using (location_id);