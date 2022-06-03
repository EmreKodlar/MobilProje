package com.example.projekitap;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

public class OneriDuzenle extends AppCompatActivity {

    private VeriTabani vt;

    final Calendar kmyCalendar= Calendar.getInstance();

    private Button duzenle;
    private EditText kisim,kyazar,kkimden,ktarih;


    //Spinner---
    private Spinner kkategori22;
    private String[] kkateroiArray={"KATEGORİ SEÇ","KLASİK","FANTASTİK","BİLİM-KURGU","POLİSİYE","PSİKOLOJİ","ROMAN","GENÇLİK","DİN","TARİH","KİŞİSEL GELİŞİM"};
    private ArrayAdapter<String> kdataAdapterKategori;
    //-----



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneri_duzenle);

        vt=new VeriTabani(this); // veritabanı bağlantısı
        kisim=(EditText) findViewById(R.id.kisim);
        kyazar=(EditText) findViewById(R.id.kyazar);
        kkimden=(EditText) findViewById(R.id.kkimden);
        ktarih=(EditText) findViewById(R.id.ktarih);

        duzenle=(Button) findViewById(R.id.duzenle);

        //Spinner---- xml de spinnerMode=Dialog olmalı
        kkategori22=(Spinner) findViewById(R.id.kkategori2);
        kdataAdapterKategori =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kkateroiArray);
        kdataAdapterKategori.setDropDownViewResource(android.R.layout.simple_list_item_single_choice); // görünümü değiştir
        kkategori22.setAdapter(kdataAdapterKategori);
        //----

        //----intent al---
        Intent gelenIntent=getIntent();
        String is = gelenIntent.getStringExtra("kisim");
        String  ya = gelenIntent.getStringExtra("kyazar");
        String ta = gelenIntent.getStringExtra("ktarih");
        String  ki = gelenIntent.getStringExtra("kkimden");
        String ka = gelenIntent.getStringExtra("kkategorik");
        int id = gelenIntent.getIntExtra("kid",0);
        //--------------

        kisim.setText(is);
        kyazar.setText(ya);
        ktarih.setText(ta);
        kkimden.setText(ki);

        //--- spinner içindeki veriyi alma---- bu kadar zor işte ----
        int spinnerPosition = kdataAdapterKategori.getPosition(ka);
        kkategori22.setSelection(spinnerPosition);
        //------------





        //--düzenleme

        duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(kisim.getText().length()==0 || kyazar.getText().length()==0 ||
                        ktarih.getText().length()==0 || kkimden.getText().length()==0 ){

                    Toast.makeText(getApplicationContext(), "Boş Alan Bırakmayınız!", Toast.LENGTH_SHORT).show();
                }
                else{



                    SQLiteDatabase db =vt.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("kitisim", kisim.getText().toString()); // DB içindeki sütun isimleri
                    values.put("kityazar", kyazar.getText().toString());
                    values.put("kittarih", ktarih.getText().toString()); // DB içindeki sütun isimleri
                    values.put("kitkimden", kkimden.getText().toString());
                    values.put("kitkategori", kkategori22.getSelectedItem().toString()); // DB içindeki sütun isimleri


                    kkategori22.setSelection(kkategori22.getSelectedItemPosition());

                    // updating row
                    db.update("Kitaplar", values, "kitapid  = ? ", new String[] { String.valueOf(id) });

                    Toast.makeText( getApplicationContext(), "Öneri Kitap Başarıyla Düzenlendi...", Toast.LENGTH_SHORT).show();
                    db.close();

                }

            }
        });

        //---***------



        //------ toolbar ayareları
        getSupportActionBar().setDisplayShowTitleEnabled(true); //yazıyı ekle
        getSupportActionBar().setTitle("Düzenleme Sayfası");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // geri dön iconu. Altta onOptionsItemSelected fonk. var

        //------------------------

        //------tarihedate picker ekleme------------------
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                kmyCalendar.set(Calendar.YEAR, year);
                kmyCalendar.set(Calendar.MONTH,month);
                kmyCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(); // aşağı fonksiyonu var
            }
        };
        ktarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(OneriDuzenle.this,date,kmyCalendar.get(Calendar.YEAR),kmyCalendar.get(Calendar.MONTH),kmyCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //-----------------------------datepicker bitti----




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // geri dön ok'u fonk.
        switch (item.getItemId()) {
            case android.R.id.home:
                //------listeyi yenile----------
                Intent intent3=new Intent(OneriDuzenle.this, HomeFragment.class);
                // intent3222ab.putExtra("kullaniciKimlik", kullaniciKimlik);
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
        ktarih.setText(dateFormat.format(kmyCalendar.getTime()));
    }
}