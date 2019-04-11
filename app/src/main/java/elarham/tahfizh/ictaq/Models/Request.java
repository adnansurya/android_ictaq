package elarham.tahfizh.ictaq.Models;

public class Request {

    public String id, idRegis, idPenguji, tanggal, status;

    public Request(){

    }

    public Request(String id, String idRegis, String idPenguji, String tanggal, String status){

        this.id = id;
        this.idRegis = idRegis;
        this.idPenguji = idPenguji;
        this.tanggal = tanggal;
        this.status = status;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRegis() {
        return idRegis;
    }

    public void setIdRegis(String idRegis) {
        this.idRegis = idRegis;
    }

    public String getIdPenguji() {
        return idPenguji;
    }

    public void setIdPenguji(String idPenguji) {
        this.idPenguji = idPenguji;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
