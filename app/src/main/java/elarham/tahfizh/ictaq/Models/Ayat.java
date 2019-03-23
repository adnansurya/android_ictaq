package elarham.tahfizh.ictaq.Models;

public class Ayat {

    public String arabic, lafaz, indo, nomorAyat;

    public Ayat(){

    }

    public Ayat(String arabic, String lafaz, String indo, String nomorAyat){
        this.arabic = arabic;
        this.lafaz = lafaz;
        this.indo = indo;
        this.nomorAyat = nomorAyat;
    }

    public String getArabic(){
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getIndo() {
        return indo;
    }

    public void setIndo(String indo) {
        this.indo = indo;
    }

    public String getLafaz() {
        return lafaz;
    }

    public void setLafaz(String lafaz) {
        this.lafaz = lafaz;
    }

    public String getNomorAyat() {
        return nomorAyat;
    }

    public void setNomorAyat(String nomorAyat) {
        this.nomorAyat = nomorAyat;
    }
}
