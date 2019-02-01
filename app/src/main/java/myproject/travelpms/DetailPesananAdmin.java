package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

import Kelas.Pesanan;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailPesananAdmin extends AppCompatActivity {

    Button btnTerima,btnTolak;
    TextView txtNamaPaket,txtStatus,txtTanggal,txtHarga,txtJenis;
    ImageView imgFoto,imgBuktiBayar;
    Button btnUpload;
    Intent i;
    private String namaPaket,idPaket,status,tanggal,jenis,keyPesanan;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private SweetAlertDialog pDialogLoading,pDialodInfo,pDialogLoading2;
    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    static final int RC_IMAGE_GALLERY = 2;
    Uri uriGambar;
    Uri file;
    FirebaseUser fbUser;
    Pesanan pesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan_admin);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        i = getIntent();
        pesanan = (Pesanan) i.getSerializableExtra("pesanan");

        pesanan = (Pesanan) i.getSerializableExtra("pesanan");
        namaPaket = pesanan.getNamaPaket();
        idPaket = pesanan.getIdPaket();
        status = pesanan.getStatus();
        tanggal = pesanan.getTanggal();
        keyPesanan = pesanan.getKey();
        jenis = pesanan.getJenisPaket();

        txtHarga = findViewById(R.id.txtHarga);
        txtStatus = findViewById(R.id.txtStatus);
        txtTanggal = findViewById(R.id.txtTanggal);
        txtJenis = findViewById(R.id.txtJenisPaket);
        txtNamaPaket = findViewById(R.id.txtNamaPaket);
        imgBuktiBayar = findViewById(R.id.imgBuktiBayar);
        imgFoto = findViewById(R.id.imgFoto);
        btnUpload = findViewById(R.id.btnUpload);
        btnTerima = findViewById(R.id.btnTerima);
        btnTolak = findViewById(R.id.btnTolak);

        txtNamaPaket.setText(namaPaket);
        txtTanggal.setText(tanggal);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        if (status.equals("M")){
            txtStatus.setText("Status : Menunggu");
        }else if (status.equals("T")){
            txtStatus.setText("Status : Diterima");
        }else if (status.equals("D")){
            txtStatus.setText("Status : Ditolak");
        }

        if (jenis.equals("paket_instansi")){
            txtJenis.setText("Paket Instansi");
        }else if (jenis.equals("paket_sekolah")){
            txtJenis.setText("Paket Sekolah");
        }else if (jenis.equals("paket_umum")){
            txtJenis.setText("Paket Umum");
        }

        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("pesanan").child(pesanan.getKey()).child("status").setValue("T");
                new SweetAlertDialog(DetailPesananAdmin.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sukses!")
                        .setContentText("Status pesanan paket diubah menjadi Diterima")
                        .show();
            }
        });
        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("pesanan").child(pesanan.getKey()).child("status").setValue("D");
                new SweetAlertDialog(DetailPesananAdmin.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sukses!")
                        .setContentText("Status pesanan paket diubah menjadi Ditolak")
                        .show();
            }
        });

        ref.child("pakettour").child(jenis).child(idPaket).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String foto = dataSnapshot.child("downloadUrl").getValue().toString();
                String harga = dataSnapshot.child("hargaPaket").getValue().toString();

                Glide.with(getApplicationContext())
                        .load(foto)
                        .into(imgFoto);

                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                int hargaPaket = Integer.parseInt(harga);
                txtHarga.setText(""+formatRupiah.format((double) hargaPaket));

                pDialogLoading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("pesanan").child(keyPesanan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("buktiBayar").exists()){
                    String buktiBayar = dataSnapshot.child("buktiBayar").getValue().toString();
                    Glide.with(DetailPesananAdmin.this)
                            .load(buktiBayar)
                            .into(imgBuktiBayar);

                }
                String statusPesanan = dataSnapshot.child("status").getValue().toString();
                if (statusPesanan.equals("M")){
                    txtStatus.setText("Status : Menunggu");
                }else if (statusPesanan.equals("T")){
                    txtStatus.setText("Status : Diterima");
                }else if (statusPesanan.equals("D")){
                    txtStatus.setText("Status : Ditolak");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
