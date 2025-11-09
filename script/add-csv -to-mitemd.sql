LOAD DATA LOCAL INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/apotek.csv'
INTO TABLE mitemd
FIELDS TERMINATED BY ';'
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(@kode, @nama, @kategori, @satuan_kecil, @harga_beli, @harga_jual, @satuan_besar)
SET
  -- Ambil IDItem dari tabel mitem berdasarkan nama
  IDItem = (SELECT IDItem FROM mitem WHERE TRIM(Nama) = TRIM(@nama) LIMIT 1),

  -- Satuan besar (default Box kalau tidak ada di daftar)
  SatuanBesar = CASE
    WHEN LOWER(TRIM(@satuan_besar)) IN ('box','dus','pack','vial','ampul','pouch','tubes','botol','kaleng','strip')
      THEN CONCAT(UPPER(LEFT(TRIM(@satuan_besar), 1)), LOWER(SUBSTRING(TRIM(@satuan_besar), 2)))
    ELSE 'Box'
  END,

  -- Jumlah otomatis
  Jumlah = 1,

  -- Satuan kecil (default Pcs kalau tidak cocok)
  Satuan = CASE
    WHEN LOWER(TRIM(@satuan_kecil)) IN ('box','dus','pack','vial','ampul','pouch','tubes','botol','kaleng','strip')
      THEN CONCAT(UPPER(LEFT(TRIM(@satuan_kecil), 1)), LOWER(SUBSTRING(TRIM(@satuan_kecil), 2)))
    ELSE 'Pcs'
  END,

  -- Konversi default 1
  Konversi = 1,

  -- Harga jual dari CSV
  HargaJual = IFNULL(NULLIF(@harga_jual, ''), 0),

  -- Laba persen dihitung otomatis
  LabaPersen = CASE
    WHEN @harga_beli IS NULL OR @harga_beli = 0 THEN 0
    ELSE ROUND(((@harga_jual - @harga_beli) / @harga_beli) * 100, 2)
  END;
