package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterListPaket;
import Adapter.AdapterPaketTerlaris;
import Kelas.PaketTour;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LaporanTerlarisActivity extends AppCompatActivity {

    private String jenisPaket;
    Intent i;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    private RecyclerView recyclerView;
    AdapterPaketTerlaris adapter;
    private List<PaketTour> paketTourList;
    private int jmlDipesan;
    Task initTask;
    private String jmlDipesanPaket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_terlaris);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        i = getIntent();
        jenisPaket = i.getStringExtra("jenis");

        paketTourList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AdapterPaketTerlaris(LaporanTerlarisActivity.this,paketTourList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();


        ref.child("pakettour").child(jenisPaket).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                paketTourList.clear();
                adapter.notifyDataSetChanged();

                for (DataSnapshot child : dataSnapshot.getChildren()){

                    if (!child.child("jmlDipesanPaket").exists()){
                         jmlDipesanPaket = "0";
                        String nama = child.child("namaPaket").getValue().toString();
                        String status = child.child("statusPaket").getValue().toString();
                        String downloadURL = child.child("downloadUrl").getValue().toString();
                        String durasi = child.child("durasiPaket").getValue().toString();
                        String harga = child.child("hargaPaket").getValue().toString();
                        String jmlPeserta = child.child("jumlahPeserta").getValue().toString();
                        String key = child.child("key").getValue().toString();
                        String fasilitas = child.child("fasilitasPaket").getValue().toString();


                        PaketTour paketTour = new PaketTour(
                                nama,
                                jmlDipesanPaket,
                                durasi,
                                jmlPeserta,
                                fasilitas,
                                key,
                                downloadURL,
                                status
                        );
                        paketTourList.add(paketTour);
                        adapter.notifyDataSetChanged();
                    }else {

                        String nama = child.child("namaPaket").getValue().toString();
                        String status = child.child("statusPaket").getValue().toString();
                        String downloadURL = child.child("downloadUrl").getValue().toString();
                        String durasi = child.child("durasiPaket").getValue().toString();
                        String harga = child.child("hargaPaket").getValue().toString();
                        String jmlPeserta = child.child("jumlahPeserta").getValue().toString();
                        String key = child.child("key").getValue().toString();
                        String fasilitas = child.child("fasilitasPaket").getValue().toString();
                         jmlDipesanPaket = child.child("jmlDipesanPaket").getValue().toString();

                        PaketTour paketTour = new PaketTour(
                                nama,
                                jmlDipesanPaket,
                                durasi,
                                jmlPeserta,
                                fasilitas,
                                key,
                                downloadURL,
                                status
                        );
                        paketTourList.add(paketTour);
                        adapter.notifyDataSetChanged();
                    }

                }
                pDialogLoading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
