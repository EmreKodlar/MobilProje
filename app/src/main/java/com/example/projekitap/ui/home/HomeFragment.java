package com.example.projekitap.ui.home;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.projekitap.databinding.FragmentHomeBinding;
import com.example.projekitap.kitapEkle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


   // public  int  listeyiYenile;

    private VeriTabani vt;
    //---listview
    public   ListView kitapList;
    public   ArrayList<kitapClass> kitapGetir;
    public   OneriAdapter adapterOneri;
    //----
    //Spinner---
    Spinner kategoriKitap;
    private String[] kateroiArray={"KATEGORİ SEÇ","KLASİK","FANTASTİK","BİLİM-KURGU","POLİSİYE","PSİKOLOJİ","ROMAN","GENÇLİK","DİN","TARİH","KİŞİSEL GELİŞİM"};
    public ArrayAdapter<String> dataAdapterKategori;
    //-----

    kitapClass kclass;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container,   Bundle savedInstanceState) {


        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);


        View root = binding.getRoot();

        vt=new VeriTabani(getContext()); // veritabanı bağlantısı

        //------ toolbar ayarları

		 /*		search ekleme
1-menu klasörüne menu.xml aç-> içeriklerini gir
2-search'ü ekleyeceğin fragmnet'te (Burda HomeFragment.java) onCreatOptionsMenu()4
 fonksioynunu çalıştır. ana fonk. dışına olacak! içine kodları yaz.
3- onCreatOptionsMenu() içinde listview   adapeter'i   ayarla (aranacak kelime apaterden çekilisn diye...)
4-  setHasOptionsMenu(true); bunu onCerate() içine koy...
*/
        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.ust_logo);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);

        //------------------------



        //listView kullanımı---------------------------

            kitapList = (ListView) root.findViewById(R.id.kitapList);
            kitapGetir = oneriListele();
            adapterOneri = new OneriAdapter(getContext(), R.layout.activity_oneri_adapter, kitapGetir); //activity_oneri_adapter-> özelleştirilecek xml'in adı
            kitapList.setAdapter(adapterOneri);
            kitapList.setTextFilterEnabled(true); // arama kutusuyla etkileşimli olsun diye

         kitapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 //--işlemler

                 AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                 builder.setTitle("Önerilen Kitap Bilgileri");
                 builder.setMessage("Kitap Adı: "+ kitapGetir.get(position).getKitadi()+"\n\n"+
                         "Yazar Adı: "+ kitapGetir.get(position).getKitapyazar() +"\n\n"+
                         "Öneren Kişi: "+ kitapGetir.get(position).getKitapkimden() +"\n\n"+
                         "Kategori: "+ kitapGetir.get(position).getKitapkategori() +"\n\n"+
                         "Hatırlatma Tarihi: "+ kitapGetir.get(position).getKitaptarih()

                 );
                 builder.setNegativeButton("Okundu", new DialogInterface.OnClickListener() {


                     @Override
                     public void onClick(DialogInterface dialogInterface, int duz) {

                         //Okundu yeri

                         SQLiteDatabase db =vt.getWritableDatabase();

                         ContentValues values = new ContentValues();
                         values.put("kitokundu", 1); // DB içindeki kitokundu=1 oldu...


                         // updating row
                         db.update("Kitaplar", values, "kitapid  = ? ", new String[] { String.valueOf(kitapGetir.get(position).getKitapid()) });

                         Toast.makeText( getContext(), "Kitap Okundu olarak İşaretlendi!", Toast.LENGTH_SHORT).show();
                         db.close();

                         //listeyi yenileyecez mecburen
                         kitapGetir = oneriListele();
                         adapterOneri = new OneriAdapter(getContext(), R.layout.activity_oneri_adapter, kitapGetir);
                         kitapList.setAdapter(adapterOneri);

                         adapterOneri.notifyDataSetChanged();

                         //Okundu bitti-----------------------------------------------------------------

                     }
                 });
                 builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int sil) {

                         // silme yeri-----------------------------------

                         AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                         builder.setTitle(kitapGetir.get(position).getKitadi() + " isimli bu kitabı");
                         builder.setMessage("Silmek İstediğinize Emin misiniz?");
                         builder.setNegativeButton("Hayır", null);
                         builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int evet) {

                                 //silme işlemi 2 satır
                                 Toast.makeText(getContext(), kitapGetir.get(position).getKitadi()  + " Silindi!" , Toast.LENGTH_SHORT).show();
                                 SQLiteDatabase db =vt.getWritableDatabase();
                                 db.delete("Kitaplar", "kitapid = ?"  , new String[]{String.valueOf(kitapGetir.get(position).getKitapid())});

                                 //listeyi yenileyecez mecburen
                                 kitapGetir = oneriListele();
                                 adapterOneri = new OneriAdapter(getContext(), R.layout.activity_oneri_adapter, kitapGetir);
                                 kitapList.setAdapter(adapterOneri);
                                 adapterOneri.notifyDataSetChanged();

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



        //Fab menu------
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentekle=new Intent(getContext(), kitapEkle.class);
                startActivity(intentekle);
            }
        });
        //----

        //Spinner---- xml de spinnerMode=Dialog olmalı
        kategoriKitap=(Spinner) root.findViewById(R.id.kategoriKitap);
        dataAdapterKategori =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, kateroiArray);
        dataAdapterKategori.setDropDownViewResource(android.R.layout.simple_list_item_single_choice); // görünümü değiştir
        kategoriKitap.setAdapter(dataAdapterKategori);

        //--spinner arama--

        kategoriKitap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String secimmm=kategoriKitap.getItemAtPosition(position).toString();
                if(secimmm.equals("KATEGORİ SEÇ")){ // normal filtrele
                    kitapGetir = oneriListele();
                    adapterOneri = new OneriAdapter(getContext(), R.layout.activity_oneri_adapter, kitapGetir);
                    kitapList.setAdapter(adapterOneri);
                    adapterOneri.notifyDataSetChanged();
                }
                else { // kategoriye göre filtrele
                    kitapGetir = spinnerListele(secimmm);
                    adapterOneri = new OneriAdapter(getContext(), R.layout.activity_oneri_adapter, kitapGetir);
                    kitapList.setAdapter(adapterOneri);
                    adapterOneri.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //---***-----spinner bitti-----------------------------------




        return root;
    }





    public ArrayList<kitapClass> oneriListele(){

        ArrayList<kitapClass> veriler=new ArrayList<>();
        SQLiteDatabase db4= vt.getWritableDatabase();

        String sorgu2="SELECT * FROM Kitaplar WHERE kitokundu =?";

        String[] secimler2 = {String.valueOf(0)}; // kitokundu=0 olanlar önerilerde gösterilecek... 1 olanlar okundu da

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



    public ArrayList<kitapClass> spinnerListele(String kategori){

        ArrayList<kitapClass> veriler=new ArrayList<>();
        SQLiteDatabase db4= vt.getWritableDatabase();

        String sorgu2="SELECT * FROM Kitaplar WHERE kitokundu =? AND kitkategori=?";

        String[] secimler2 = {String.valueOf(0), String.valueOf(kategori)}; // kitokundu=0 olanlar önerilerde gösterilecek... 1 olanlar okundu da

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
    public void onCreateOptionsMenu(  Menu menu, @NonNull MenuInflater inflater) {
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

                adapterOneri.getFilter().filter(newText);


                return false;
            }
        });
    }


    //---search view bitti-----------




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}