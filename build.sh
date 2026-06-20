#!/bin/bash
# build.sh — Mengompilasi ketiga modul (app.data -> app.logic -> app.ui)
# ke dalam folder mods/, sesuai dependensi pada module-info.java masing-masing.
set -e

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT_DIR"

rm -rf mods
mkdir -p mods

echo "==> Compiling app.data ..."
javac -d mods/app.data $(find app.data -name "*.java")

echo "==> Compiling app.logic ..."
javac --module-path mods -d mods/app.logic $(find app.logic -name "*.java")

echo "==> Compiling app.ui ..."
javac --module-path mods -d mods/app.ui $(find app.ui -name "*.java")

echo "==> Build selesai. Jalankan dengan: ./run.sh"
