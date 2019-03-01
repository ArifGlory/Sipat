package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;

import Kelas.SharedVariable;
import Kelas.UserPreference;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends AppCompatActivity {


    Intent i;
    int delay =  3000;
    DatabaseReference ref,refDevice;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private static final long time = 3;
    private CountDownTimer mCountDownTimer;
    private long mTimeRemaining;
    private String now;
    String activeDeviceKirim;
    UserPreference mUserpref;
    String bagian;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    String versiApp = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(SplashActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        mUserpref = new UserPreference(this);
        bagian = mUserpref.getBagian();

        Calendar calendar = Calendar.getInstance();
        int bulan = calendar.get(Calendar.MONTH)+1;
        now = ""+calendar.get(Calendar.DATE)+"-"+bulan+"-"+calendar.get(Calendar.YEAR);
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        cekVersi();


        if (fbUser != null){
            String token = FirebaseInstanceId.getInstance().getToken();


            SharedVariable.userID = fAuth.getCurrentUser().getUid();

            ref.child("users").child(SharedVariable.userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String email = dataSnapshot.child("email").getValue().toString();
                    String level = dataSnapshot.child("level").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String nama = dataSnapshot.child("displayName").getValue().toString();
                    String foto = dataSnapshot.child("foto").getValue().toString();

                    SharedVariable.email = email;
                    SharedVariable.level = level;
                    SharedVariable.phone = phone;
                    SharedVariable.nama = nama;
                    SharedVariable.fotoUser = foto;

                    mUserpref.setBagian(level);
                    mUserpref.setEmail(email);
                    mUserpref.setIdUser(SharedVariable.userID);

                    if (level.equals("admin")){
                        i = new Intent(SplashActivity.this, AdminActivity.class);
                        startActivity(i);
                    }else {

                        if (level.equals("Instansi")){
                            SharedVariable.paket = "paket_instansi";
                        }else if (level.equals("Sekolah")){
                            SharedVariable.paket = "paket_sekolah";
                        }else if (level.equals("Umum")){
                            SharedVariable.paket = "paket_umum";
                        }

                        i = new Intent(SplashActivity.this, BerandaActivity.class);
                        startActivity(i);
                    }

                    pDialogLoading.dismiss();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }else {
            i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    private void cekVersi(){
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String versi = dataSnapshot.child("versionCheck").getValue().toString();

                if (!versi.equals(versiApp)){
                    finishAffinity();
                    System.exit(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
