package com.example.projekitap;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekitap.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;


 public class kitapEkle extends AppCompatActivity  {

    private VeriTabani vt;

    private int son_id;

    //---calendar ve alarm işlmeleri----
    final Calendar calendar= Calendar.getInstance();
     Calendar calNow=Calendar.getInstance();
       Calendar calSet=(Calendar) calNow.clone();
    private TimePickerDialog timePickerDialog;
     int islem_kodu=1; // alarmın hangi işlem olduğu
     ///-------

    Button eklekitap;
    EditText isim,yazar,kimden,tarih,kacgun;






    //Spinner---
    Spinner kategori;
    private String[] kateroiArray={"KATEGORİ SEÇ","KLASİK","FANTASTİK","BİLİM-KURGU","POLİSİYE","PSİKOLOJİ","ROMAN","GENÇLİK","DİN","TARİH","KİŞİSEL GELİŞİM"};
    private ArrayAdapter<String> dataAdapterKategori;
    //-----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_ekle);




        vt=new VeriTabani(this); // veritabanı bağlantısı
        isim=(EditText) findViewById(R.id.isim);
        yazar=(EditText) findViewById(R.id.yazar);
        kimden=(EditText) findViewById(R.id.kimden);
        tarih=(EditText) findViewById(R.id.tarih);
        kacgun=(EditText) findViewById(R.id.kacgun);

        eklekitap=(Button) findViewById(R.id.ekleSoz);


        //------ toolbar ayareları
        getSupportActionBar().setDisplayShowTitleEnabled(true); //yazıyı ekle
        getSupportActionBar().setTitle("Öneri Kitap Ekleme Sayfası");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // geri dön iconu. Altta onOptionsItemSelected fonk. var

        //------------------------



        //----------Time Picker ve Alarm ayarlama------------------------------------------------------

        // not1: manifest.xml için-> <receiver android:name=".AlarmRecevier" />
        // <uses-permission android:name="android.permission.WAKE_LOCK" />
        // <uses-permission android:name="android.permission.VIBRATE" />
        //not2: AlarmReceiver.java dosyasını unutma! Onunla bütünleşik çalışıyor... VEritabanı bilgileri intent ile orada olacak


        tarih.setKeyListener(null); // klavye çıkmasın diye...
       // tarih.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+ calendar.get(Calendar.MINUTE)); // seçilenler EditText'e gelsin...



        //------tarihedate picker ekleme------------------
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(); // aşağı fonksiyonu var
            }
        };
        tarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(kitapEkle.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //-----------------------------datepicker bitti----

        //-----------------------------Time Picker ve Tarih bitti------Kalan Kod, kitap ekleme kısmında...


        //Spinner---- xml de spinnerMode=Dialog olmalı
        kategori=(Spinner) findViewById(R.id.kategori);
        dataAdapterKategori =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kateroiArray);
        dataAdapterKategori.setDropDownViewResource(android.R.layout.simple_list_item_single_choice); // görünümü değiştir
        kategori.setAdapter(dataAdapterKategori);
        //----

        //---notify için (AlarReceiver için) gerekli sdk sorgusu-----eklekitap için de lazım yani---

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="KitapTakipKanal";
            String description="Kitap Takip Uygulaması Kanalı";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel=new NotificationChannel("KitapUyari",name,importance);// KitapUyari-> AlarmReceivr içindeki id
            notificationChannel.setDescription(description);

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //------------------notify için gerekli sdk sorgusu bitti-----------


        eklekitap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isim.getText().length()==0 || yazar.getText().length()==0 || kimden.getText().length()==0 || tarih.getText().length()==0 ){

                    Toast.makeText( getApplicationContext(), "Boş Alan Bırakmayınız!", Toast.LENGTH_SHORT).show();

                }

                else {

                    try {

                        int gunGetir= Integer.parseInt(kacgun.getText().toString());

                        kitap_ekle(isim.getText().toString(), yazar.getText().toString(), tarih.getText().toString(),
                                kimden.getText().toString() , kategori.getSelectedItem().toString(),0,0,gunGetir);
                        Toast.makeText(getApplicationContext(), "Yeni Kitabı Başarıyla Eklediniz...", Toast.LENGTH_SHORT).show();

                        isim.setText("");
                        yazar.setText("");
                        tarih.setText("");
                        kimden.setText("");
                        kacgun.setText("");



                    } finally {
                        vt.close();
                    }

                }

            }
        });


    }


    private  void kitap_ekle(String isim,String yazar, String tarih, String kimden, String kategori, int okundu,float kitoy,int kacgun){

        SQLiteDatabase db=vt.getWritableDatabase(); // yazılabilir db oluşturduk
        ContentValues veriler=new ContentValues(); // verileri yazacağımız content
        veriler.put("kitisim",isim); // verileri ekledik (vt'deki sütun adı, burda belirlediğimiz ad)
        veriler.put("kityazar",yazar);
        veriler.put("kittarih",tarih);
        veriler.put("kitkimden",kimden);
        veriler.put("kitkategori",kategori);
        veriler.put("kitokundu",okundu);
        veriler.put("kitoy",kitoy);

        db.insertOrThrow("Kitaplar",null,veriler); //(tablo,null,veriler)

        //---Alarm kısmı----

        Intent intent=new Intent(getBaseContext(), AlarmReceiver.class);  // veritabanından çektiklerini burada intent ile AlarmReceiver'e gönder....
        intent.putExtra("kisim",isim);
        intent.putExtra("kyazar",yazar);
        intent.putExtra("kkimden",kimden);

        //id'yi almamız lazım----

        String sorgu2="SELECT * FROM Kitaplar  ORDER BY kitapid DESC LIMIT 0,1 ";
        Cursor okunanlar2=db.rawQuery(sorgu2, null); // integer ile rawQuery kullanımı
        if(okunanlar2.moveToNext()) { // while olursa moveToNext() kullanılır

            son_id=okunanlar2.getInt(0);
        }
        okunanlar2.close();
        db.close();

        //-- id'yi aldık........

        intent.putExtra("kid",son_id);

        islem_kodu=son_id;


        PendingIntent pendingIntent=PendingIntent.getBroadcast(getBaseContext(),islem_kodu,intent,0);
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + kacgun*60*1000  ,pendingIntent);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // geri dön ok'u fonk.
        switch (item.getItemId()) {
            case android.R.id.home:
                //------listeyi yenile----------
                Intent intent3=new Intent(kitapEkle.this, HomeFragment.class);
                startActivity(intent3);
                //---------------------
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

     private void updateLabel(){ // data picker fonk.
         String myFormat="yyyy-MM-dd HH:mm:ss";
         SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
         tarih.setText(dateFormat.format(calendar.getTime()));
     }

}