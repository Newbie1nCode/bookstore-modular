package com.bookstore.data.entity;

import java.util.Objects;

/**
 * Entitas Buku — representasi data sebuah buku digital di toko.
 */
public final class Buku {

    private final String isbn;
    private final String judul;
    private final String penulis;
    private final Kategori kategori;
    private final double harga;
    private int stok;

    public Buku(String isbn, String judul, String penulis, Kategori kategori, double harga, int stok) {
        this.isbn = isbn;
        this.judul = judul;
        this.penulis = penulis;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public double getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public void kurangiStok(int jumlah) {
        if (jumlah > stok) {
            throw new IllegalStateException("Stok tidak cukup untuk buku: " + judul);
        }
        this.stok -= jumlah;
    }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Buku)) return false;
        Buku b = (Buku) o;
        return isbn.equals(b.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
