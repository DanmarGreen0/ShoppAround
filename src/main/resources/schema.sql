--
--DROP TABLE IF EXISTS User;
--DROP TABLE IF EXISTS Auth_User_Group;
--CREATE TABLE User(
--     Id INT AUTO_INCREMENT PRIMARY KEY,
--     Username varchar(255) NOT NULL UNIQUE,
--     First_Name varchar(255) NOT NULL,
--     Last_Name varchar(255) NOT NULL,
--     Password varchar(255) NOT NULL,
--     Address varchar(255) NOT NULL,
--     Date_Of_Birth Date NOT NULL,
--     Email varchar(255) NOT NULL UNIQUE,
--     Phone_No varchar(255) NOT NULL,
--     Created_At Date,
--     Modified_At Date
--);
--
--CREATE TABLE Roles (
--     Auth_User_Group_Id INT AUTO_INCREMENT PRIMARY KEY,
--     Name varchar(255) NOT NULL,
--);

CREATE TABLE SPRING_SESSION (
	PRIMARY_ID CHAR(36) NOT NULL,
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	PRINCIPAL_NAME VARCHAR(100),
	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES BLOB NOT NULL,
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;