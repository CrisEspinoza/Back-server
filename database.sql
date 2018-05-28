CREATE DATABASE mingeso;

USE mingeso;

CREATE TABLE product(
id 			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
name 		VARCHAR(255) NOT NULL,
expiration_date 		DATE,
category_id    INT,
price 		INT
 );

 CREATE TABLE category(
   id 			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
   name     VARCHAR(50) NOT NULL
 );



INSERT INTO `mingeso`.`category` (name) values ('Importado');
INSERT INTO `mingeso`.`category` (name) values ('Nacional');


INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Confort', '2008-11-11', 1, 10000);
INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Tallarines', '2010-12-01', 1, 7000);
INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Arroz', '2000-11-11', 2, 4000);
INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Mantequilla', '2019-11-11', 1, 2000);
INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Hamburguesa', '2008-11-11', 1, 100);
INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Mayonesa', '2018-11-11', 2, 50000);
INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Ketchup', '2008-12-11', 2, 60000);
INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Detergente', '2008-10-11', 1, 70000);
INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Pañales', '2008-11-12', 2, 80000);
INSERT INTO `mingeso`.`product` (name, expiration_date, category_id, price) values ('Servilleta', '2008-11-12', 1, 12000);
