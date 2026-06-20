package com.bookstore.logic.service;

import com.bookstore.data.entity.Buku;
import com.bookstore.data.repository.BukuRepository;
import com.bookstore.logic.dto.HasilTransaksi;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * BookService — fasad logika bisnis utama yang dipakai oleh app.ui.
 *
 * app.ui TIDAK pernah memanggil BukuRepository atau entity app.data
 * secara langsung untuk operasi transaksi; semua melewati kelas ini,
 * yang kemudian berkoordinasi dengan app.data dan DiscountService.
 */
public final class BookService {

    private final BukuRepository repository;
    private final DiscountService discountService;

    public BookService() {
        this.repository = new BukuRepository();
        this.discountService = new DiscountService();
    }

    public List<Buku> daftarSemuaBuku() {
        return repository.cariSemua();
    }

    public Collection<Buku> cariByKategori(String kategori) {
        return repository.cariByKategori(kategori);
    }

    public Optional<Buku> cariByIsbn(String isbn) {
        return repository.cariByIsbn(isbn);
    }

    /**
     * Validasi stok + hitung diskon + hitung total akhir, lalu
     * benar-benar memotong stok di data store jika berhasil.
     */
    public HasilTransaksi beli(String isbn, int jumlah) {
        Buku buku = repository.cariByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Buku dengan ISBN " + isbn + " tidak ditemukan."));

        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pembelian harus lebih dari 0.");
        }
        if (jumlah > buku.getStok()) {
            throw new IllegalStateException(
                    "Stok tidak cukup. Tersisa " + buku.getStok() + " eksemplar untuk \"" + buku.getJudul() + "\".");
        }

        double subtotal = buku.getHarga() * jumlah;
        double persenDiskon = discountService.hitungPersenDiskon(jumlah);
        double nominalDiskon = subtotal * persenDiskon;
        double totalAkhir = subtotal - nominalDiskon;

        buku.kurangiStok(jumlah);

        return new HasilTransaksi(buku, jumlah, subtotal, nominalDiskon, totalAkhir);
    }

    public void tambahBuku(Buku buku) {
        repository.simpan(buku);
    }
}
