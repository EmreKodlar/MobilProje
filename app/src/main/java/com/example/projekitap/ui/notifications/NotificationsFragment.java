package com.example.projekitap.ui.notifications;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projekitap.R;
import com.example.projekitap.VeriTabani;
import com.example.projekitap.databinding.FragmentNotificationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    VeriTabani vt;

    public static int checkileGelenId;

    private String sozKendisi,yazarii;

    //---listview
    public ListView sozlerList;
    public ArrayList<sozClass> sozGetir;
    public SozAdapter adapterSoz;
    sozClass sozClass;
    //----

    //--Fab menuler
    FloatingActionButton fab1,fab2,fab3;
    boolean isFABOpen=false;
    //----
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vt=new VeriTabani(getContext()); // veritabanı bağlantısı

        //------ toolbar ayarları

		 //	search ekleme HomeFragment.java'da detaylı yazıyor...
        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.ust_logo);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);

        //------------------------

        //listView kullanımı----------------------------
        sozlerList = (ListView) root.findViewById(R.id.sozlerList);
        sozGetir=sozListele();
        adapterSoz = new SozAdapter(getContext(),R.layout.activity_soz_adapter,sozGetir);
        sozlerList.setAdapter(adapterSoz);
        sozlerList.setTextFilterEnabled(true); // arama kutusuyla etkileşimli olsun diye
        //-----------------------------------------

        sozlerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Yazar: "+ sozGetir.get(position).getSyazar());
                builder.setMessage( "Söz: "+  sozGetir.get(position).getSyazi() );
                builder.setPositiveButton("OK",null);
                builder.show();


            }
        });

        //---------fab menu------------------
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
         fab1 = (FloatingActionButton) root.findViewById(R.id.fab1);
         fab2 = (FloatingActionButton) root.findViewById(R.id.fab2);
         fab3 = (FloatingActionButton) root.findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();

                    fab1.setOnClickListener(new View.OnClickListener() { // ekle butonu
                        @Override
                        public void onClick(View v) {
                            Intent ekleint=new Intent(getContext(),sozEkle.class);
                            startActivity(ekleint);
                        }
                    });

                    fab2.setOnClickListener(new View.OnClickListener() { //duzenle butonu
                        @Override
                        public void onClick(View v) {


                            SQLiteDatabase db45= vt.getWritableDatabase();
                            String sorgu22="SELECT * FROM Sozler WHERE sozid =?";
                            String[] secimler22 = {String.valueOf(checkileGelenId)};
                            Cursor okunanlar22=db45.rawQuery(sorgu22, secimler22);

                            Intent duzint=new Intent(getContext(),sozDuzenle.class);
                            duzint.putExtra("sozId", checkileGelenId);


                            if(okunanlar22.moveToFirst()) {

                                duzint.putExtra("sozYaziii", okunanlar22.getString(1));
                                duzint.putExtra("sozYazarrr", okunanlar22.getString(2));
                            }
                            okunanlar22.close();
                            db45.close();
                            startActivity(duzint);

                        }
                    });

                    fab3.setOnClickListener(new View.OnClickListener() { // sil butonu
                        @Override
                        public void onClick(View v) {

                            //---silme

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Söz Silme Paneli");
                            builder.setMessage("Bu Sözü Silmek İstediğinize Emin misiniz?");
                            builder.setNegativeButton("Hayır", null);
                            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int evet) {

                                    // silme 2 satır ;)

                                    SQLiteDatabase db =vt.getWritableDatabase();

                                    db.delete("Sozler", "sozid = ?"  , new String[]{String.valueOf(checkileGelenId)});

                                    //listeyi yenileyecez mecburen

                                    sozGetir=sozListele();
                                    adapterSoz = new SozAdapter(getContext(),R.layout.activity_soz_adapter,sozGetir);
                                    sozlerList.setAdapter(adapterSoz);
                                    sozlerList.setChoiceMode(sozlerList.CHOICE_MODE_SINGLE); // Checkbox tek seçim yapsın!!!

                                    Toast.makeText( getContext(), "Seçili Söz Başarıyla Silindi", Toast.LENGTH_SHORT).show();


                                }
                            });

                            builder.show();

                            //--silme bitti


                        }
                    });


                }else{
                    closeFABMenu();
                }
            }
        });
        //-----




        return root;
    }


    public ArrayList<sozClass> sozListele(){

        ArrayList<sozClass> veriler=new ArrayList<sozClass>();
        SQLiteDatabase db4= vt.getWritableDatabase();

        String sorgu2="SELECT * FROM Sozler ";

        Cursor okunanlar2=db4.rawQuery(sorgu2, null);


        while(okunanlar2.moveToNext()) { // while olursa moveToNext() kullanılır

            sozClass= new sozClass(okunanlar2.getInt(0),okunanlar2.getString(1),okunanlar2.getString(2),okunanlar2.getString(3),okunanlar2.getInt(4));
            veriler.add(sozClass);
        }
        okunanlar2.close();
        db4.close();
        return veriler;
    }


    //--------fab menu fonksionları--------------

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55)); // res->values->dimen.xml içine tanımladık...
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }

    //-----------fab menu fonksionları bitti-----------


    //---- searchview tasarımı------------------

    /*Not: adapteri çektiğin sozClass.java ya
    @Override
    public String toString() {
        return syazi;
    }
    ekledik. Yoksa Çalışmıyordu. String olarak görmesini saladık...
    */
    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.toolbar_notifications,menu); // italik yazan toolbar_notifications.xml ordan çekiyoruz...
        MenuItem menuItem=menu.findItem(R.id.action_search); //  menu.xml içindeki action_search
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Söz Ara...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //arama kısmı

                 adapterSoz.getFilter().filter(newText);


                return false;
            }
        });



    }

    //---search view bitti-----------

    //---diğer ikonlar---toolbar ayarı--------

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id_buton=item.getItemId();

        if(id_buton==R.id.action_delete){
            //---silme

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Söz Silme Paneli");
            builder.setMessage("Bu Sözü Silmek İstediğinize Emin misiniz?");
            builder.setNegativeButton("Hayır", null);
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int evet) {

                    // silme 2 satır ;)

                    SQLiteDatabase db =vt.getWritableDatabase();

                    db.delete("Sozler", "sozid = ?"  , new String[]{String.valueOf(checkileGelenId)});

                    //listeyi yenileyecez mecburen

                    sozGetir=sozListele();
                    adapterSoz = new SozAdapter(getContext(),R.layout.activity_soz_adapter,sozGetir);
                    sozlerList.setAdapter(adapterSoz);
                    sozlerList.setChoiceMode(sozlerList.CHOICE_MODE_SINGLE); // Checkbox tek seçim yapsın!!!

                    //-------------



                    Toast.makeText( getContext(), "Seçili Söz Başarıyla Silindi", Toast.LENGTH_SHORT).show();


                }
            });

            builder.show();

            //--silme bitti
        }

        if(id_buton==R.id.action_share){


            SQLiteDatabase db45= vt.getWritableDatabase();
            String sorgu22="SELECT * FROM Sozler WHERE sozid =?";
            String[] secimler22 = {String.valueOf(checkileGelenId)};
            Cursor okunanlar22=db45.rawQuery(sorgu22, secimler22);


            if(okunanlar22.moveToFirst()) {

                sozKendisi=okunanlar22.getString(1);
                yazarii=okunanlar22.getString(2);
            }
            okunanlar22.close();
            db45.close();


            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = sozKendisi + " - " + yazarii ;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Kitap Önerisi");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            ((Activity) getContext()).startActivity(Intent.createChooser(sharingIntent, "Paylaş"));


        }

        return super.onOptionsItemSelected(item);
    }

    //---toolbar ayarı bitti-----------

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}