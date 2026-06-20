/**
 * Modul app.ui
 * -------------
 * Tanggung jawab : Menampilkan antarmuka CLI dan menerima input
 *                   langsung dari pengguna.
 * Kontrol akses  : Membutuhkan app.logic dan berfungsi sebagai
 *                   main entry point aplikasi.
 *
 * CATATAN ARSITEKTUR:
 * app.ui SENGAJA tidak menulis "requires app.data;" di sini.
 * Modul ini hanya mengenal com.bookstore.logic.* (lihat com.bookstore.ui.Main).
 * Java Module System akan menolak kompilasi apapun yang mencoba
 * mengakses paket internal app.data secara langsung dari sini,
 * sesuai larangan keras pada ketentuan tugas.
 */
module app.ui {
    requires app.logic;
}
