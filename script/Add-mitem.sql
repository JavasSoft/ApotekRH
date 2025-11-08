CREATE TABLE `mkategori` (
	`IDKategori` INT NOT NULL AUTO_INCREMENT,
	`Kode` VARCHAR(10) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Nama` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Deskripsi` TEXT NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`IsAktif` ENUM('Ya','Tidak') NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	PRIMARY KEY (`IDKategori`) USING BTREE,
	UNIQUE INDEX `Kode` (`Kode`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=6
;
