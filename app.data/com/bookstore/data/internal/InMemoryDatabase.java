package com.bookstore.data.internal;

import com.bookstore.data.entity.Buku;
import com.bookstore.data.entity.Kategori;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * InMemoryDatabase — simulasi database internal.
 *
 * PENTING: Paket "com.bookstore.data.internal" TIDAK diekspor pada
 * module-info.java milik app.data. Artinya kelas ini hanya boleh
 * diakses dari DALAM modul app.data sendiri (misalnya oleh
 * BukuRepository di paket com.bookstore.data.repository).
 *
 * Modul lain (app.logic, app.ui) tidak akan bisa mengimpor kelas ini
 * sama sekali — bahkan gagal pada saat kompilasi — karena modul Java
 * menegakkan strong encapsulation. Ini mengimplementasikan aturan:
 * "app.ui tidak boleh mengakses langsung paket internal milik app.data".
 */
public final class InMemoryDatabase {

    private static final Map<String, Buku> TABEL_BUKU = new LinkedHashMap<>();

    static {
        seed();
    }

    private InMemoryDatabase() {
    }

    private static void seed() {
        Kategori fiksi = new Kategori("K01", "Fiksi");
        Kategori teknologi = new Kategori("K02", "Teknologi");
        Kategori sejarah = new Kategori("K03", "Sejarah");

        simpan(new Buku("978-602-1", "Laut Bercerita", "Leila S. Chudori", fiksi, 95000, 12));
        simpan(new Buku("978-602-2", "Clean Architecture", "Robert C. Martin", teknologi, 185000, 7));
        simpan(new Buku("978-602-3", "Sapiens", "Yuval Noah Harari", sejarah, 145000, 9));
        simpan(new Buku("978-602-4", "Filosofi Teras", "Henry Manampiring", fiksi, 89000, 15));
        simpan(new Buku("978-602-5", "Java: The Complete Reference", "Herbert Schildt", teknologi, 210000, 5));
        simpan(new Buku("978-602-6", "Bumi Manusia", "Pramoedya A. Toer", sejarah, 78000, 20));
    }

    public static void simpan(Buku buku) {
        TABEL_BUKU.put(buku.getIsbn(), buku);
    }

    public static Map<String, Buku> semuaData() {
        return TABEL_BUKU;
    }
}
