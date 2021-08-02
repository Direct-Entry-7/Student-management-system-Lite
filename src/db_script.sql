DROP DATABASE IF EXISTS sms_lite;

CREATE DATABASE sms_lite;

USE sms_lite;

CREATE TABLE student(
    id VARCHAR(4) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);


INSERT INTO student VALUES ('C001','Saman');
INSERT INTO student VALUES ('C002','Nisal');

CREATE TABLE contact(
    contact VARCHAR(15) NOT NULL,
    student_id VARCHAR(4) NOT NULL,
    CONSTRAINT PRIMARY KEY (contact,student_id),
    CONSTRAINT fk_contact FOREIGN KEY (student_id) REFERENCES student(id)
);