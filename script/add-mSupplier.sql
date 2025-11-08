CREATE TABLE `msup` (
  `IDSupplier` INT(11) NOT NULL 
  `Kode` VARCHAR(20) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci', 
  `Nama` VARCHAR(100) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
  `Alamat` VARCHAR(200) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
  `Telephone` VARCHAR(20) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
  `Kota` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
  `Bank` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
  `NomorRekening` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
  `NamaRekening` VARCHAR(100) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
  `IsAktif` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`IDSupplier`) USING BTREE,
  UNIQUE INDEX `Kode` (`Kode`) USING BTREE
) 
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  LUKMAN
 * Created: 6 Nov 2025
 */

