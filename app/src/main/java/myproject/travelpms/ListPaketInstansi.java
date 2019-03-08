package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.client.Firebase;
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
import Kelas.PaketTour;
import Kelas.UserPreference;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListPaketInstansi extends AppCompatActivity {

    private RecyclerView recyclerView;
    AdapterListPaket adapter;
    FloatingActionButton btnTambah;

    String level,paketTour; private List<PaketTour> paketTourList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    UserPreference mUserpref;
    private SweetAlertDialog pDialogLoading,pDialodInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_paket_instansi);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        paketTourList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AdapterListPaket(ListPaketInstansi.this,paketTourList);
        btnTambah = findViewById(R.id.btnCreate);

        mUserpref = new UserPreference(this);
        level = mUserpref.getBagian();
        paketTour = "paket_instansi";

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TambahPaketTour.class);
                startActivity(intent);
            }
        });

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        ref.child("pakettour").child(paketTour).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                paketTourList.clear();
                adapter.notifyDataSetChanged();

                for (DataSnapshot child : dataSnapshot.getChildren()){
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
                            harga,
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
                pDialogLoading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
