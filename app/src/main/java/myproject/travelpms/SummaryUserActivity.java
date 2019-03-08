package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import Kelas.SharedVariable;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class SummaryUserActivity extends AppCompatActivity {

    CircleImageView imgFoto;
    TextView txtNamaPengguna,txtPaketDilihat,txtPaketDipesan;
    private RecyclerView recyclerView;
    private List<PaketTour> paketTourList;
    DatabaseReference ref,refUser;
    AdapterListPaket adapter;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    Intent i;
    private String keyUser;
    List<String> arrKeyPaket = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_user);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        i = getIntent();
        keyUser = i.getStringExtra("keyUser");

        paketTourList = new ArrayList<>();
        arrKeyPaket.clear();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AdapterListPaket(this,paketTourList);

        txtNamaPengguna = findViewById(R.id.txtNamaPengguna);
        imgFoto = findViewById(R.id.imgFoto);
        txtPaketDilihat = findViewById(R.id.txtPaketDilihat);
        txtPaketDipesan = findViewById(R.id.txtPaketDipesan);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();


        ref.child("users").child(keyUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nama = dataSnapshot.child("displayName").getValue().toString();
                String foto = dataSnapshot.child("foto").getValue().toString();

                txtNamaPengguna.setText(nama);

                if (!foto.equals("no")){
                    Glide.with(SummaryUserActivity.this)
                            .load(foto)
                            .into(imgFoto);
                }else {
                    imgFoto.setImageResource(R.drawable.pemilik_kos);
                }

                pDialogLoading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("users").child(keyUser).child("paketDilihat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    int hitung = 0;
                    for (DataSnapshot child : dataSnapshot.getChildren()){

                        String keyPaket = child.child("keyPaket").getValue().toString();
                        String namaPaket = child.child("namaPaket").getValue().toString();

                        txtPaketDilihat.append("Paket "+namaPaket + " \n");
                        hitung++;
                    }
                    txtPaketDilihat.append("Jumlah : "+hitung);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("users").child(keyUser).child("paketDipesan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    int hitung = 0;
                    for (DataSnapshot child : dataSnapshot.getChildren()){

                        String keyPaket = child.child("keyPaket").getValue().toString();
                        String namaPaket = child.child("namaPaket").getValue().toString();

                        txtPaketDipesan.append("Paket "+namaPaket + " \n");
                        hitung++;
                    }
                    txtPaketDipesan.append("Jumlah : "+hitung);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
