package com.example.projekitap.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekitap.R;
import com.example.projekitap.VeriTabani;
import com.example.projekitap.oyver;
import com.example.projekitap.ui.home.kitapClass;

import java.util.List;

public class OkunanAdapter extends ArrayAdapter<kitapClass> {

    VeriTabani vt;
    private Context context;
    int res;

    private ImageButton ibDetay;
    private TextView tvAd ;

    private RatingBar ratingBar1;


    public OkunanAdapter(@NonNull Context context, int resource, @NonNull List<kitapClass> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res= resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        vt=new VeriTabani(getContext()); // veritabanı bağlantısı

        //kisi bilgileri getir
        int kitapid=getItem(position).getKitapid();
        String isim = getItem(position).getKitadi();
        String yazar = getItem(position).getKitapyazar();
        String tarih = getItem(position).getKitaptarih();
        String kitapkimden=getItem(position).getKitapkimden();
        String kitapkategori=getItem(position).getKitapkategori();
        int kitapokundu=getItem(position).getKitapokundu();
        float kitapoy=getItem(position).getKitapoy();

        //Bilgileri ile birlikte yeni bir kişi oluşturun -- verileri almak için kullanacağız...
        kitapClass kitt=new kitapClass(kitapid,  isim,  yazar,  tarih,  kitapkimden, kitapkategori,kitapokundu,kitapoy);


        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(res,parent,false);


        tvAd= (TextView)convertView.findViewById(R.id.kitapAdi2);
        ratingBar1 =(RatingBar) convertView.findViewById(R.id.ratingBar1);
        ibDetay=(ImageButton)  convertView.findViewById(R.id.kitapDetay2);

        ibDetay.setId(Integer.valueOf(kitt.getKitapid())); // butonun içine id'yi attık...

        tvAd.setText(isim + " - " + yazar);
        ratingBar1.setRating(kitapoy);


        //----------açılır menü (dikey 3 nokta)---OneriAdapter.java içinde tanımlı

        ibDetay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getContext(), ibDetay);

                popup.getMenuInflater().inflate(R.menu.popup_menu_okunan, popup.getMenu()); // popup_menu -> res/menu/popup_menu.xml dosyası

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Paylaş")){


                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String shareBody = kitt.getKitadi() + " isimli kitabı okumanızı öneririm";
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Kitap Önerisi");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            ((Activity) getContext()).startActivity(Intent.createChooser(sharingIntent, "Paylaş"));

                        }
                        else if(item.getTitle().equals("Oy Ver")){

                            Intent oyintent=new Intent(getContext(), oyver.class);
                            oyintent.putExtra("idAl",kitt.getKitapid());
                            oyintent.putExtra("oyAl",kitt.getKitapoy());
                            oyintent.putExtra("isimAl",kitt.getKitadi());

                            ((Activity) getContext()).startActivity(oyintent);

                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu



            }
        });

        //--- açılır menü bitti -----------





        return  convertView;
    }


}
