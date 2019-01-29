package myproject.travelpms;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import Kelas.PaketTour;
import Kelas.SharedVariable;
import Kelas.Wisata;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class UbahWisata extends AppCompatActivity {

    TextView txtNamaPaket;
    EditText etNamaWisata;
    Button btnSimpan;
    private String keyPaket,namaPaket,keyWisata,uriFirebase;
    Intent i;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    ImageView imgBrowse;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    FirebaseUser fbUser;
    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    static final int RC_IMAGE_GALLERY = 2;
    Uri uriGambar,file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_wisata);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(UbahWisata.this);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            finish();
        }
        ref = FirebaseDatabase.getInstance().getReference();

        i = getIntent();

        final Wisata wisata = (Wisata) i.getSerializableExtra("wisata");
        keyPaket = i.getStringExtra("keyPaket");
        namaPaket = i.getStringExtra("namaPaket");
        keyWisata = wisata.getKey();
        uriFirebase = wisata.getDownloadUrl();

        txtNamaPaket = findViewById(R.id.txtNamaPaket);
        etNamaWisata = findViewById(R.id.etNamaWisata);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgBrowse = (ImageView) findViewById(R.id.img_browse);

        txtNamaPaket.setText(namaPaket);
        Glide.with(this)
                .load(wisata.getDownloadUrl())
                .into(imgBrowse);
        etNamaWisata.setText(wisata.getNamaWisata());

        imgBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UbahWisata.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, RC_IMAGE_GALLERY);

                }
            }
        });

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

    }

    private void matikanKomponen(){
        pDialogLoading.show();
        imgBrowse.setEnabled(false);
        etNamaWisata.setEnabled(false);
    }

    private void hidupkanKomponen(){

        imgBrowse.setEnabled(true);
        etNamaWisata.setEnabled(true);
    }

    private void checkValidation(){
        String getNama = etNamaWisata.getText().toString();

        matikanKomponen();

        if (getNama.equals("") || getNama.length() == 0
                ) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Semua Field Harus diisi")
                    .show();
            hidupkanKomponen();
            pDialogLoading.dismiss();
        }else if (uriGambar == null){
            uploadWithoutGambar();
        }else {
            uploadData(uriGambar);
            hidupkanKomponen();
        }
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
            final String tipe = GetMimeType(UbahWisata.this,uriGambar);
            Toast.makeText(UbahWisata.this, "Tipe : !\n" + tipe, Toast.LENGTH_LONG).show();

            imgBrowse.setImageURI(uriGambar);
        }
        else if (requestCode == 100 && resultCode == RESULT_OK){
            uriGambar = file;
            imgBrowse.setImageURI(uriGambar);
        }

    }

    private void uploadWithoutGambar(){
        ref.child("pakettour").child(SharedVariable.paket).child(keyPaket).child("wisataList").child(keyWisata)
                .child("namaWisata").setValue(etNamaWisata.getText().toString());

        pDialogLoading.dismiss();
        new SweetAlertDialog(UbahWisata.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sukses!")
                .setContentText("Data Berhasil Diubah")
                .show();

    }

    private void uploadData(final Uri uri){
        pDialogLoading.show();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("images");
        StorageReference userRef = imagesRef.child(fbUser.getUid());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = fbUser.getUid() + "_" + timeStamp;
        StorageReference fileRef = userRef.child(filename);

        UploadTask uploadTask = fileRef.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(UbahWisata.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                pDialogLoading.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                pDialogLoading.dismiss();
                Toast.makeText(UbahWisata.this, "Upload gambar finished!", Toast.LENGTH_SHORT).show();

                // save image to database


                //String key = ref.child("pakettour").child(SharedVariable.paket).child(keyPaket).child("wisataList").push().getKey();


                Wisata wisata = new Wisata(
                        etNamaWisata.getText().toString(),
                        keyWisata,
                        downloadUrl.toString(),
                        "on",
                        SharedVariable.paket
                );

                ref.child("pakettour").child(SharedVariable.paket).child(keyPaket).child("wisataList").child(keyWisata).setValue(wisata);

                new SweetAlertDialog(UbahWisata.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sukses!")
                        .setContentText("Data Berhasil Diubah")
                        .show();

                //etNamaWisata.setText("");
                //imgBrowse.setImageResource(R.drawable.ic_browse);

            }
        });


    }
}
