CREATE TABLE `tbelih` (
    `IDBeliH` INT(11) NOT NULL AUTO_INCREMENT,
    `IDPoH` INT(11) NOT NULL,
    `InvoiceNumber` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
    `Tanggal` DATETIME NOT NULL,
    `Total` DOUBLE NOT NULL,
    `Status` ENUM('Pending', 'Paid', 'Canceled') NOT NULL COLLATE 'latin1_swedish_ci',
    `InsertTime` DATETIME NULL DEFAULT NULL,
    `InsertUser` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
    PRIMARY KEY (`IDBeliH`) USING BTREE,
    INDEX `IDPoH` (`IDPoH`) USING BTREE,
    CONSTRAINT `tbelih_ibfk_1` FOREIGN KEY (`IDPoH`) REFERENCES `tpoh` (`IDPoH`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;
