<div align="center">

<!-- ASCII Art Banner SVG -->
<img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=700&size=14&duration=4000&pause=2000&color=F97316&center=true&vCenter=true&multiline=true&repeat=false&width=700&height=100&lines=%E2%96%88%E2%96%88%E2%96%88%E2%96%88+%E2%96%88%E2%96%88%E2%96%88%E2%96%88+%E2%96%88%E2%96%88%E2%96%88%E2%96%88+%E2%96%88%E2%96%88%E2%96%88+%E2%96%88+%E2%96%88%E2%96%88%E2%96%88%E2%96%88+%E2%96%88%E2%96%88%E2%96%88%E2%96%88+%E2%96%88+%E2%96%88+%E2%96%88%E2%96%88%E2%96%88%E2%96%88;Sistem+Manajemen+Toko+Buku+Digital+%E2%80%94+Arsitektur+Modular+JPMS" alt="BookStore" />

<br/>

<img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=500&size=18&duration=2500&pause=800&color=F97316&center=true&vCenter=true&width=600&lines=%F0%9F%93%9A+Sistem+Manajemen+Toko+Buku+Digital;%E2%9A%99%EF%B8%8F+Java+Platform+Module+System+(JPMS);%F0%9F%94%92+Strong+Encapsulation+%E2%80%94+Tanpa+Maven%2FGradle;%F0%9F%96%A5%EF%B8%8F+CLI+Interaktif+dengan+Warna+ANSI" alt="Typing animation" />

<br/><br/>

<!-- Badges -->
<p>
  <img src="https://img.shields.io/badge/Java-21-EA8C00?style=flat-square&logo=openjdk&logoColor=white" />
  &nbsp;
  <img src="https://img.shields.io/badge/Build-javac_+_java-3B82F6?style=flat-square&logo=gnu-bash&logoColor=white" />
  &nbsp;
  <img src="https://img.shields.io/badge/JPMS-Module_System-16A34A?style=flat-square" />
  &nbsp;
  <img src="https://img.shields.io/badge/Maven%2FGradle-Tidak_Dipakai-DC2626?style=flat-square" />
  &nbsp;
  <img src="https://img.shields.io/badge/Status-Selesai-16A34A?style=flat-square&logo=checkmarx&logoColor=white" />
</p>

<p><sub>Proyek tugas praktikum <b>Studi Kasus Modular</b> — dibangun murni dari CLI menggunakan Java Platform Module System (JPMS).</sub></p>

</div>

---

## 📖 Daftar Isi

