package myproject.travelpms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import Kelas.PaketTour;
import Kelas.SharedVariable;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TambahPaketTour extends AppCompatActivity {

    EditText etNama,etHarga,etJmlPeserta,etFasilitas,etDurasi;
    ImageView imgBrowse;
    Button btnUpload;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    static final int RC_IMAGE_GALLERY = 2;
    FirebaseUser fbUser;
    DialogInterface.OnClickListener listener;
    Uri uri,file;
    private SweetAlertDialog pDialogLoading,pDialodInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_paket_tour);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(TambahPaketTour.this);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            finish();
        }
        ref = FirebaseDatabase.getInstance().getReference();

        imgBrowse = (ImageView) findViewById(R.id.img_browse);
        etNama = findViewById(R.id.etNamaPaket);
        etHarga = findViewById(R.id.etHargaPaket);
        etDurasi = findViewById(R.id.etDurasi);
        etJmlPeserta = findViewById(R.id.etJumlahPeserta);
        etFasilitas = findViewById(R.id.etFasilitas);
        btnUpload = findViewById(R.id.btnSimpan);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);

        imgBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TambahPaketTour.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, RC_IMAGE_GALLERY);

                }
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });


    }

    private void matikanKomponen(){
        pDialogLoading.show();
       etFasilitas.setEnabled(false);
        etHarga.setEnabled(false);
        etNama.setEnabled(false);
        imgBrowse.setEnabled(false);
        etJmlPeserta.setEnabled(false);
        etDurasi.setEnabled(false);
    }

    private void hidupkanKomponen(){

        etFasilitas.setEnabled(true);
        etHarga.setEnabled(true);
        etNama.setEnabled(true);
        imgBrowse.setEnabled(true);
        etJmlPeserta.setEnabled(true);
        etDurasi.setEnabled(true);
    }

    private void checkValidation(){
        String getNama = etNama.getText().toString();
        String getHarga = etHarga.getText().toString();
        String getJumlah = etJmlPeserta.getText().toString();
        String getDurasi = etDurasi.getText().toString();
        String getFasilitas = etFasilitas.getText().toString();
        matikanKomponen();

        if (getNama.equals("") || getNama.length() == 0
                || getHarga.equals("") || getHarga.length() == 0
                || getJumlah.equals("") || getJumlah.length() == 0
                || getDurasi.equals("") || getDurasi.length() == 0
                || getFasilitas.equals("") || getFasilitas.length() == 0
                ) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Semua Field Harus diisi")
                    .show();
            hidupkanKomponen();
            pDialogLoading.dismiss();
        }else if (uri == null){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Gambar Paket harus Diisi")
                    .show();
            hidupkanKomponen();
            pDialogLoading.dismiss();
        }else {
            uploadData(uri);
            hidupkanKomponen();
        }
    }


    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraSIPAT");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
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
            uri = data.getData();
            final String tipe = GetMimeType(TambahPaketTour.this,uri);
            Toast.makeText(TambahPaketTour.this, "Tipe : !\n" + tipe, Toast.LENGTH_LONG).show();

            imgBrowse.setImageURI(uri);
        }
        else if (requestCode == 100 && resultCode == RESULT_OK){
            uri = file;
            imgBrowse.setImageURI(uri);
        }


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
                Toast.makeText(TambahPaketTour.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
               pDialogLoading.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                pDialogLoading.dismiss();
                Toast.makeText(TambahPaketTour.this, "Upload gambar finished!", Toast.LENGTH_SHORT).show();

                // save image to database


                String key = ref.child("pakettour").child(SharedVariable.paket).push().getKey();
                PaketTour paketTour = new PaketTour(
                    etNama.getText().toString(),
                        etHarga.getText().toString(),
                        etDurasi.getText().toString(),
                        etJmlPeserta.getText().toString(),
                        etFasilitas.getText().toString(),
                        key,
                        downloadUrl.toString(),
                        "on"
                );

                ref.child("pakettour").child(SharedVariable.paket).child(key).setValue(paketTour);

                new SweetAlertDialog(TambahPaketTour.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sukses!")
                        .setContentText("Data Berhasil Disimpan")
                        .show();

                etHarga.setText("");
                etNama.setText("");
                etJmlPeserta.setText("");
                etFasilitas.setText("");
                etDurasi.setText("");
                imgBrowse.setImageResource(R.drawable.ic_browse);


            }
        });


    }
}
