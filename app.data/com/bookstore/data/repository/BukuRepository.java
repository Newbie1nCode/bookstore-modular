package com.bookstore.data.repository;

import com.bookstore.data.entity.Buku;
import com.bookstore.data.internal.InMemoryDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * BukuRepository — satu-satunya pintu masuk publik untuk mengakses
 * data buku dari luar modul app.data.
 *
 * Kelas ini berada di paket "com.bookstore.data.repository" yang
 * DIEKSPOR, sehingga boleh dipakai oleh app.logic. Di baliknya, ia
 * memanggil InMemoryDatabase yang bersifat internal/tersembunyi.
 */
public final class BukuRepository {

    public List<Buku> cariSemua() {
        return new ArrayList<>(InMemoryDatabase.semuaData().values());
    }

    public Optional<Buku> cariByIsbn(String isbn) {
        return Optional.ofNullable(InMemoryDatabase.semuaData().get(isbn));
    }

    public void simpan(Buku buku) {
        InMemoryDatabase.simpan(buku);
    }

    public Collection<Buku> cariByKategori(String namaKategori) {
        List<Buku> hasil = new ArrayList<>();
        for (Buku b : InMemoryDatabase.semuaData().values()) {
            if (b.getKategori().getNama().equalsIgnoreCase(namaKategori)) {
                hasil.add(b);
            }
        }
        return hasil;
    }
}
