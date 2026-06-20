package com.bookstore.logic.service;

/**
 * DiscountService — aturan bisnis diskon toko buku digital.
 */
public final class DiscountService {

    /**
     * Diskon bertingkat berdasarkan jumlah buku yang dibeli
     * dalam satu transaksi.
     */
    public double hitungPersenDiskon(int jumlah) {
        if (jumlah >= 5) {
            return 0.15;
        } else if (jumlah >= 3) {
            return 0.10;
        } else if (jumlah >= 2) {
            return 0.05;
        }
        return 0.0;
    }
}
