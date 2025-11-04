CREATE TABLE `tstokitem` (
    `IDStock` INT(11) NOT NULL AUTO_INCREMENT,
    `Kode` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
    `Nama` VARCHAR(100) NOT NULL COLLATE 'latin1_swedish_ci',
    `Quantity` INT(11) NOT NULL,
    `Harga` DECIMAL(10,2) NOT NULL,
    `LastUpdated` DATETIME NULL DEFAULT NULL,
    PRIMARY KEY (`IDStock`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;
