DROP TABLE IF EXISTS `product_details`;
CREATE TABLE `tfip_project`.`product_details` (
  `productID` INT NOT NULL AUTO_INCREMENT,
  `productName` VARCHAR(255) NOT NULL,
  `price` DOUBLE NOT NULL,
  `description` VARCHAR(255) NULL,
  `email` VARCHAR(255) NOT NULL,
  `uploadTime` DATETIME NOT NULL,
  `productStatus` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`productID`));

DROP TABLE IF EXISTS `image_details`;
CREATE TABLE `tfip_project`.`image_details` (
  `imageID` INT NOT NULL AUTO_INCREMENT,
  `imageName` VARCHAR(255) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `imageBytes` MEDIUMBLOB NOT NULL,
  `productID` INT NOT NULL,
  PRIMARY KEY (`imageID`),
  INDEX `productID_idx` (`productID` ASC) VISIBLE,
  CONSTRAINT `productID`
    FOREIGN KEY (`productID`)
    REFERENCES `tfip_project`.`product_details` (`productID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

DROP TABLE IF EXISTS `user_details`;
CREATE TABLE `tfip_project`.`user_details` (
  `userID` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`userID`));



