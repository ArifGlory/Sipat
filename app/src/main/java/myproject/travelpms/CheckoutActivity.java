package myproject.travelpms;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Kelas.DaftarPaketKelas;
import Kelas.Pesanan;
import Kelas.SharedVariable;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CheckoutActivity extends AppCompatActivity {

    Button btnTanggal,btnSimpan;
    EditText etTanggal,etKeterangan;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    FirebaseUser fbUser;
    Intent i;
    private String keyPaket,foto;
    String keyPesanan = "";
    DaftarPaketKelas daftarPaketKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(CheckoutActivity.this);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            finish();
        }
        ref = FirebaseDatabase.getInstance().getReference();
        i = getIntent();
        keyPaket = i.getStringExtra("keyPaket");
        daftarPaketKelas = (DaftarPaketKelas) i.getSerializableExtra("daftarPaket");


        btnSimpan = findViewById(R.id.btnSimpan);
        btnTanggal = findViewById(R.id.btnTanggal);
        etKeterangan = findViewById(R.id.etKeterangan);
        etTanggal = findViewById(R.id.etTanggal);
        etTanggal.setEnabled(false);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        btnTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });


        ref.child("users").child(SharedVariable.userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foto = dataSnapshot.child("foto").getValue().toString();
                pDialogLoading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void matikanKomponen(){
        pDialogLoading.show();
        etKeterangan.setEnabled(false);
        btnTanggal.setEnabled(false);
    }

    private void hidupkanKomponen(){

        etKeterangan.setEnabled(true);
        btnTanggal.setEnabled(true);
    }

    private void checkValidation(){
        String getTanggal = etTanggal.getText().toString();

        matikanKomponen();

        if (getTanggal.equals("") || getTanggal.length() == 0
                ) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Semua Field Harus diisi")
                    .show();
            hidupkanKomponen();
            pDialogLoading.dismiss();
        }else {
           uploadData();
            hidupkanKomponen();
        }
    }

    private void uploadData(){
        String ket = etKeterangan.getText().toString();
        if (ket.equals("") || ket.length()==0){
            ket = "-";
        }
        keyPesanan = ref.child("pesanan").push().getKey();
        Pesanan pesanan = new Pesanan(
                SharedVariable.userID,
                SharedVariable.keyPaketUser,
                etTanggal.getText().toString(),
                ket,
                keyPesanan,
                SharedVariable.paket,
                "M",
                SharedVariable.namaPaketPesanan,
                SharedVariable.nama,
                foto
        );

        Task initTask;
       initTask =  ref.child("pesanan").child(keyPesanan).setValue(pesanan);

        initTask =  ref.child("pesanan").child(keyPesanan).setValue(pesanan);
        initTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                ref.child("pesanan").child(keyPesanan).child("daftarPaket").setValue(daftarPaketKelas);
                new SweetAlertDialog(CheckoutActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Pemesanan berhasil!")
                        .setContentText("Silakan cek perkembangan pesanan anda di menu 'Paket Tour Saya' ")
                        .show();

                etKeterangan.setText("");
                etTanggal.setText("");
                pDialogLoading.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new SweetAlertDialog(CheckoutActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Pemesanan gagal"+e.getMessage())
                        .show();
                pDialogLoading.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        i = new Intent(getApplicationContext(),BerandaActivity.class);
        startActivity(i);
        super.onBackPressed();
    }

    private void showDateDialog(){


        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                etTanggal.setText(""+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
