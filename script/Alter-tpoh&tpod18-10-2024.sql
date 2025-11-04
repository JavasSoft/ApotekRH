-- Modify tpoh table
ALTER TABLE `tpoh`
    CHANGE `Kode` `kode` VARCHAR(50) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
    CHANGE `Total` `Total` DECIMAL(10,2) NULL DEFAULT NULL,
    CHANGE `SubTotal` `SubTotal` DECIMAL(10,2) NULL DEFAULT NULL,
    CHANGE `Ppn` `Ppn` DECIMAL(10,2) NULL DEFAULT NULL,
    CHANGE `InsertTime` `InsertTime` DATETIME NULL DEFAULT NULL;

-- Modify tpod table
ALTER TABLE `tpod`
    CHANGE `price` `price` DECIMAL(10,2) NOT NULL,
    CHANGE `subtotal` `subtotal` DECIMAL(10,2) NOT NULL;
