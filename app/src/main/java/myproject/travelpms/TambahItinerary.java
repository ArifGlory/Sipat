package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Kelas.Ititenary;
import Kelas.SharedVariable;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TambahItinerary extends AppCompatActivity {

    EditText etNamaTempat,etAlamat;
    Button btnSimpan,btnLokasi;
    String keyPaket,namaPaket;
    Intent i;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    FirebaseUser fbUser;
    private int PLACE_PICKER_REQUEST = 1;
    private String alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_itinerary);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(TambahItinerary.this);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            finish();
        }
        ref = FirebaseDatabase.getInstance().getReference();

        i = getIntent();
        keyPaket = i.getStringExtra("key");

        etNamaTempat = findViewById(R.id.etNamaTempat);
        etAlamat = findViewById(R.id.etAlamat);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnLokasi = findViewById(R.id.btnLokasi);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);


        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder  = new PlacePicker.IntentBuilder();
                try {
                    //menjalankan place picker
                    startActivityForResult(builder.build(TambahItinerary.this), PLACE_PICKER_REQUEST);

                    // check apabila <a title="Solusi Tidak Bisa Download Google Play Services di Android" href="http://www.twoh.co/2014/11/solusi-tidak-bisa-download-google-play-services-di-android/" target="_blank">Google Play Services tidak terinstall</a> di HP
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialogLoading.show();
                checkValidation();
            }
        });

    }

    private void checkValidation(){
        String getNama = etNamaTempat.getText().toString();
        String getAlamat = etAlamat.getText().toString();


        if (getNama.equals("") || getNama.length() == 0
                || getAlamat.equals("") || getAlamat.length() == 0 || getAlamat.equals("Silakan pilih lokasi")
                ) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Semua Field Harus diisi")
                    .show();

            pDialogLoading.dismiss();
        }else {
            uploadData(getNama,getAlamat);
        }
    }



    private void uploadData(String namaTempat,String alamatTempat){

        pDialogLoading.dismiss();

        String key = ref.child("pakettour").child(SharedVariable.paket).child(keyPaket).child("itineraryList").push().getKey();

        Ititenary ititenary = new Ititenary(
                namaTempat,
                alamatTempat
        );

        ref.child("pakettour").child(SharedVariable.paket).child(keyPaket).child("itineraryList").child(key).setValue(ititenary);

        new SweetAlertDialog(TambahItinerary.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sukses!")
                .setContentText("Data Berhasil Disimpan")
                .show();

        etNamaTempat.setText("");
        etAlamat.setText("Silakan pilih lokasi");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        // menangkap hasil balikan dari Place Picker, dan menampilkannya pada TextView
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format(
                        "Place: %s \n" +
                                "Alamat: %s \n" +
                                "Latlng %s \n", place.getName(), place.getAddress(), place.getLatLng().latitude+" "+place.getLatLng().longitude);
                //tvPlaceAPI.setText(toastMsg);

                etAlamat.setText(place.getAddress());
                alamat = (String) place.getAddress();

            }
        }


    }
}
