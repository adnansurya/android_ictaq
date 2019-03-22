package elarham.tahfizh.ictaq.Models;

public class Surah {

    public String nomor, nama, asma, ayat, type, arti;

    public Surah(){

    }

    public Surah(String nomor, String nama, String asma, String ayat, String type, String arti) {
        this.nomor = nomor;
        this.nama = nama;
        this.asma = asma;
        this.ayat = ayat;
        this.type = type;
        this.arti = arti;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAsma() {
        return asma;
    }

    public void setAsma(String asma) {
        this.asma = asma;
    }

    public String getAyat() {
        return ayat;
    }

    public void setAyat(String ayat) {
        this.ayat = ayat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArti() {
        return arti;
    }

    public void setArti(String arti) {
        this.arti = arti;
    }
}
