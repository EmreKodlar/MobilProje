package com.example.projekitap.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekitap.R;
import com.example.projekitap.VeriTabani;

import java.util.ArrayList;

public class SozAdapter extends ArrayAdapter<sozClass>  {

    VeriTabani vt;
    private Context context;
    int res;

    int selected_position=-1;

    String sozyazi,sozyazar,soztarih;

    int sozid,sozkullanici;


    public SozAdapter(@NonNull Context context, int resource, @NonNull ArrayList<sozClass> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res= resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //kisi bilgileri getir
        sozid=getItem(position).getSid();
        sozyazi = getItem(position).getSyazi();
        sozyazar = getItem(position).getSyazar();
        soztarih = getItem(position).getStarih();
        sozkullanici=getItem(position).getSkullanici();

        vt=new VeriTabani(getContext()); // veritabanı bağlantısı


        //Bilgileri ile birlikte yeni bir kişi oluşturun
        sozClass kitt=new sozClass(sozid,  sozyazi,  sozyazar,  soztarih,  sozkullanici);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(res,parent,false);

        TextView sozYazari = convertView.findViewById(R.id.sozYazari);
        TextView sozunKendisi = convertView.findViewById(R.id.sozunKendisi);
        TextView sozTarih = convertView.findViewById(R.id.sozTarih);

        CheckBox secimler=convertView.findViewById(R.id.secimler);

        secimler.setId(Integer.valueOf(sozid));  //Checkox id alsın...

        //--Cehckbox kullanımı-------------------------------------------------------------------

        // tek seçim yapmak için------------
        if(selected_position==position)
        {
            secimler.setChecked(true);

        }
        else
        {
            secimler.setChecked(false);

        }
        //------------------------------------

        secimler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                NotificationsFragment eki=new NotificationsFragment();

                if(secimler.isChecked()){

                    selected_position= position;
                    eki.checkileGelenId=secimler.getId(); // seçilince id döndürsün...


                }
                else
                {
                    selected_position=-1;
                }


                notifyDataSetChanged();


            }
        });

        //---------checbox kullanımı bitti---------------


        if(sozyazi.length()>42){

            String yeniYazi= sozyazi.substring(0,42) + " ... ";

            sozunKendisi.setText(yeniYazi);

        }
        else{

            sozunKendisi.setText(sozyazi);

        }

        sozYazari.setText(sozyazar);

        sozTarih.setText(soztarih);



        return  convertView;
    }



}
