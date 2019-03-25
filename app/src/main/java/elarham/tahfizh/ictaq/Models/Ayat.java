package elarham.tahfizh.ictaq.Models;

public class Ayat {

    public String arabic, english, indo, nomorAyat;

    public Ayat(){

    }

    public Ayat(String arabic, String english, String indo, String nomorAyat){
        this.arabic = arabic;
        this.english =
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

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getNomorAyat() {
        return nomorAyat;
    }

    public void setNomorAyat(String nomorAyat) {
        this.nomorAyat = nomorAyat;
    }

}
