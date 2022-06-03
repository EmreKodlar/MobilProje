package com.example.projekitap.ui.home;

public class kitapClass {


    private int kitapid;
    private String kitadi;
    private String kitapyazar;
    private String kitaptarih;
    private String kitapkimden;
    private String kitapkategori;
    private int kitapokundu;
    private float kitapoy;


    public kitapClass(int kitapid, String kitadi, String kitapyazar, String kitaptarih, String kitapkimden, String kitapkategori,
                      int kitapokundu,float kitapoy ) {
        this.kitapid = kitapid;
        this.kitadi = kitadi;
        this.kitapyazar = kitapyazar;
        this.kitaptarih = kitaptarih;
        this.kitapkimden = kitapkimden;
        this.kitapkategori = kitapkategori;
        this.kitapokundu=kitapokundu;
        this.kitapoy=kitapoy;
    }
    public kitapClass(){}

    public int getKitapid() {
        return kitapid;
    }

    public void setKitapid(int kitapid) {
        this.kitapid = kitapid;
    }

    public String getKitadi() {
        return kitadi;
    }

    public void setKitadi(String kitadi) {
        this.kitadi = kitadi;
    }

    public String getKitapyazar() {
        return kitapyazar;
    }

    public void setKitapyazar(String kitapyazar) {
        this.kitapyazar = kitapyazar;
    }

    public String getKitaptarih() {
        return kitaptarih;
    }

    public void setKitaptarih(String kitaptarih) {
        this.kitaptarih = kitaptarih;
    }

    public String getKitapkimden() {
        return kitapkimden;
    }

    public void setKitapkimden(String kitapkimden) {
        this.kitapkimden = kitapkimden;
    }

    public String getKitapkategori() {
        return kitapkategori;
    }

    public void setKitapkategori(String kitapkategori) {
        this.kitapkategori = kitapkategori;
    }

    public int getKitapokundu() {
        return kitapokundu;
    }

    public void setKitapokundu(int kitapokundu) {
        this.kitapokundu = kitapokundu;
    }

    public float getKitapoy() {
        return kitapoy;
    }

    public void setKitapoy(float kitapoy) {
        this.kitapoy = kitapoy;
    }

    @Override
    public String toString() {
        return kitadi;
    }

}
