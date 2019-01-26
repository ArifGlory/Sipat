package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private SweetAlertDialog pDialogLoading;

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


        if (fbUser != null){
            String token = FirebaseInstanceId.getInstance().getToken();
            SharedVariable.nama = fAuth.getCurrentUser().getDisplayName();
            SharedVariable.userID = fAuth.getCurrentUser().getUid();

            mCountDownTimer = new CountDownTimer(time * 1000, 50) {
                @Override
                public void onTick(long millisUnitFinished) {
                    mTimeRemaining = ((millisUnitFinished / 1000) + 1);

                }

                @Override
                public void onFinish() {

                    if (bagian.equals("none")){
                        i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                    }else {
                        i = new Intent(SplashActivity.this, BerandaActivity.class);
                        startActivity(i);
                    }

                }
            };
            mCountDownTimer.start();

        }else {
            i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }
    }
}
