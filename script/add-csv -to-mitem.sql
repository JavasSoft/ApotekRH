LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/dataset.csv'
INTO TABLE mitem
FIELDS TERMINATED BY ';'
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(@kode, @nama, @satuan_besar, @satuan_kecil, @konversi, @harga_beli, @harga_jual, @kategori)
SET
  Kode = NULLIF(TRIM(@kode), ''),
  Nama = NULLIF(TRIM(@nama), ''),
  HargaBeli = IFNULL(@harga_beli, 0),
  Aktif = 1,
  Kategori = CASE
    WHEN LOWER(TRIM(@kategori)) = 'obat bebas' THEN 'Obat Bebas'
    WHEN LOWER(TRIM(@kategori)) = 'obat prekursor' THEN 'Obat Prekursor'
    WHEN LOWER(TRIM(@kategori)) = 'obat keras' THEN 'Obat Keras'
    WHEN LOWER(TRIM(@kategori)) = 'alat kesehatan' THEN 'Alat Kesehatan'
    WHEN LOWER(TRIM(@kategori)) = 'vitamin' THEN 'Vitamin'
    ELSE 'Obat Bebas'
  END;
