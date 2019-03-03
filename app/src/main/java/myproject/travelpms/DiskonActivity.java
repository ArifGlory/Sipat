package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DiskonActivity extends AppCompatActivity {

    EditText etDiskon;
    Button btnSimpan;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    Intent i;
    private String jenis,keyPaket,diskon;
    private SweetAlertDialog pDialogLoading,pDialodInfo,pDialogLoading2;
    Task initTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diskon);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        i = getIntent();
        jenis = i.getStringExtra("jenis");
        keyPaket = i.getStringExtra("key");

        etDiskon = findViewById(R.id.etDiskon);
        btnSimpan = findViewById(R.id.btnSimpan);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getDiskon = etDiskon.getText().toString();

                if (getDiskon.equals("") || getDiskon.length() == 0){
                    new SweetAlertDialog(DiskonActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Gambar wisata harus Diisi")
                            .show();
                }else {
                    initTask = ref.child("pakettour").child(jenis).child(keyPaket).child("diskon").setValue(getDiskon);

                    pDialogLoading.show();

                    initTask.addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            pDialogLoading.dismiss();
                            new SweetAlertDialog(DiskonActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Berhasil!")
                                    .setContentText("Diskon telah diubah")
                                    .show();
                            etDiskon.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pDialogLoading.dismiss();
                            new SweetAlertDialog(DiskonActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("Gagal merubah diskon"+e.getMessage())
                                    .show();

                        }
                    });
                }
            }
        });

       ref.child("pakettour").child(jenis).child(keyPaket).child("diskon").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               pDialogLoading.dismiss();

               if (dataSnapshot.exists()){
                   diskon = dataSnapshot.getValue().toString();
                   etDiskon.setText(diskon);
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });



    }
}
