package elarham.tahfizh.ictaq.Models;

public class User {

    public String username, nama, kode;


    public User(){

    }

    public User(String username, String nama, String kode){

        this.username = username;
        this.nama = nama;
        this.kode = kode;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }
}
