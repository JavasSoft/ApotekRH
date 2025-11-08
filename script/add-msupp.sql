CREATE TABLE `msupp` (
	`IDSupplier` INT(11) NOT NULL AUTO_INCREMENT,
	`Kode` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Nama` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Email` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Alamat` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Telephone` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Kota` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Bank` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`NomorRekening` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`NamaRekening` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Aktif` TINYINT(4) NULL DEFAULT NULL,
	PRIMARY KEY (`IDSupplier`) USING BTREE,
	UNIQUE INDEX `Kode` (`Kode`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;