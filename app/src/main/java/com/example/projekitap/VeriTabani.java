package com.example.projekitap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VeriTabani extends SQLiteOpenHelper {

    public static final String VERITABANI_ADI = "TakipProje";
    private static final int VERITABANI_VERSIYONU = 11;

    //Tablo isimleri
    public static final String Kullanici = "Kullanici"; // Tablo1
    public static final String Kitaplar = "Kitaplar"; // Tablo2
    public static final String Sozler = "Sozler"; // Tablo3
    public static final String EKitap = "EKitap"; // Tablo4


    //sütun isimleri

    public static final String kid = "kid"; // Tablo1
    public static final String kisim = "kisim";
    public static final String ksifre = "ksifre";
    public static final String kmail = "kmail";
    public static final String kresim = "kresim";
    public static final String kitapid = "kitapid"; // Tablo2 -> Kitaplar
    public static final String kitisim = "kitisim";
    public static final String kityazar = "kityazar";
    public static final String kittarih = "kittarih";
    public static final String kitkimden= "kitkimden";
    public static final String kitkategori= "kitkategori";
    public static final String kitokundu= "kitokundu";
    public static final String kitoy= "kitoy";
    //public static final String kitkullanici = "kitkullanici";
    public static final String sozid = "sozid"; // Tablo3
    public static final String sozyazi = "sozyazi";
    public static final String sozyazar = "sozyazar";
    public static final String soztarih = "soztarih";
    public static final String sozkullanici = "sozkullanici";
    public static final String eid = "eid"; // Tablo4
    public static final String eyazi = "eyazi";
    public static final String ebaslik = "ebaslik";
    public static final String etarih = "etarih";
    public static final String ekullanici = "ekullanici";

    // Tablo oluştur


    String tablo1="CREATE TABLE "+
            Kullanici+" (" + kid + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            kisim +" Text,"+
            ksifre +" Text," +
            kmail +" Text," +
            kresim +" BLOB)";


    String tablo2="CREATE TABLE "+
            Kitaplar+" (" + kitapid + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            kitisim +" Text,"+
            kityazar +" Text," +
            kittarih +" Text," +
            kitkimden +" Text," +
            kitkategori+" Text," +
            kitokundu+" INTEGER," +
            kitoy +" FLOAT)";

    String tablo3="CREATE TABLE "+
            Sozler+" (" + sozid + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            sozyazi +" Text,"+
            sozyazar +" Text," +
            soztarih +" Text," +
            sozkullanici +" INTEGER)";

    String tablo4="CREATE TABLE "+
            EKitap+" (" + eid + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            eyazi +" Text,"+
            ebaslik +" Text," +
            etarih +" Text," +
            ekullanici +" INTEGER)";


    public  VeriTabani(Context context) { // önemli!!!
        super(context, VERITABANI_ADI, null, VERITABANI_VERSIYONU);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { // tablolar burda oluşturuluyor


        // İlk tablomuzun adı Kullanici
        sqLiteDatabase.execSQL(tablo1);

        //İkinci Tablo Kitaplar
        sqLiteDatabase.execSQL(tablo2);

        //3. Tablo Sozler
        sqLiteDatabase.execSQL(tablo3);

        //4. Tablo EKitap
        sqLiteDatabase.execSQL(tablo4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int eskiVersiyon, int yeniVersiyon) { // tablo düzenlemeleri burada

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Kullanici");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Kitaplar");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Sozler");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS EKitap");
        onCreate(sqLiteDatabase);
    }
}



