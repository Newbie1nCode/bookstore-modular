package com.bookstore.data.entity;

/**
 * Entitas Kategori buku.
 */
public final class Kategori {

    private final String id;
    private final String nama;

    public Kategori(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    @Override
    public String toString() {
        return nama;
    }
}
