package myproject.travelpms;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Kelas.Pesanan;
import Kelas.SharedVariable;
import Kelas.Wisata;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class InvoiceUser extends AppCompatActivity {

    TextView txtNamaPaket,txtStatus,txtTanggal,txtHarga,txtJenis,txtDiskon,txtTotal,txtJmlPenumpang;
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
    private String destro = "0";
    private int diskonPaket,hargaPaket,total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_user);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        i = getIntent();
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
        txtDiskon = findViewById(R.id.txtDiskon);
        txtTotal = findViewById(R.id.txtTotal);
        txtJmlPenumpang = findViewById(R.id.txtJmlPenumpang);

        txtNamaPaket.setText(namaPaket);
        txtTanggal.setText(tanggal);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        pDialogLoading2 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading2.setTitleText("Uploading..");
        pDialogLoading2.setCancelable(false);

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
                hargaPaket = Integer.parseInt(harga);
                txtHarga.setText(""+formatRupiah.format((double) hargaPaket));

                pDialogLoading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("pakettour").child(jenis).child(idPaket).child("diskon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               pDialogLoading.show();

                if (dataSnapshot.exists()){
                    String diskon = dataSnapshot.getValue().toString();

                    NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    diskonPaket = Integer.parseInt(diskon);


                    Log.d("diskon:",diskon);
                    txtDiskon.setText(""+formatRupiah.format((double) diskonPaket));


                }else {
                    txtDiskon.setText("Rp. 0");

                }

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

                    if (destro.equals("0")){
                        String buktiBayar = dataSnapshot.child("buktiBayar").getValue().toString();
                        Glide.with(InvoiceUser.this)
                                .load(buktiBayar)
                                .into(imgBuktiBayar);
                        btnUpload.setText("Ubah Bukti Bayar");
                    }

                }

                String totalHarga = dataSnapshot.child("totalHarga").getValue().toString();
                String jmlPenumpang = dataSnapshot.child("jmlPenumpang").getValue().toString();

                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                int total = Integer.parseInt(totalHarga);
                txtTotal.setText(""+formatRupiah.format((double) total));

                txtJmlPenumpang.setText(jmlPenumpang + " Orang");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*i = new Intent(getApplicationContext(),TambahBuktiBayar.class);
               i.putExtra("keyPesanan",pesanan.getKey());
               startActivity(i);*/
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InvoiceUser.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, RC_IMAGE_GALLERY);

                }
            }
        });
        imgBuktiBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, RC_IMAGE_GALLERY);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_IMAGE_GALLERY && resultCode == RESULT_OK) {
            uriGambar = data.getData();
            final String tipe = GetMimeType(InvoiceUser.this,uriGambar);
            Toast.makeText(InvoiceUser.this, "Tipe : !\n" + tipe, Toast.LENGTH_LONG).show();

            final ImageView imageView = new ImageView(this);
            imageView.setImageURI(uriGambar);
            new SweetAlertDialog(InvoiceUser.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Anda yakin memilih gambar ini ?")
                   .setCustomView(imageView)
                    .setContentText("Anda yakin memilih gambar ini ?")
                    .setConfirmText("Batal")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelButton("Ya", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                           uploadData(uriGambar);

                        }
                    })
                    .show();
        }
        else if (requestCode == 100 && resultCode == RESULT_OK){
            uriGambar = file;
            imgBuktiBayar.setImageURI(uriGambar);
        }


    }

    private void uploadData(final Uri uri){

            pDialogLoading2.show();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imagesRef = storageRef.child("images");
            StorageReference userRef = imagesRef.child(SharedVariable.userID);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = fbUser.getUid() + "_" + timeStamp;
            StorageReference fileRef = userRef.child(filename);

            UploadTask uploadTask = fileRef.putFile(uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d("erorGambar : ",exception.getMessage());
                    Toast.makeText(InvoiceUser.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                    pDialogLoading2.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    pDialogLoading2.dismiss();
                    Toast.makeText(InvoiceUser.this, "Upload gambar finished!", Toast.LENGTH_SHORT).show();


                    // save image to database

                    ref.child("pesanan").child(keyPesanan).child("buktiBayar").setValue(downloadUrl.toString());

                    new SweetAlertDialog(InvoiceUser.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Sukses!")
                            .setContentText("Bukti bayar Berhasil Disimpan")
                            .show();
                    imgBuktiBayar.setImageURI(uriGambar);

                }
            });


    }

    public static String GetMimeType(Context context, Uri uriImage)
    {
        String strMimeType = null;

        Cursor cursor = context.getContentResolver().query(uriImage,
                new String[] { MediaStore.MediaColumns.MIME_TYPE },
                null, null, null);

        if (cursor != null && cursor.moveToNext())
        {
            strMimeType = cursor.getString(0);
        }

        return strMimeType;
    }

    @Override
    protected void onDestroy() {
        destro = "1";
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        destro = "0";
        super.onStart();
    }
}
