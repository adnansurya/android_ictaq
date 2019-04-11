package elarham.tahfizh.ictaq.Models;

public class Jadwal {

    public String id, idReq, tanggal, jam, mulai, nilai, catatan;

    public Jadwal(){

    }

    public Jadwal( String id, String idReq, String tanggal, String jam, String mulai, String nilai, String catatan){
        this.id = id;
        this.idReq = idReq;
        this.tanggal = tanggal;
        this.jam = jam;
        this.mulai = mulai;
        this.nilai = nilai;
        this.catatan = catatan;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdReq() {
        return idReq;
    }

    public void setIdReq(String idReq) {
        this.idReq = idReq;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getMulai() {
        return mulai;
    }

    public void setMulai(String mulai) {
        this.mulai = mulai;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
