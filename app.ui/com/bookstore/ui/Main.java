package com.bookstore.ui;

import com.bookstore.data.entity.Buku;
import com.bookstore.data.entity.Kategori;
import com.bookstore.logic.dto.HasilTransaksi;
import com.bookstore.logic.service.BookService;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

import static com.bookstore.ui.Console.*;

/**
 * Main — entry point CLI (main entry point) untuk modul app.ui.
 *
 * Catatan kepatuhan arsitektur:
 *  - Modul ini hanya berkomunikasi dengan {@link BookService} (paket
 *    com.bookstore.logic.service yang diekspor oleh app.logic).
 *  - Tipe {@link Buku} & {@link Kategori} dipakai hanya untuk MENAMPILKAN
 *    data (getter), bukan untuk mengakses logika/penyimpanan internal —
 *    akses ke database tetap 100% lewat BookService.
 */
public final class Main {

    private static final BookService bookService = new BookService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final NumberFormat RP = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private static final int WIDTH = 64;

    public static void main(String[] args) {
        tampilkanBanner();
        boolean jalan = true;
        while (jalan) {
            tampilkanMenu();
            String pilihan = bacaInput("Pilih menu");
            switch (pilihan.trim()) {
                case "1":
                    tampilkanSemuaBuku();
                    break;
                case "2":
                    cariPerKategori();
                    break;
                case "3":
                    prosesPembelian();
                    break;
                case "4":
                    tambahBukuBaru();
                    break;
                case "0":
                    jalan = false;
                    break;
                default:
                    pesanError("Pilihan tidak dikenali. Coba lagi.");
            }
        }
        tampilkanPenutup();
    }

    // ---------------------------------------------------------------
    // Tampilan
    // ---------------------------------------------------------------

    private static void tampilkanBanner() {
        clearScreenHint();
        System.out.println(c(" ____             _    ____  _                    ", FG_ORANGE));
        System.out.println(c("| __ )  ___   ___ | | _/ ___|| |_ ___  _ __ ___    ", FG_ORANGE));
        System.out.println(c("|  _ \\ / _ \\ / _ \\| |/ \\___ \\| __/ _ \\| '__/ _ \\   ", FG_ORANGE));
        System.out.println(c("| |_) | (_) | (_) |   < ___) | || (_) | | |  __/   ", FG_ORANGE));
        System.out.println(c("|____/ \\___/ \\___/|_|\\_\\____/ \\__\\___/|_|  \\___|   ", FG_ORANGE));
        System.out.println();
        System.out.println(c("        Sistem Manajemen Toko Buku Digital", BOLD + FG_WHITE)
                + c("  ·  arsitektur modular Java", DIM + FG_GRAY));
        doubleRule(WIDTH);
        System.out.println(c(" Modul aktif: ", FG_GRAY) + c("app.data", FG_CYAN) + c(" → ", FG_GRAY)
                + c("app.logic", FG_GREEN) + c(" → ", FG_GRAY) + c("app.ui", FG_MAGENTA));
        doubleRule(WIDTH);
    }

    private static void tampilkanMenu() {
        System.out.println();
        box("MENU UTAMA");
        cetakMenuItem("1", "Tampilkan semua buku");
        cetakMenuItem("2", "Cari buku berdasarkan kategori");
        cetakMenuItem("3", "Beli buku");
        cetakMenuItem("4", "Tambah buku baru");
        cetakMenuItem("0", "Keluar");
        rule(WIDTH);
    }

    private static void cetakMenuItem(String kode, String label) {
        System.out.println(c("  [" + kode + "]", BOLD + FG_ORANGE) + "  " + c(label, FG_WHITE));
    }

    private static void tampilkanSemuaBuku() {
        List<Buku> daftar = bookService.daftarSemuaBuku();
        cetakTabelBuku(daftar);
    }

    private static void cariPerKategori() {
        String kategori = bacaInput("Masukkan nama kategori (contoh: Fiksi, Teknologi, Sejarah)");
        Collection<Buku> hasil = bookService.cariByKategori(kategori);
        if (hasil.isEmpty()) {
            pesanInfo("Tidak ada buku pada kategori \"" + kategori + "\".");
            return;
        }
        cetakTabelBuku(List.copyOf(hasil));
    }

    private static void prosesPembelian() {
        tampilkanSemuaBuku();
        String isbn = bacaInput("Masukkan ISBN buku yang ingin dibeli");
        Optional<Buku> opt = bookService.cariByIsbn(isbn);
        if (opt.isEmpty()) {
            pesanError("ISBN tidak ditemukan.");
            return;
        }
        String jumlahStr = bacaInput("Jumlah eksemplar");
        int jumlah;
        try {
            jumlah = Integer.parseInt(jumlahStr.trim());
        } catch (NumberFormatException e) {
            pesanError("Jumlah harus berupa angka.");
            return;
        }

        try {
            HasilTransaksi hasil = bookService.beli(isbn, jumlah);
            cetakStruk(hasil);
        } catch (IllegalArgumentException | IllegalStateException e) {
            pesanError(e.getMessage());
        }
    }

