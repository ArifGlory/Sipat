package myproject.travelpms;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

import Adapter.AdapterListPesanan;
import Kelas.Pesanan;
import Kelas.SharedVariable;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListPesananUser extends AppCompatActivity {

    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private RecyclerView recyclerView;
    AdapterListPesanan adapter;
    private List<Pesanan> listPesanan;
    private SweetAlertDialog pDialogLoading,pDialodInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pesanan_user);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
        ref = FirebaseDatabase.getInstance().getReference();

        listPesanan = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AdapterListPesanan(this,listPesanan);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        ref.child("pesanan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPesanan.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String idUser = child.child("idUser").getValue().toString();
                    String idPaket = child.child("idPaket").getValue().toString();
                    String status = child.child("status").getValue().toString();
                    String jenisPaket = child.child("jenisPaket").getValue().toString();
                    String keyPesanan = child.getKey();
                    String foto = "user";
                    String tanggal = child.child("tanggal").getValue().toString();
                    String namaPaket = child.child("namaPaket").getValue().toString();
                    String namaPengguna = child.child("namaPengguna").getValue().toString();
                    String keterangan = child.child("keterangan").getValue().toString();

                    if (idUser.equals(SharedVariable.userID)){
                        Pesanan pesanan = new Pesanan(
                                idUser,
                                idPaket,
                                tanggal,
                                keterangan,
                                keyPesanan,
                                jenisPaket,
                                status,
                                namaPaket,
                                namaPengguna,
                                foto
                        );

                        listPesanan.add(pesanan);
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
