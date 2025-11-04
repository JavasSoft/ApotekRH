CREATE TABLE `tbelibayar` (
    `IDBayar` INT(11) NOT NULL AUTO_INCREMENT,
    `IDBeliH` INT(11) NOT NULL,
    `TanggalBayar` DATETIME NOT NULL,
    `Jumlah` DECIMAL(10,2) NOT NULL,
    `MetodePembayaran` ENUM('Cash', 'Transfer', 'Credit Card') NOT NULL COLLATE 'latin1_swedish_ci',
    `InsertTime` DATETIME NULL DEFAULT NULL,
    `InsertUser` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
    PRIMARY KEY (`IDBayar`) USING BTREE,
    INDEX `IDBeliH` (`IDBeliH`) USING BTREE,
    CONSTRAINT `tbelibayar_ibfk_1` FOREIGN KEY (`IDBeliH`) REFERENCES `tbelih` (`IDBeliH`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;
