package com.bookstore.logic.dto;

import com.bookstore.data.entity.Buku;

/**
 * HasilTransaksi — DTO (Data Transfer Object) yang membungkus hasil
 * perhitungan transaksi pembelian sehingga app.ui tidak perlu
 * mengetahui detail internal logika diskon/hitung total.
 */
public final class HasilTransaksi {

    private final Buku buku;
    private final int jumlah;
    private final double subtotal;
    private final double diskon;
    private final double totalAkhir;

    public HasilTransaksi(Buku buku, int jumlah, double subtotal, double diskon, double totalAkhir) {
        this.buku = buku;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
        this.diskon = diskon;
        this.totalAkhir = totalAkhir;
    }

    public Buku getBuku() {
        return buku;
    }

    public int getJumlah() {
        return jumlah;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiskon() {
        return diskon;
    }

    public double getTotalAkhir() {
        return totalAkhir;
    }
}
