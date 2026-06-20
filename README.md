<div align="center">

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&size=28&duration=3000&pause=1000&color=2E86C1&center=true&vCenter=true&width=600&lines=BookStore+%E2%80%94+Sistem+Modular;Java+Platform+Module+System+(JPMS);Strong+Encapsulation+%E2%80%A2+Tanpa+Maven%2FGradle" alt="Typing SVG" />

# 📚 BookStore
### Sistem Manajemen Toko Buku Digital — Arsitektur Modular

<p>
  <img src="https://img.shields.io/badge/Java-11%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Build-javac%20%2B%20java-blue?style=for-the-badge&logo=apache-ant&logoColor=white" />
  <img src="https://img.shields.io/badge/Tested%20on-JDK%2021-007396?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Module%20System-JPMS-success?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Maven%2FGradle-Tidak%20Dipakai-red?style=for-the-badge" />
</p>

<sub>Proyek tugas praktikum <b>Studi Kasus Modular</b> — dibangun murni dari CLI menggunakan Java Platform Module System (JPMS).</sub>

</div>

<br>

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.gif" width="100%" height="4px">

## 📖 Daftar Isi

- [🧩 Struktur Modul](#-struktur-modul)
- [🔒 Strong Encapsulation](#-strong-encapsulation--larangan-akses-internal)
- [🗂️ Struktur Folder](#️-struktur-folder)
- [🚀 Cara Menjalankan](#-cara-menjalankan)
- [🖥️ Fitur CLI](#️-fitur-cli)
- [✅ Ketentuan Tugas](#-ketentuan-tugas-yang-dipenuhi)
- [👥 Anggota Kelompok](#-anggota-kelompok)

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.gif" width="100%" height="4px">

## 🧩 Struktur Modul

Sistem dipecah menjadi **3 modul independen**, masing-masing dengan tanggung jawab tunggal:

<div align="center">

| Modul | 🎯 Tanggung Jawab | 📤 Mengekspor |
|:---:|:---|:---|
| **`app.data`** | Entitas data (`Buku`, `Kategori`) & simulasi database internal (`InMemoryDatabase`) | `com.bookstore.data.entity`<br>`com.bookstore.data.repository` |
| **`app.logic`** | Logika bisnis: diskon, hitung total, validasi stok | `com.bookstore.logic.service`<br>`com.bookstore.logic.dto` |
| **`app.ui`** | CLI interaktif, menerima input pengguna, **main entry point** | *(tidak mengekspor apa pun)* |

</div>

### 🔗 Diagram Alur Dependensi

```mermaid
flowchart LR
    A["📦 app.data<br/><sub>Entitas & Repository</sub>"] -->|requires transitive| B["⚙️ app.logic<br/><sub>Business Logic</sub>"]
    B -->|requires| C["🖥️ app.ui<br/><sub>CLI Entry Point</sub>"]

    style A fill:#1f6feb,stroke:#fff,stroke-width:2px,color:#fff
    style B fill:#2ea043,stroke:#fff,stroke-width:2px,color:#fff
    style C fill:#bf3989,stroke:#fff,stroke-width:2px,color:#fff
```

### ⚙️ Konfigurasi `module-info.java`

```java
// app.data
module app.data {
    exports com.bookstore.data.entity;
    exports com.bookstore.data.repository;
}

// app.logic
module app.logic {
    requires transitive app.data;
    exports com.bookstore.logic.service;
    exports com.bookstore.logic.dto;
}

// app.ui
module app.ui {
    requires app.logic;
}
```

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.gif" width="100%" height="4px">

## 🔒 Strong Encapsulation — Larangan Akses Internal

> ⛔ **`app.ui` TIDAK BOLEH mengakses `app.data.internal`**

Paket `com.bookstore.data.internal` — tempat `InMemoryDatabase` berada — **tidak pernah diekspor** oleh `app.data`. Karena `app.ui` hanya `requires app.logic` dan tidak pernah mengimpor paket internal tersebut, Java Module System menegakkan **strong encapsulation**: kompilasi akan **gagal** jika ada kode di `app.ui` (atau modul lain mana pun) mencoba mengimpor `com.bookstore.data.internal.*` secara langsung.

Ini sudah diverifikasi. Mencoba mengimpor paket itu dari modul lain menghasilkan:

```text
error: package com.bookstore.data.internal is not visible
  (package com.bookstore.data.internal is declared in module app.data, which does not export it)
```

### 🛡️ Rantai Komunikasi yang Benar

```mermaid
flowchart LR
    UI["🖥️ app.ui"] -->|"✅ panggil"| SVC["⚙️ BookService<br/><sub>fasad app.logic</sub>"]
    SVC -->|"✅ panggil"| REPO["📚 BukuRepository<br/><sub>API publik app.data</sub>"]
    REPO -->|"✅ panggil"| DB["🔒 InMemoryDatabase<br/><sub>internal, tersembunyi</sub>"]

    UI -.->|"❌ DILARANG — gagal kompilasi"| DB

    style UI fill:#bf3989,stroke:#fff,color:#fff
    style SVC fill:#2ea043,stroke:#fff,color:#fff
    style REPO fill:#1f6feb,stroke:#fff,color:#fff
    style DB fill:#444,stroke:#f33,stroke-width:2px,color:#fff
    linkStyle 3 stroke:#f33,stroke-width:2px,stroke-dasharray: 5 5
```

`app.ui` hanya berbicara dengan `BookService` (fasad di `app.logic`), yang di baliknya memanggil `BukuRepository` (API publik `app.data`) → `InMemoryDatabase` (internal, tersembunyi).

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.gif" width="100%" height="4px">

## 🗂️ Struktur Folder

<details open>
<summary><b>📂 Klik untuk lihat/sembunyikan struktur lengkap</b></summary>

```text
bookstore-modular/
├── app.data/
│   ├── module-info.java
│   └── com/bookstore/data/
│       ├── entity/        # Buku, Kategori
│       ├── repository/    # BukuRepository — API publik
│       └── internal/      # InMemoryDatabase — TIDAK diekspor 🔒
│
├── app.logic/
│   ├── module-info.java
│   └── com/bookstore/logic/
│       ├── service/       # BookService, DiscountService
│       └── dto/           # HasilTransaksi
│
├── app.ui/
│   ├── module-info.java
│   └── com/bookstore/ui/
│       ├── Main.java       # entry point CLI
│       └── Console.java    # helper tampilan ANSI
│
├── build.sh
└── run.sh
```

</details>

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.gif" width="100%" height="4px">

## 🚀 Cara Menjalankan

> 💡 Butuh **JDK 11+** (memakai fitur module system) — *tested di JDK 21*

### Opsi 1 — Pakai Script (disarankan)

```bash
chmod +x build.sh run.sh
./build.sh   # kompilasi 3 modul secara berurutan ke mods/
./run.sh     # jalankan CLI
```

### Opsi 2 — Manual via CLI

```bash
javac -d mods/app.data $(find app.data -name "*.java")
javac --module-path mods -d mods/app.logic $(find app.logic -name "*.java")
javac --module-path mods -d mods/app.ui $(find app.ui -name "*.java")
java  --module-path mods -m app.ui/com.bookstore.ui.Main
```

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.gif" width="100%" height="4px">

## 🖥️ Fitur CLI

```text
╔════════════════════════════════════════════════════════════════╗
║                         MENU UTAMA                              ║
╚════════════════════════════════════════════════════════════════╝
  [1]  Tampilkan semua buku
  [2]  Cari buku berdasarkan kategori
  [3]  Beli buku
  [4]  Tambah buku baru
  [0]  Keluar
```

| Fitur | Deskripsi |
|---|---|
| 📋 **Katalog** | Tabel rapi dengan warna ANSI — stok menipis ditampilkan **merah** |
| 💳 **Beli Buku** | Otomatis hitung diskon bertingkat (5% / 10% / 15%), cetak struk, validasi & kurangi stok lewat `app.logic` |
| ➕ **Tambah Buku** | Menambah entri katalog baru secara *runtime* |

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.gif" width="100%" height="4px">

## ✅ Ketentuan Tugas yang Dipenuhi

- [x] Setiap modul punya `module-info.java` terkonfigurasi benar
- [x] `app.ui` tidak mengakses paket internal `app.data` secara langsung *(terverifikasi gagal-kompilasi jika dicoba)*
- [x] Format pengumpulan: unggah repo ini ke GitHub, lalu tautannya ke LMS
- [x] Kelompok maksimal 3 mahasiswa — isi nama anggota di bawah ini

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.gif" width="100%" height="4px">

## 👥 Anggota Kelompok

<div align="center">

| No | Nama | NIM |
|:---:|:---|:---|
| 1 | _Dimas Maycardo Sihotang_ | _202333500844_ |
| 2 | _Rianca Aril Pratama_ | _202333500851_ |
| 3 | _Muhammad Jalaluddin Gassing_ | _2023335008585_ |

</div>

<br>

<div align="center">
<sub>Dibangun dengan ☕ dan Java Platform Module System</sub>
</div>
