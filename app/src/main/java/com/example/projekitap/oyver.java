package com.example.projekitap;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekitap.ui.dashboard.DashboardFragment;

public class oyver extends AppCompatActivity {

    private VeriTabani vt;
    RatingBar ratingBar1;
    Button oyla;
    TextView kitis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyver);

        //------ toolbar ayareları
        getSupportActionBar().setDisplayShowTitleEnabled(true); //yazıyı ekle
        getSupportActionBar().setTitle("Oy Verme Sayfası");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // geri dön iconu. Altta onOptionsItemSelected fonk. var

        //------------------------

        vt=new VeriTabani(this); // veritabanı bağlantısı
        ratingBar1=(RatingBar) findViewById(R.id.ratingBar2);
        kitis=(TextView) findViewById(R.id.kitis);
        oyla=(Button) findViewById(R.id.oyla);

        //----intent al---
        Intent gelenInt=getIntent();

        int idAl = gelenInt.getIntExtra("idAl",0);

        String isimAl = gelenInt.getStringExtra("isimAl");

        float oyAl = gelenInt.getFloatExtra("oyAl",0);
        //--------------

        ratingBar1.setRating(oyAl);
        kitis.setText(isimAl);

        oyla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db =vt.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("kitoy", ratingBar1.getRating()); // DB içindeki kitoy'a rating değeri ekledik


                // updating row
                db.update("Kitaplar", values, "kitapid  = ? ", new String[] { String.valueOf(idAl) });

                Toast.makeText( oyver.this, "Kitap Oy'u Güncellendi!", Toast.LENGTH_SHORT).show();
                db.close();

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // geri dön ok'u fonk.
        switch (item.getItemId()) {
            case android.R.id.home:
                //------listeyi yenile----------
                Intent intent33=new Intent(oyver.this, DashboardFragment.class);

                startActivity(intent33);
                //---------------------
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}