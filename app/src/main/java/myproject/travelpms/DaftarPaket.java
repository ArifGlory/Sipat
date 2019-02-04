package myproject.travelpms;

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
import android.widget.ProgressBar;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Kelas.DaftarPaketKelas;
import Kelas.SharedVariable;
import Kelas.UserModel;
import Kelas.Utils;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DaftarPaket extends AppCompatActivity {

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
    String keyPaket = "";
    String namaPaket = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_paket);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(DaftarPaket.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        i = getIntent();
        keyPaket = i.getStringExtra("keyPaket");
        namaPaket = i.getStringExtra("namaPaket");

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        fullName = (EditText) findViewById(R.id.fullName);
        emailId = (EditText) findViewById(R.id.userEmailId);
        signUpButton = (Button) findViewById(R.id.signUpBtn);
        userPhone = (EditText) findViewById(R.id.userPhone);
        ibuKandung = findViewById(R.id.ibuKandung);
        asalIntansi = findViewById(R.id.asalInstansi);
        btnTanggal = findViewById(R.id.btnTanggal);
        etTanggal = findViewById(R.id.etTanggal);
        tujuanDestinasi = findViewById(R.id.destinasi);

        fullName.setText(SharedVariable.nama);
        emailId.setText(SharedVariable.email);
        userPhone.setText(SharedVariable.phone);
        tujuanDestinasi.setText(SharedVariable.namaPaketPesanan);

        fStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = fAuth.getCurrentUser();
                if (user != null ){
                    //user sedang login
                    Log.d("Fauth : ","onAuthStateChanged:signed_in:" + user.getUid());
                }
                //user sedang logout
                Log.d("Fauth : ","onAuthStateChanged:signed_out");
            }
        };

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading");
        pDialogLoading.setCancelable(false);

        btnTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });


    }

    private void checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getPhone = userPhone.getText().toString();
        String getIbu = ibuKandung.getText().toString();
        String getTujuan = tujuanDestinasi.getText().toString();
        String getTanggal = etTanggal.getText().toString();

        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getPhone.equals("") || getPhone.length() == 0
                || getIbu.equals("") || getIbu.length() == 0
                || getTujuan.equals("") || getTujuan.length() == 0
                || getTanggal.equals("") || getTanggal.length() == 0
                ) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Semua Field Harus diisi")
                    .show();
        }
        //check valid email
        else if (!m.find()) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Email anda tidak valid")
                    .show();

        }
        // Else do signup or do your stuff
        else
            daftarPaket();

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

    private void daftarPaket(){

        DaftarPaketKelas daftarPaketKelas = new DaftarPaketKelas(
                fullName.getText().toString(),
                emailId.getText().toString(),
                userPhone.getText().toString(),
                etTanggal.getText().toString(),
                tujuanDestinasi.getText().toString(),
                asalIntansi.getText().toString(),
                ibuKandung.getText().toString()
        );

        i = new Intent(getApplicationContext(),CheckoutActivity.class);
        i.putExtra("keyPaket",keyPaket);
        i.putExtra("daftarPaket",daftarPaketKelas);
        startActivity(i);

    }
}
