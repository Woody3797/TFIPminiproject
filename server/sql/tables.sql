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

DROP TABLE IF EXISTS `chat_details`;
CREATE TABLE `tfip_project`.`chat_details` (
  `chatID` INT NOT NULL AUTO_INCREMENT,
  `buyer` VARCHAR(255) NOT NULL,
  `seller` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`chatID`));

DROP TABLE IF EXISTS `message_details`;
CREATE TABLE `tfip_project`.`message_details` (
  `messageID` INT NOT NULL AUTO_INCREMENT,
  `chatID` INT NOT NULL,
  `sender` VARCHAR(255) NOT NULL,
  `content` VARCHAR(512) NOT NULL,
  `timestamp` DATETIME NOT NULL,
  PRIMARY KEY (`messageID`));
