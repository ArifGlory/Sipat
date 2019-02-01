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
import android.util.Log;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import Kelas.SharedVariable;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfilPerusahaan extends AppCompatActivity {

    TextView txtNama,txtPhone,txtEmail,txtAlamat,txtSlogan,txtSlogan2;
    ImageView imgFoto,imgAlamat;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private SweetAlertDialog pDialogInfo,pDialogLoading,pDialogLoading2;
    Intent i;
    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    static final int RC_IMAGE_GALLERY = 2;
    Uri uriGambar;
    Uri file;
    FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_perusahaan);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        i = getIntent();
        String isUser = i.getStringExtra("isUser");


        imgAlamat = findViewById(R.id.imgAlamat);
        imgFoto = findViewById(R.id.imgFoto);
        txtNama = findViewById(R.id.txtNamaPengguna);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtSlogan = findViewById(R.id.txtSLogan);
        txtSlogan2 = findViewById(R.id.txtSLogan2);

        if (isUser.equals("1")){
            txtAlamat.setVisibility(View.INVISIBLE);
            imgAlamat.setVisibility(View.INVISIBLE);
            txtSlogan2.setText("SIPAT");
            txtSlogan.setText("Your Travel Solutions");

            pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialogLoading.setTitleText("Menampilkan data..");
            pDialogLoading.setCancelable(false);
            pDialogLoading.show();

            pDialogLoading2 = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialogLoading2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialogLoading2.setTitleText("Uploading..");
            pDialogLoading2.setCancelable(false);

            ref.child("users").child(SharedVariable.userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String foto = dataSnapshot.child("foto").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String nama = dataSnapshot.child("displayName").getValue().toString();

                    if (!foto.equals("no")){
                        Glide.with(ProfilPerusahaan.this)
                                .load(foto)
                                .into(imgFoto);
                    }else {
                        imgFoto.setImageResource(R.drawable.pemilik_kos);
                    }

                    txtEmail.setText(email);
                    txtPhone.setText(phone);
                    txtNama.setText(nama);

                    pDialogLoading.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            imgFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfilPerusahaan.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, RC_IMAGE_GALLERY);

                    }
                }
            });
        }

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
                Toast.makeText(ProfilPerusahaan.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                pDialogLoading2.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                pDialogLoading2.dismiss();
                Toast.makeText(ProfilPerusahaan.this, "Upload gambar finished!", Toast.LENGTH_SHORT).show();

                // save image to database
                ref.child("users").child(SharedVariable.userID).child("foto").setValue(downloadUrl.toString());

                new SweetAlertDialog(ProfilPerusahaan.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sukses!")
                        .setContentText("Foto Berhasil Disimpan")
                        .show();


            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_IMAGE_GALLERY && resultCode == RESULT_OK) {
            uriGambar = data.getData();
            final String tipe = GetMimeType(ProfilPerusahaan.this,uriGambar);
            Toast.makeText(ProfilPerusahaan.this, "Tipe : !\n" + tipe, Toast.LENGTH_LONG).show();

            final ImageView imageView = new ImageView(this);
            imageView.setImageURI(uriGambar);
            new SweetAlertDialog(ProfilPerusahaan.this, SweetAlertDialog.WARNING_TYPE)
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
}
