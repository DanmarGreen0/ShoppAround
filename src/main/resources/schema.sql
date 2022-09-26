
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Auth_User_Group;
CREATE TABLE User (
     Id INT AUTO_INCREMENT PRIMARY KEY,
     Username varchar(255) NOT NULL UNIQUE,
     First_Name varchar(255) NOT NULL,
     Last_Name varchar(255) NOT NULL,
     Password varchar(255) NOT NULL,
     Address varchar(255) NOT NULL,
     Date_Of_Birth Date NOT NULL,
     Email varchar(255) NOT NULL UNIQUE,
     Phone_No varchar(255) NOT NULL,
     Created_At Date,
     Modified_At Date
);

CREATE TABLE Auth_User_Group (
     Auth_User_Group_Id INT AUTO_INCREMENT PRIMARY KEY,
     Username varchar(255) NOT NULL,
     Auth_Group varchar(255) NOT NULL,
     CONSTRAINT USER_AUTH_USER_GROUP_FK FOREIGN KEY(Username) REFERENCES User(Username),
     UNIQUE (Username, Auth_Group)
);