    private static void tambahBukuBaru() {
        System.out.println();
        box("TAMBAH BUKU BARU");
        String isbn = bacaInput("ISBN");
        String judul = bacaInput("Judul");
        String penulis = bacaInput("Penulis");
        String namaKategori = bacaInput("Kategori");
        double harga;
        int stok;
        try {
            harga = Double.parseDouble(bacaInput("Harga (angka saja, contoh: 99000)").trim());
            stok = Integer.parseInt(bacaInput("Stok awal").trim());
        } catch (NumberFormatException e) {
            pesanError("Harga/stok harus berupa angka.");
            return;
        }

        Kategori kategori = new Kategori("K-" + System.currentTimeMillis(), namaKategori);
        Buku bukuBaru = new Buku(isbn, judul, penulis, kategori, harga, stok);
        bookService.tambahBuku(bukuBaru);
        pesanSukses("Buku \"" + judul + "\" berhasil ditambahkan ke katalog.");
    }

    // ---------------------------------------------------------------
    // Komponen tampilan tabel & struk
    // ---------------------------------------------------------------

    private static void cetakTabelBuku(List<Buku> daftar) {
        System.out.println();
        box("KATALOG BUKU (" + daftar.size() + " judul)");
        if (daftar.isEmpty()) {
            pesanInfo("Belum ada data buku.");
            return;
        }
        String header = String.format(" %-12s %-26s %-16s %-12s %-12s %s",
                "ISBN", "Judul", "Kategori", "Harga", "Stok", "Penulis");
        System.out.println(c(header, BOLD + FG_CYAN));
        rule(WIDTH + 40);
        for (Buku b : daftar) {
            String stokWarna = b.getStok() <= 5 ? FG_RED : FG_GREEN;
            String baris = String.format(" %-12s %-26s %-16s %-12s ",
                    b.getIsbn(), potong(b.getJudul(), 26), potong(b.getKategori().getNama(), 16),
                    RP.format(b.getHarga()));
            System.out.print(c(baris, FG_WHITE));
            System.out.print(c(String.format("%-12s", b.getStok() + " unit"), stokWarna));
            System.out.println(c(b.getPenulis(), DIM + FG_GRAY));
        }
        rule(WIDTH + 40);
    }

    private static void cetakStruk(HasilTransaksi hasil) {
        System.out.println();
        box("STRUK PEMBELIAN");
        Buku b = hasil.getBuku();
        System.out.println(c("  Judul     : ", FG_GRAY) + c(b.getJudul(), BOLD + FG_WHITE));
        System.out.println(c("  Jumlah    : ", FG_GRAY) + hasil.getJumlah() + " eksemplar");
        System.out.println(c("  Subtotal  : ", FG_GRAY) + RP.format(hasil.getSubtotal()));
        String warnaDiskon = hasil.getDiskon() > 0 ? FG_YELLOW : FG_GRAY;
        System.out.println(c("  Diskon    : ", FG_GRAY) + c("- " + RP.format(hasil.getDiskon()), warnaDiskon));
        rule(36);
        System.out.println(c("  TOTAL     : ", BOLD + FG_GRAY) + c(RP.format(hasil.getTotalAkhir()), BOLD + FG_GREEN));
        rule(36);
        System.out.println(c("  Sisa stok \"" + b.getJudul() + "\": ", FG_GRAY) + b.getStok() + " unit");
        pesanSukses("Transaksi berhasil! Terima kasih telah berbelanja.");
    }

    // ---------------------------------------------------------------
    // Util input & pesan
    // ---------------------------------------------------------------

    private static String bacaInput(String label) {
        System.out.print(c("  ❯ " + label + ": ", FG_CYAN));
        return scanner.nextLine();
    }

    private static void pesanError(String msg) {
        System.out.println(c("  ✗ " + msg, FG_RED));
    }

    private static void pesanSukses(String msg) {
        System.out.println(c("  ✓ " + msg, FG_GREEN));
    }

    private static void pesanInfo(String msg) {
        System.out.println(c("  ℹ " + msg, FG_YELLOW));
    }

    private static String potong(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private static void tampilkanPenutup() {
        System.out.println();
        doubleRule(WIDTH);
        System.out.println(c("  Sampai jumpa! Modul app.ui ditutup dengan aman.", BOLD + FG_MAGENTA));
        doubleRule(WIDTH);
    }
}
