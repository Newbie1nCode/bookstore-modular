#!/bin/bash
# run.sh — Menjalankan aplikasi CLI lewat module path Java.
set -e

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT_DIR"

if [ ! -d "mods/app.ui" ]; then
    echo "Belum dikompilasi. Menjalankan build.sh terlebih dahulu ..."
    ./build.sh
fi

java --module-path mods -m app.ui/com.bookstore.ui.Main
