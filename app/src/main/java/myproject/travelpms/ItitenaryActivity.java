package myproject.travelpms;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterItitenary;
import Adapter.AdapterListPaket;
import Kelas.Ititenary;
import Kelas.PaketTour;
import Kelas.SharedVariable;
import Kelas.UserPreference;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItitenaryActivity extends AppCompatActivity {

    FloatingActionButton btnTambah;
    private RecyclerView recyclerView;
    AdapterItitenary adapter;
    private List<Ititenary> ititenaryList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    String level,paketTour;
    UserPreference mUserpref;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    Intent i;
    private String keyPaket;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ititenary);

        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        i = getIntent();
        keyPaket = i.getStringExtra("key");

        ititenaryList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new AdapterItitenary(ItitenaryActivity.this,ititenaryList);
        btnTambah = findViewById(R.id.btnCreate);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TambahItinerary.class);
                intent.putExtra("key",keyPaket);
                startActivity(intent);
            }
        });
        if (!SharedVariable.email.equals("admin@gmail.com")){
            btnTambah.setVisibility(View.GONE);
        }

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        ref.child("pakettour").child(SharedVariable.paket).child(keyPaket).child("itineraryList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ititenaryList.clear();
                adapter.notifyDataSetChanged();

                if (!dataSnapshot.exists()){

                    new SweetAlertDialog(ItitenaryActivity.this,SweetAlertDialog.NORMAL_TYPE)
                            .setContentText("Data Kosong")
                            .setConfirmText("Ok")
                            .show();
                }else {

                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String namaTempat = child.child("namaTempat").getValue().toString();
                        String alamat = child.child("alamat").getValue().toString();

                        Ititenary ititenary = new Ititenary(
                                namaTempat,alamat
                        );

                        ititenaryList.add(ititenary);
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
