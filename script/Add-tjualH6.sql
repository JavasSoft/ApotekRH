CREATE TABLE tjualh (
    IDJualH INT AUTO_INCREMENT PRIMARY KEY,
    Kode VARCHAR(20) UNIQUE,          -- Nomor faktur
    IDCust INT,
    Tanggal DATE,
    JenisBayar ENUM('Tunai','Kredit'),
    IDDokter INT,
    JatuhTempo DATE NULL,             -- Diisi kalau kredit
    SubTotal DECIMAL(18,2),
    Diskon DECIMAL(18,2),
    Ppn DECIMAL(18,2),
    Total DECIMAL(18,2),
    Status ENUM('Open','Lunas','Cancel') DEFAULT 'Open',
    InsertUser VARCHAR(50),
    InsertTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    UpdateUser VARCHAR(50)
);
