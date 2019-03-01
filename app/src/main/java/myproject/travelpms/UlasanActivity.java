package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Adapter.AdapterItitenary;
import Adapter.AdapterUlasan;
import Kelas.Ititenary;
import Kelas.SharedVariable;
import Kelas.Ulasan;
import Kelas.UserPreference;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class UlasanActivity extends AppCompatActivity {

    EditText etKomentar;
    Button btnKirim;
    private RecyclerView recyclerView;
    AdapterUlasan adapter;
    private List<Ulasan> ulasanList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    String level,paketTour;
    UserPreference mUserpref;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    Intent i;
    private String keyPaket,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ulasan);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        i = getIntent();
        keyPaket = i.getStringExtra("key");

        ulasanList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new AdapterUlasan(UlasanActivity.this,ulasanList);
        btnKirim = findViewById(R.id.btnKirim);
        etKomentar = findViewById(R.id.etKomentar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        ref.child("pakettour").child(SharedVariable.paket).child(keyPaket).child("ulasanList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ulasanList.clear();
                adapter.notifyDataSetChanged();

                if (!dataSnapshot.exists()){

                    new SweetAlertDialog(UlasanActivity.this,SweetAlertDialog.NORMAL_TYPE)
                            .setContentText("Data Kosong")
                            .setConfirmText("Ok")
                            .show();
                }else {

                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String namaUser = child.child("namaUser").getValue().toString();
                        String foto = child.child("foto").getValue().toString();
                        String isiUlasan = child.child("isiUlasan").getValue().toString();
                        String tanggal = child.child("tanggal").getValue().toString();

                        Ulasan ulasan = new Ulasan(
                                foto,namaUser,isiUlasan,tanggal
                        );

                        ulasanList.add(ulasan);
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

    private void checkValidation(){
        String getUlasan = etKomentar.getText().toString();

        if (getUlasan.equals("") || getUlasan.length() == 0 || getUlasan.equals("Berikan ulasan..")
                ) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Semua Field Harus diisi")
                    .show();

            pDialogLoading.dismiss();
        }else {
            uploadData(getUlasan);
        }
    }

    private void uploadData(String isiUlasan){

        Calendar calendar = Calendar.getInstance();
        int bulan = calendar.get(Calendar.MONTH)+1;
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        long timeDifference = System.currentTimeMillis() - System.currentTimeMillis();
        if(timeDifference < oneDayInMillis){
            //formattedTime = DateFormat.format("hh:mm a", System.currentTimeMillis()).toString();
            //Toast.makeText(this,""+calendar.get(Calendar.DATE)+" - "+bulan+" - "+calendar.get(Calendar.YEAR)+" "+DateFormat.format("hh:mm a", System.currentTimeMillis()).toString(),Toast.LENGTH_LONG).show();
            time = ""+calendar.get(Calendar.DATE)+"-"+bulan+"-"+calendar.get(Calendar.YEAR)+" "+ DateFormat.format("hh:mm a", System.currentTimeMillis()).toString();

        }else{
            //formattedTime = DateFormat.format("dd MMM - hh:mm a", System.currentTimeMillis()).toString();
            //Toast.makeText(this,""+calendar.get(Calendar.DATE)+" - "+bulan+" - "+calendar.get(Calendar.YEAR)+" "+DateFormat.format("dd MMM - hh:mm a", System.currentTimeMillis()).toString(),Toast.LENGTH_LONG).show();
            time = ""+calendar.get(Calendar.DATE)+" - "+bulan+" - "+calendar.get(Calendar.YEAR)+" "+DateFormat.format("dd MMM - hh:mm a", System.currentTimeMillis()).toString();

        }

        String key = ref.child("pakettour").child(SharedVariable.paket).child(keyPaket).child("ulasanList").push().getKey();

        Ulasan ulasan = new Ulasan(
                SharedVariable.fotoUser,
                SharedVariable.nama,
                isiUlasan,
                time
        );

        ref.child("pakettour").child(SharedVariable.paket).child(keyPaket).child("ulasanList").child(key).setValue(ulasan);

        new SweetAlertDialog(UlasanActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sukses!")
                .setContentText("Berhasil Dikirim !")
                .show();

        etKomentar.setText("");
    }
}
