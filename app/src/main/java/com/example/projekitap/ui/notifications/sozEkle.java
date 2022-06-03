package com.example.projekitap.ui.notifications;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekitap.R;
import com.example.projekitap.VeriTabani;

import java.text.SimpleDateFormat;
import java.util.Date;

public class sozEkle extends AppCompatActivity {

    private VeriTabani vt;

    private EditText sozYazar,sozYazi;
    private Button ekleSoz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soz_ekle);

        vt=new VeriTabani(this); // veritabanı bağlantısı
        //------ toolbar ayareları
        getSupportActionBar().setDisplayShowTitleEnabled(true); //yazıyı ekle
        getSupportActionBar().setTitle("Söz Ekleme Sayfası");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // geri dön iconu. Altta onOptionsItemSelected fonk. var

        //------------------------

        sozYazar=(EditText)findViewById(R.id.sozYazar);
        sozYazi=(EditText) findViewById(R.id.sozYazi);
        ekleSoz=(Button) findViewById(R.id.ekleSoz);

        ekleSoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sozYazar.getText().length()==0 || sozYazi.getText().length()==0 ){

                    Toast.makeText( getApplicationContext(), "Boş Alan Bırakmayınız!!!!!!", Toast.LENGTH_SHORT).show();

                }

                else {

                    try {


                        SimpleDateFormat sekil = new SimpleDateFormat();
                        Date tarihh = new Date();


  soz_ekle(sozYazi.getText().toString(), sozYazar.getText().toString(), sekil.format(tarihh));

  Toast.makeText(getApplicationContext(), "Yeni Sözü  Başarıyla Eklediniz...", Toast.LENGTH_SHORT).show();

                        sozYazar.setText("");
                        sozYazi.setText("");
                    } finally {
                        vt.close();
                    }


                }
            }
        });
    }

    private  void soz_ekle(String yazi,String yazar, String tarih){

        SQLiteDatabase db=vt.getWritableDatabase(); // yazılabilir db oluşturduk
        ContentValues veriler=new ContentValues(); // verileri yazacağımız content
        veriler.put("sozyazi",yazi); // verileri ekledik (vt'deki sütun adı, burda belirlediğimiz ad)
        veriler.put("sozyazar",yazar);
        veriler.put("soztarih",tarih);
        veriler.put("sozkullanici",1);

        db.insertOrThrow("Sozler",null,veriler); //(tablo,null,veriler)

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // geri dön ok'u fonk.
        switch (item.getItemId()) {
            case android.R.id.home:
                //------listeyi yenile----------
                Intent intent33=new Intent(sozEkle.this, NotificationsFragment.class);
                // intent3222ab.putExtra("kullaniciKimlik", kullaniciKimlik);
                startActivity(intent33);
                //---------------------
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}