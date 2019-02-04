package myproject.travelpms;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

import Kelas.DaftarPaketKelas;
import Kelas.UserModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailDaftarPaket extends AppCompatActivity {

    Intent i;
    EditText fullName, emailId,
            ibuKandung, tujuanDestinasi,userPhone,asalIntansi,etTanggal;
    Button signUpButton,btnTanggal;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    ProgressBar progressBar;
    UserModel userDaftarModel;
    private String time,levelUser;
    private SweetAlertDialog pDialogLoading;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    String keyPesanan = "";
    FirebaseUser fbUser;
    String namaPaket = "";
    DaftarPaketKelas daftarPaketKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daftar_paket);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        i = getIntent();
        keyPesanan = i.getStringExtra("keyPesanan");

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        fullName = (EditText) findViewById(R.id.fullName);
        emailId = (EditText) findViewById(R.id.userEmailId);
        userPhone = (EditText) findViewById(R.id.userPhone);
        ibuKandung = findViewById(R.id.ibuKandung);
        asalIntansi = findViewById(R.id.asalInstansi);
        etTanggal = findViewById(R.id.etTanggal);
        tujuanDestinasi = findViewById(R.id.destinasi);

        fullName.setEnabled(false);
        emailId.setEnabled(false);
        userPhone.setEnabled(false);
        ibuKandung.setEnabled(false);
        asalIntansi.setEnabled(false);
        etTanggal.setEnabled(false);
        tujuanDestinasi.setEnabled(false);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        ref.child("pesanan").child(keyPesanan).child("daftarPaket").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nama = dataSnapshot.child("namaPeserta").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String asalInstansiuser = dataSnapshot.child("asalInstansi").getValue().toString();
                String ibuKandunguser = dataSnapshot.child("ibuKandung").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String ttl = dataSnapshot.child("ttl").getValue().toString();
                String tujuan = dataSnapshot.child("tujuanDestinasi").getValue().toString();

                fullName.setText(nama);
                emailId.setText(email);
                asalIntansi.setText(asalInstansiuser);
                ibuKandung.setText(ibuKandunguser);
                userPhone.setText(phone);
                etTanggal.setText(ttl);
                tujuanDestinasi.setText(tujuan);

                pDialogLoading.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
