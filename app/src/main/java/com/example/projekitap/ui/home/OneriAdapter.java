package com.example.projekitap.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekitap.OneriDuzenle;
import com.example.projekitap.R;
import com.example.projekitap.VeriTabani;

import java.util.List;

public class OneriAdapter  extends ArrayAdapter<kitapClass> { // not bu class ile activity_oneri_adapter.xml'i kitap listesini düzenlemek için oluşturduk...

    VeriTabani vt;
    private Context context;
    int res;

    public   ImageButton ibDetay;

    //public  ArrayList<kitapClass> kitapGetir2;

    HomeFragment hF=new HomeFragment();

    public OneriAdapter(@NonNull Context context, int resource, @NonNull List<kitapClass> objects) {
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


        TextView  tvAd= (TextView)convertView.findViewById(R.id.kitapAdi);
        TextView tvYazar =(TextView) convertView.findViewById(R.id.kitapYazari);
        ibDetay=(ImageButton)  convertView.findViewById(R.id.kitapDetay);

        ibDetay.setId(Integer.valueOf(kitt.getKitapid())); // butonun içine id'yi attık...

        tvAd.setText(isim);
        tvYazar.setText(yazar);



        //----------açılır menü (dikey 3 nokta)---OneriAdapter.java içinde tanımlı

            ibDetay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getContext(), ibDetay);

                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu()); // popup_menu -> res/menu/popup_menu.xml dosyası

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Paylaş")){


                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String shareBody = kitt.getKitadi() + " isimli kitabı okumanızı öneririm";
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Kitap Önerisi");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            ((Activity) getContext()).startActivity(Intent.createChooser(sharingIntent, "Paylaş"));


                         //----listeyi yenile-----burada olmuyor-kendi sayfasında sileceğiz- not olarak kalsın

                           // hF.adapterOneri.clear();
                           // hF.adapterOneri.addAll( hF.kitapGetir);
                          //  hF.adapterOneri.notifyDataSetChanged();

                            //-------

                        }
                        else if(item.getTitle().equals("Düzenle")){


                            Intent intentDuz=new Intent(getContext(), OneriDuzenle.class);

                         intentDuz.putExtra("kid",kitt.getKitapid());
                            intentDuz.putExtra("kisim",kitt.getKitadi());
                            intentDuz.putExtra("kyazar",kitt.getKitapyazar());
                            intentDuz.putExtra("ktarih",kitt.getKitaptarih());
                            intentDuz.putExtra("kkimden",kitt.getKitapkimden());
                            intentDuz.putExtra("kkategorik", kitt.getKitapkategori());


                            ((Activity) getContext()).startActivity(intentDuz); // activity olmayanlar için startActivity kullanımı
                           // context.getApplicationContext().startActivity(intentDuz);
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu



            }
        });

        //--- açılır menü bitti -----------

        //-----ImageView kullanımı----------------- // not!!!!!!!!!! kategoriye göre kitap resmi belirlemek!

       ImageView img = (ImageView) convertView.findViewById(R.id.kitapResim);

        switch(kitt.getKitapkategori()) {
            case "KLASİK":
               img.setImageResource(R.drawable.klasik_resim);
                break;
            case "FANTASTİK":
                img.setImageResource(R.drawable.fantastik);
                break;
            case "BİLİM-KURGU":
                img.setImageResource(R.drawable.bilimkurgu);
                break;
            case "POLİSİYE":
                img.setImageResource(R.drawable.polisiye);
                break;
            case "PSİKOLOJİ":
                img.setImageResource(R.drawable.psikoloji);
                break;
            case "ROMAN":
               img.setImageResource(R.drawable.roman);
                break;
            case "GENÇLİK":
                 img.setImageResource(R.drawable.genclik);
                break;
            case "DİN":
                img.setImageResource(R.drawable.din);
                break;
            case "TARİH":
                img.setImageResource(R.drawable.tarih_resim);
                break;
            case "KİŞİSEL GELİŞİM":
                img.setImageResource(R.drawable.kisisel_gelisim);
                break;
            default:
                img.setImageResource(R.drawable.kitap_icon);
                break;
        }


        //-------------***-----------------------------




        return  convertView;
    }



}