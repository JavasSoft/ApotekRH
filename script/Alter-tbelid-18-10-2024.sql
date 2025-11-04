ALTER TABLE `tbelid`
ADD COLUMN `IDBarang` INT(11) NOT NULL AFTER `Kode`,  -- Add the new column
ADD INDEX `IDBarang` (`IDBarang`),  -- Add an index for the new column
ADD CONSTRAINT `tbelid_ibfk_2` FOREIGN KEY (`IDBarang`) REFERENCES `mbarang` (`IDBarang`) ON UPDATE RESTRICT ON DELETE RESTRICT;
