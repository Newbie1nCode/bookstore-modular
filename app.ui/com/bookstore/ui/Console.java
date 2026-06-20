package com.bookstore.ui;

/**
 * Console — kumpulan kode ANSI dan helper untuk mempercantik tampilan CLI:
 * warna, garis pemisah, dan kotak (box-drawing characters).
 */
final class Console {

    static final String RESET = "\u001B[0m";
    static final String BOLD = "\u001B[1m";
    static final String DIM = "\u001B[2m";

    static final String FG_WHITE = "\u001B[97m";
    static final String FG_GRAY = "\u001B[90m";
    static final String FG_CYAN = "\u001B[96m";
    static final String FG_GREEN = "\u001B[92m";
    static final String FG_YELLOW = "\u001B[93m";
    static final String FG_RED = "\u001B[91m";
    static final String FG_MAGENTA = "\u001B[95m";
    static final String FG_ORANGE = "\u001B[38;5;208m";

    private Console() {
    }

    static String c(String text, String color) {
        return color + text + RESET;
    }

    static void clearScreenHint() {
        // Tidak benar-benar membersihkan terminal (agar tetap portabel),
        // hanya menambah jarak visual antar layar.
        System.out.println();
        System.out.println();
    }

    static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    static void rule(int width) {
        System.out.println(c(repeat("─", width), FG_ORANGE));
    }

    static void doubleRule(int width) {
        System.out.println(c(repeat("═", width), FG_ORANGE));
    }

    static void box(String title) {
        int width = Math.max(48, title.length() + 8);
        System.out.println(c("╔" + repeat("═", width) + "╗", FG_ORANGE));
        int pad = (width - title.length()) / 2;
        System.out.println(c("║", FG_ORANGE) + repeat(" ", pad)
                + c(title, BOLD + FG_WHITE)
                + repeat(" ", width - title.length() - pad) + c("║", FG_ORANGE));
        System.out.println(c("╚" + repeat("═", width) + "╝", FG_ORANGE));
    }
}
