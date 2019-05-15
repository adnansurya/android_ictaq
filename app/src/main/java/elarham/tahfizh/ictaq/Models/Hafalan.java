package elarham.tahfizh.ictaq.Models;

public class Hafalan {

    public String juzHafalan;
    public String nilaiHafalan;

    public Hafalan(){

    }

    public Hafalan(String juzHafalan, String nilaiHafalan){
        this.juzHafalan = juzHafalan;
        this.nilaiHafalan = nilaiHafalan;
    }

    public String getJuzHafalan() {
        return juzHafalan;
    }

    public void setJuzHafalan(String juzHafalan) {
        this.juzHafalan = juzHafalan;
    }

    public String getNilaiHafalan() {
        return nilaiHafalan;
    }

    public void setNilaiHafalan(String nilaiHafalan) {
        this.nilaiHafalan = nilaiHafalan;
    }
}