| # | Bagian |
|---|--------|
| 1 | [🧩 Struktur Modul](#-struktur-modul) |
| 2 | [🔒 Strong Encapsulation](#-strong-encapsulation) |
| 3 | [🗂️ Struktur Folder](#️-struktur-folder) |
| 4 | [🚀 Cara Menjalankan](#-cara-menjalankan) |
| 5 | [🖥️ Fitur CLI](#️-fitur-cli) |
| 6 | [✅ Ketentuan Tugas](#-ketentuan-tugas) |
| 7 | [👥 Anggota Kelompok](#-anggota-kelompok) |

---

## 🧩 Struktur Modul

Sistem dipecah menjadi **3 modul independen**, masing-masing dengan tanggung jawab tunggal:

<div align="center">

| Modul | 🎯 Tanggung Jawab | 📤 Mengekspor |
|:---:|:---|:---|
| `app.data` | Entitas data (`Buku`, `Kategori`) & simulasi database internal (`InMemoryDatabase`) | `com.bookstore.data.entity`<br>`com.bookstore.data.repository` |
| `app.logic` | Logika bisnis: diskon, hitung total, validasi stok | `com.bookstore.logic.service`<br>`com.bookstore.logic.dto` |
| `app.ui` | CLI interaktif, menerima input pengguna, **main entry point** | *(tidak mengekspor apa pun)* |

</div>

### 🔗 Diagram Dependensi

```mermaid
flowchart LR
    A["📦 app.data\nEntitas & Repository"] -->|"requires transitive"| B["⚙️ app.logic\nBusiness Logic"]
    B -->|"requires"| C["🖥️ app.ui\nCLI Entry Point"]

    style A fill:#1D4ED8,stroke:#93C5FD,stroke-width:2px,color:#fff
    style B fill:#15803D,stroke:#86EFAC,stroke-width:2px,color:#fff
    style C fill:#9333EA,stroke:#D8B4FE,stroke-width:2px,color:#fff
```

### ⚙️ Konfigurasi `module-info.java`

```java
// ── app.data ──────────────────────────────────
module app.data {
    exports com.bookstore.data.entity;
    exports com.bookstore.data.repository;
    // com.bookstore.data.internal → TIDAK diekspor 🔒
}

// ── app.logic ─────────────────────────────────
module app.logic {
    requires transitive app.data;
    exports com.bookstore.logic.service;
    exports com.bookstore.logic.dto;
}

// ── app.ui ────────────────────────────────────
module app.ui {
    requires app.logic;
}
```

---

## 🔒 Strong Encapsulation

> ⛔ `app.ui` **tidak boleh** mengakses `com.bookstore.data.internal` secara langsung.

Paket `com.bookstore.data.internal` — tempat `InMemoryDatabase` berada — tidak pernah diekspor. Java Module System menegakkan *strong encapsulation*: kompilasi **langsung gagal** jika ada modul lain yang mencoba mengimpornya.

```text
error: package com.bookstore.data.internal is not visible
  (package com.bookstore.data.internal is declared in module
   app.data, which does not export it)
```

### 🛡️ Rantai Komunikasi yang Benar

```mermaid
flowchart LR
    UI["🖥️ app.ui"] -->|"✅ panggil"| SVC["⚙️ BookService\nfasad app.logic"]
    SVC -->|"✅ panggil"| REPO["📚 BukuRepository\nAPI publik app.data"]
    REPO -->|"✅ panggil internal"| DB["🔒 InMemoryDatabase\ntidak diekspor"]
    UI -. "❌ DILARANG — gagal kompilasi" .-> DB

    style UI fill:#7E22CE,stroke:#D8B4FE,color:#fff
    style SVC fill:#15803D,stroke:#86EFAC,color:#fff
    style REPO fill:#1D4ED8,stroke:#93C5FD,color:#fff
    style DB fill:#1F2937,stroke:#EF4444,stroke-width:2px,color:#9CA3AF
    linkStyle 3 stroke:#EF4444,stroke-dasharray:5 5,stroke-width:2px
```

---

## 🗂️ Struktur Folder

```
bookstore-modular/
│
├── 📦 app.data/
│   ├── module-info.java
│   └── com/bookstore/data/
│       ├── entity/          ← Buku, Kategori
│       ├── repository/      ← BukuRepository  (API publik ✅)
│       └── internal/        ← InMemoryDatabase (tersembunyi 🔒)
│
├── ⚙️  app.logic/
│   ├── module-info.java
│   └── com/bookstore/logic/
│       ├── service/         ← BookService, DiscountService
│       └── dto/             ← HasilTransaksi
│
├── 🖥️  app.ui/
│   ├── module-info.java
│   └── com/bookstore/ui/
│       ├── Main.java        ← entry point CLI
│       └── Console.java     ← helper tampilan ANSI
│
├── build.sh
└── run.sh
```

---

## 🚀 Cara Menjalankan

> **Prasyarat:** JDK 11 atau lebih baru — *diuji pada JDK 21*

### Opsi 1 — Script (Disarankan)

```bash
chmod +x build.sh run.sh
./build.sh   # kompilasi 3 modul ke mods/
./run.sh     # jalankan CLI
```

### Opsi 2 — Manual via CLI

```bash
# 1. Kompilasi app.data
javac -d mods/app.data \
  $(find app.data -name "*.java")

# 2. Kompilasi app.logic (butuh app.data)
javac --module-path mods \
  -d mods/app.logic \
  $(find app.logic -name "*.java")

# 3. Kompilasi app.ui (butuh app.logic)
javac --module-path mods \
  -d mods/app.ui \
  $(find app.ui -name "*.java")

# 4. Jalankan
java --module-path mods \
  -m app.ui/com.bookstore.ui.Main
```

---

## 🖥️ Fitur CLI

Tampilan CLI menggunakan ANSI color codes untuk pengalaman terminal yang nyaman:

```
╔════════════════════════════════════════════╗
║               MENU UTAMA                   ║
╠════════════════════════════════════════════╣
║  [1]  Tampilkan semua buku                 ║
║  [2]  Cari buku berdasarkan kategori       ║
║  [3]  Beli buku                            ║
║  [4]  Tambah buku baru                     ║
║  [0]  Keluar                               ║
╚════════════════════════════════════════════╝
  › Pilih menu:
```

| Fitur | Deskripsi |
|:---:|:---|
| 📋 **Katalog** | Tabel rapi dengan warna ANSI — stok menipis ditampilkan merah |
| 💳 **Beli Buku** | Hitung diskon bertingkat otomatis (5% / 10% / 15%), cetak struk, validasi & kurangi stok via `app.logic` |
| ➕ **Tambah Buku** | Tambah entri katalog baru secara *runtime* |

---

## ✅ Ketentuan Tugas

- [x] Setiap modul memiliki `module-info.java` yang terkonfigurasi benar
- [x] `app.ui` tidak mengakses paket internal `app.data` secara langsung *(terverifikasi via error kompilasi)*
- [x] Format pengumpulan: unggah repo ke GitHub, tautkan ke LMS
- [x] Kelompok maksimal 3 mahasiswa

---

## 👥 Anggota Kelompok

<div align="center">

| No | Nama | NIM |
|:---:|:---|:---|
| 1 | Dimas Maycardo Sihotang | 202333500844 |
| 2 | Rianca Aril Pratama | 202333500851 |
| 3 | Muhammad Jalaluddin Gassing | 202333500858 |

<br/>

<img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&size=13&duration=3000&pause=1000&color=6B7280&center=true&vCenter=true&width=400&lines=Dibangun+dengan+%E2%98%95+Java+%2B+JPMS;Praktikum+Pemrograman+Modular" alt="Footer" />

</div>
