CREATE TABLE mcoatipe (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama_tipe VARCHAR(50) NOT NULL
);

CREATE TABLE mcoa (
    kode_akun VARCHAR(10) PRIMARY KEY,       -- Kode Akun sebagai ID unik
    nama_akun VARCHAR(100) NOT NULL,         -- Nama akun
    saldo_normal ENUM('Debet', 'Kredit') NOT NULL, -- Saldo normal (Debet/Kredit)
    tipe_akun_id INT NOT NULL,               -- Tipe Akun, mengacu ke tabel mcoatipe
    jenis_trans VARCHAR(50),                 -- Jenis transaksi (string opsional)
    arus_kas VARCHAR(50),                    -- Arus kas (opsional)
    keterangan TEXT,                         -- Keterangan tambahan
    data_aktif BOOLEAN DEFAULT TRUE,         -- Status aktif
    subledger BOOLEAN DEFAULT FALSE,         -- Subledger status
    parent_kode_akun VARCHAR(10),            -- Relasi ke parent akun, jika ada
    FOREIGN KEY (parent_kode_akun) REFERENCES mcoa(kode_akun), -- Parent-child relasi
    FOREIGN KEY (tipe_akun_id) REFERENCES mcoatipe(id)         -- Relasi ke tabel mcoatipe
);
-- Tambahkan foreign key untuk parent_kode_akun (self-referencing)
ALTER TABLE mcoa
ADD CONSTRAINT fk_parent FOREIGN KEY (parent_kode_akun) REFERENCES mcoa(kode_akun) ON DELETE CASCADE;

-- Tambahkan foreign key untuk tipe_akun_id
ALTER TABLE mcoa
ADD CONSTRAINT fk_tipe_akun FOREIGN KEY (tipe_akun_id) REFERENCES mcoatipe(id);
