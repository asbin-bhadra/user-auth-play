-- Session schema

-- !Ups

CREATE TABLE User_AUTH.Session_Details (
  `SESSION_ID` BINARY(16) NOT NULL,
  `TOKEN` VARCHAR(45) NULL,
  `USERNAME` VARCHAR(45) NULL,
  `EXPIRATION` DATETIME NULL,
  PRIMARY KEY (`SESSION_ID`));

-- !Downs

DROP TABLE Session_Details;