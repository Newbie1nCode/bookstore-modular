/**
 * Modul app.data
 * --------------
 * Tanggung jawab : Mengelola kelas entitas data (Buku, Kategori)
 *                   dan simulasi database internal.
 * Kontrol akses  : Hanya mengekspor paket entitas data ke modul lain.
 *                   Paket internal (database simulasi) TIDAK diekspor,
 *                   sehingga tidak bisa diakses langsung oleh app.ui.
 */
module app.data {
    exports com.bookstore.data.entity;
    exports com.bookstore.data.repository;
}
