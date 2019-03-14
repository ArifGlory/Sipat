package myproject.travelpms;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

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

import Adapter.AdapterLaporanPengguna;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LaporanPenggunaActivity extends AppCompatActivity {

    Spinner spBulan;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    private String bulan = "";
    List<Integer> arrayPengguna = new ArrayList<Integer>();
    List<String> arraylabel = new ArrayList<String>();

    RecyclerView recyclerView;
    AdapterLaporanPengguna adapter;
    List<String> jmlUserList;
    private int jan,feb,mar,apr,mei,jun,jul,agu,sep,okt,nov,des;
    TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_pengguna);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        jmlUserList = new ArrayList<String>();
        adapter = new AdapterLaporanPengguna(this,jmlUserList);
        txtTotal = findViewById(R.id.txtTotal);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                jmlUserList.clear();
                adapter.notifyDataSetChanged();
                jan = 0;feb = 0;mar = 0;
                apr = 0;mei = 0;jun = 0;
                jul = 0;agu = 0;sep = 0;
                okt = 0;nov = 0;des = 0;
                int total = 0;


                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String tglMasuk = child.child("last_login").getValue().toString();
                    int strip = tglMasuk.indexOf("-");

                    String sub = tglMasuk.substring(strip+1,strip+2);
                    Log.d("sub:",sub);
                    Log.d("tglmasuk:",tglMasuk);
                    Log.d("strip",""+strip);

                   switch (sub){
                       case "1":
                           jan++;
                           break;
                       case "2":
                           feb++;
                           break;
                       case "3":
                           mar++;
                           break;
                       case "4":
                           apr++;
                           break;
                       case "5":
                           mei++;
                           break;
                       case "6":
                           jun++;
                           break;
                       case "7":
                           jul++;
                           break;
                       case "8":
                           agu++;
                           break;
                       case "9":
                           sep++;
                           break;
                       case "10":
                           okt++;
                           break;
                       case "11":
                           nov++;
                           break;
                       case "12":
                           des++;
                           break;
                   }


                   total++;
                }

                jmlUserList.add(String.valueOf(jan));
                jmlUserList.add(String.valueOf(feb));
                jmlUserList.add(String.valueOf(mar));
                jmlUserList.add(String.valueOf(apr));
                jmlUserList.add(String.valueOf(mei));
                jmlUserList.add(String.valueOf(jun));
                jmlUserList.add(String.valueOf(jul));
                jmlUserList.add(String.valueOf(agu));
                jmlUserList.add(String.valueOf(sep));
                jmlUserList.add(String.valueOf(okt));
                jmlUserList.add(String.valueOf(nov));
                jmlUserList.add(String.valueOf(des));

                txtTotal.setText("Total Pengguna = "+String.valueOf(total));

                adapter.notifyDataSetChanged();
                pDialogLoading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
