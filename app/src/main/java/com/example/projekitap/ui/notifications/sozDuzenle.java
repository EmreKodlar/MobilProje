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

public class sozDuzenle extends AppCompatActivity {

    private VeriTabani vt;

    private Button duzenleSoz;
    private EditText sozYaziAl,sozYazarAl;
    private String yazi_al,yazar_al;
    private  int  idSoz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soz_duzenle);

        vt=new VeriTabani(this); // veritabanı bağlantısı
        sozYaziAl=(EditText) findViewById(R.id.sozYaziAl);
        sozYazarAl=(EditText) findViewById(R.id.sozYazarAl);
        duzenleSoz=(Button) findViewById(R.id.duzenleSoz);

        //----intent al---
        Intent gelenIntent=getIntent();
        idSoz = gelenIntent.getIntExtra("sozId",0);
        yazi_al = gelenIntent.getStringExtra("sozYaziii");
        yazar_al = gelenIntent.getStringExtra("sozYazarrr");

        //--------------

        sozYaziAl.setText(yazi_al);
        sozYazarAl.setText(yazar_al);

        //--düzenleme

        duzenleSoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sozYaziAl.getText().length()==0 || sozYazarAl.getText().length()==0  ){

                    Toast.makeText(getApplicationContext(), "Boş Alan Bırakmayınız!", Toast.LENGTH_SHORT).show();
                }
                else{

                    SQLiteDatabase db =vt.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("sozyazi", sozYaziAl.getText().toString()); // DB içindeki sütun isimleri
                    values.put("sozyazar", sozYazarAl.getText().toString());

                    db.update("Sozler", values, "sozid  = ? ", new String[] { String.valueOf(idSoz) });

                    Toast.makeText( getApplicationContext(), "Söz Başarıyla Düzenlendi...", Toast.LENGTH_SHORT).show();
                    db.close();

                }

            }
        });

        //---***------

        //------ toolbar ayareları
        getSupportActionBar().setDisplayShowTitleEnabled(true); //yazıyı ekle
        getSupportActionBar().setTitle("Söz Düzenleme Sayfası");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // geri dön iconu. Altta onOptionsItemSelected fonk. var

        //------------------------

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // geri dön ok'u fonk.
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent31=new Intent(sozDuzenle.this, NotificationsFragment.class);
                startActivity(intent31);

                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}