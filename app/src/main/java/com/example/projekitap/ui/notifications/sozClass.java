package com.example.projekitap.ui.notifications;

public class sozClass {



    private int sid,skullanici;
    private String syazi,syazar,starih;

    public sozClass(int sid, String syazi, String syazar, String starih, int skullanici) {
        this.sid = sid;
        this.syazi = syazi;
        this.syazar = syazar;
        this.starih = starih;
        this.skullanici = skullanici;
    }

    public  sozClass(){



    } // Boş constractır Önemli...


    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSyazi() {
        return syazi;
    }

    public void setSyazi(String syazi) {
        this.syazi = syazi;
    }

    public String getSyazar() {
        return syazar;
    }

    public void setSyazar(String syazar) {
        this.syazar = syazar;
    }

    public String getStarih() {
        return starih;
    }

    public void setStarih(String starih) {
        this.starih = starih;
    }

    public int getSkullanici() {
        return skullanici;
    }

    public void setSkullanici(int skullanici) {
        this.skullanici = skullanici;
    }

    @Override
    public String toString(){
        return syazi;
    }

}
