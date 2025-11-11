CREATE TABLE `mitem` (
	`IDItem` INT NOT NULL AUTO_INCREMENT,
	`Kode` VARCHAR(100) NOT NULL DEFAULT '0' COLLATE 'utf8mb4_0900_ai_ci',
	`Nama` VARCHAR(100) NOT NULL DEFAULT '0' COLLATE 'utf8mb4_0900_ai_ci',
	`Kategori` ENUM('Obat Bebas','Obat Prekursor','Obat Keras','Alat Kesehatan','Vitamin') NOT NULL DEFAULT 'Obat Bebas' COLLATE 'utf8mb4_0900_ai_ci',
	`HargaBeli` DOUBLE NOT NULL DEFAULT '0',
	`Aktif` TINYINT NOT NULL DEFAULT '0',
	PRIMARY KEY (`IDItem`) USING BTREE,
	UNIQUE INDEX `Kode` (`Kode`) USING BTREE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
AUTO_INCREMENT=12303
;
