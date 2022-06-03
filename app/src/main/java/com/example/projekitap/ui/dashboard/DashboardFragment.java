package com.example.projekitap.ui.dashboard;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projekitap.R;
import com.example.projekitap.VeriTabani;
import com.example.projekitap.databinding.FragmentDashboardBinding;
import com.example.projekitap.ui.home.kitapClass;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private VeriTabani vt;
    //---listview
    public ListView okunanList;
    public ArrayList<kitapClass> okunanGetir;
    public OkunanAdapter adapterOkunan;
    //----
    //Spinner---
    Spinner kategoriOkunan;
    private String[] okunanArray={"KATEGORİ SEÇ","KLASİK","FANTASTİK","BİLİM-KURGU","POLİSİYE","PSİKOLOJİ","ROMAN","GENÇLİK","DİN","TARİH","KİŞİSEL GELİŞİM"};
    public ArrayAdapter<String> dataAdapterOkunan;
    //-----

    kitapClass kclass;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vt=new VeriTabani(getContext()); // veritabanı bağlantısı

        //------ toolbar ayarları---search butonu home fragmentte yazıyor...

        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.ust_logo);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);

        //------------------------


        //listView kullanımı---------------------------

        okunanList = (ListView) root.findViewById(R.id.okunanList);
        okunanGetir = okunanListele();
        adapterOkunan = new OkunanAdapter(getContext(), R.layout.activity_okunan_adapter, okunanGetir);
        okunanList.setAdapter(adapterOkunan);
        okunanList.setTextFilterEnabled(true); // arama kutusuyla etkileşimli olsun diye

        okunanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //--işlemler

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Okunan Kitap Bilgileri");
                builder.setMessage("Kitap Adı: "+ okunanGetir.get(position).getKitadi()+"\n\n"+
                        "Yazar Adı: "+ okunanGetir.get(position).getKitapyazar() +"\n\n"+
                        "Öneren Kişi: "+ okunanGetir.get(position).getKitapkimden() +"\n\n"+
                        "Kategori: "+ okunanGetir.get(position).getKitapkategori() +"\n\n"+
                        "Hatırlatma Tarihi: "+ okunanGetir.get(position).getKitaptarih() +"\n\n"+
                        "Oy Oranı: "+ okunanGetir.get(position).getKitapoy()
                );
                builder.setNegativeButton("Okunmadı", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int duz) {

                        //OkunMAdı yeri

                        SQLiteDatabase db =vt.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("kitokundu", 0); // DB içindeki kitokundu=1 oldu...


                        // updating row
                        db.update("Kitaplar", values, "kitapid  = ? ", new String[] { String.valueOf(okunanGetir.get(position).getKitapid()) });

                        Toast.makeText( getContext(), "Kitap OkunMAdı olarak İşaretlendi!", Toast.LENGTH_SHORT).show();
                        db.close();

                        //listeyi yenileyecez mecburen
                        okunanGetir = okunanListele();
                        adapterOkunan = new OkunanAdapter(getContext(), R.layout.activity_okunan_adapter, okunanGetir);
                        okunanList.setAdapter(adapterOkunan);

                        adapterOkunan.notifyDataSetChanged();

                        //Okundu bitti-----------------------------------------------------------------

                    }
                });
                builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int sil) {

                        // silme yeri-----------------------------------

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(okunanGetir.get(position).getKitadi() + " isimli bu kitabı");
                        builder.setMessage("Silmek İstediğinize Emin misiniz?");
                        builder.setNegativeButton("Hayır", null);
                        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int evet) {

                                //silme işlemi 2 satır
                                Toast.makeText(getContext(), okunanGetir.get(position).getKitadi()  + " Silindi!" , Toast.LENGTH_SHORT).show();
                                SQLiteDatabase db =vt.getWritableDatabase();
                                db.delete("Kitaplar", "kitapid = ?"  , new String[]{String.valueOf(okunanGetir.get(position).getKitapid())});

                                //listeyi yenileyecez mecburen
                                okunanGetir = okunanListele();
                                adapterOkunan = new OkunanAdapter(getContext(), R.layout.activity_okunan_adapter, okunanGetir);
                                okunanList.setAdapter(adapterOkunan);
                                adapterOkunan.notifyDataSetChanged();

                            }
                        });

                        builder.show();
                    }
                });

                builder.show();



                //--işlemler bitti
            }
        });
        //-------------------------***----------------------------------------------------------------



        //Spinner---- xml de spinnerMode=Dialog olmalı
        kategoriOkunan=(Spinner) root.findViewById(R.id.kategoriOkunan);
        dataAdapterOkunan =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, okunanArray);
        dataAdapterOkunan.setDropDownViewResource(android.R.layout.simple_list_item_single_choice); // görünümü değiştir
        kategoriOkunan.setAdapter(dataAdapterOkunan);

        //--spinner arama--

        kategoriOkunan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String secimm=kategoriOkunan.getItemAtPosition(position).toString();
                if(secimm.equals("KATEGORİ SEÇ")){ // normal filtrele
                    okunanGetir = okunanListele();
                    adapterOkunan = new OkunanAdapter(getContext(), R.layout.activity_okunan_adapter, okunanGetir);
                    okunanList.setAdapter(adapterOkunan);
                    adapterOkunan.notifyDataSetChanged();
                }
                else { // kategoriye göre filtrele
                    okunanGetir = spinnerListele2(secimm);
                    adapterOkunan = new OkunanAdapter(getContext(), R.layout.activity_okunan_adapter, okunanGetir);
                    okunanList.setAdapter(adapterOkunan);
                    adapterOkunan.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //---***-----spinner bitti-----------------------------------


        return root;
    }




    public ArrayList<kitapClass> okunanListele(){

        ArrayList<kitapClass> veriler=new ArrayList<>();
        SQLiteDatabase db4= vt.getWritableDatabase();

        String sorgu2="SELECT * FROM Kitaplar WHERE kitokundu =?";

        String[] secimler2 = {String.valueOf(1)}; // kitokundu=0 olanlar önerilerde gösterilecek... 1 olanlar okundu da

        Cursor okunanlar2=db4.rawQuery(sorgu2, secimler2); // integer ile rawQuery kullanımı



        while(okunanlar2.moveToNext()) { // while olursa moveToNext() kullanılır

            kclass= new kitapClass(okunanlar2.getInt(0),okunanlar2.getString(1),okunanlar2.getString(2),
                    okunanlar2.getString(3),okunanlar2.getString(4),okunanlar2.getString(5),
                    okunanlar2.getInt(6),okunanlar2.getFloat(7));
            veriler.add(kclass);
        }
        okunanlar2.close();
        db4.close();
        return veriler;
    }



    public ArrayList<kitapClass> spinnerListele2(String kategori){

        ArrayList<kitapClass> veriler=new ArrayList<>();
        SQLiteDatabase db4= vt.getWritableDatabase();

        String sorgu2="SELECT * FROM Kitaplar WHERE kitokundu =? AND kitkategori=?";

        String[] secimler2 = {String.valueOf(1), String.valueOf(kategori)}; // kitokundu=0 olanlar önerilerde gösterilecek... 1 olanlar okundu da

        Cursor okunanlar2=db4.rawQuery(sorgu2, secimler2); // integer ile rawQuery kullanımı



        while(okunanlar2.moveToNext()) { // while olursa moveToNext() kullanılır

            kclass= new kitapClass(okunanlar2.getInt(0),okunanlar2.getString(1),okunanlar2.getString(2),
                    okunanlar2.getString(3),okunanlar2.getString(4),okunanlar2.getString(5),
                    okunanlar2.getInt(6),okunanlar2.getFloat(7));
            veriler.add(kclass);
        }
        okunanlar2.close();
        db4.close();
        return veriler;
    }



//---- searchview fragmnet için yapılan------------------

    /*Not: adapteri çektiğin kitapClass.java ya
    @Override
    public String toString() {
        return kitadi;
    }
    ekledik. Yoksa Çalışmıyordu. String olarak görmesini saladık...
    */
    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu,menu); // italik yazan menu.xml
        MenuItem menuItem=menu.findItem(R.id.action_search); //  menu.xml içindeki action_search
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Kitap Adı Ara...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //arama kısmı

                adapterOkunan.getFilter().filter(newText);


                return false;
            }
        });
    }


    //---search view bitti-----------

    //------alaram fonksiyonu

    public void alarmCalis(){



    }



    ///-----------***---------------

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}