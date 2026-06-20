/**
 * Modul app.logic
 * ----------------
 * Tanggung jawab : Memproses logika bisnis seperti diskon,
 *                   hitung total, dan validasi stok.
 * Kontrol akses  : Membutuhkan app.data, dan mengekspor paket
 *                   logika bisnis ke modul lain (app.ui).
 */
module app.logic {
    requires transitive app.data;

    exports com.bookstore.logic.service;
    exports com.bookstore.logic.dto;
}
