package elarham.tahfizh.ictaq.Models;

public class ProvKota {

    public String namaProvKota;
    public String idProvKota;

    public ProvKota(){

    }

    public ProvKota(String id, String nama){
        this.idProvKota = id;
        this.namaProvKota = nama;
    }

    public void setIdProvKota(String idProvKota) {
        this.idProvKota = idProvKota;
    }

    public String getIdProvKota() {
        return idProvKota;
    }

    public void setNamaProvKota(String namaProvKota) {
        this.namaProvKota = namaProvKota;
    }

    public String getNamaProvKota() {
        return namaProvKota;
    }
}
