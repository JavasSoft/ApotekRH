CREATE TABLE tjurnalitem (
    IDJurnal INT AUTO_INCREMENT PRIMARY KEY,
    Tanggal DATE,
    IDItem INT,
    KodeTrans VARCHAR(20),       -- Nomor dokumen (PO / Penjualan / Stok Awal)
    JenisTrans ENUM('StokAwal','Beli','Jual','ReturBeli','ReturJual','Koreksi'),
    QtyMasuk DECIMAL(18,2) DEFAULT 0,
    QtyKeluar DECIMAL(18,2) DEFAULT 0,
    Satuan VARCHAR(20),
    Keterangan VARCHAR(255),
    InsertUser VARCHAR(50),
    InsertTime DATETIME DEFAULT CURRENT_TIMESTAMP

);
