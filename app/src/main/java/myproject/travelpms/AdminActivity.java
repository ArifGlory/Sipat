package myproject.travelpms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Fragment.FragmentBeranda;
import Fragment.FragmentPaketTour;
import Fragment.FragmentListPengguna;
import Fragment.FragmentListPesanan;
import Fragment.FragmentLaporan;
import Kelas.UserPreference;

public class AdminActivity extends AppCompatActivity {

    private TextView mTextMessage;
    DialogInterface.OnClickListener listener;
    FragmentPaketTour fragmentPaketTour;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    FirebaseUser fbUser;
    UserPreference mUserpref;
    FragmentListPengguna fragmentListPengguna;
    FragmentListPesanan fragmentListPesanan;
    FragmentLaporan fragmentLaporan;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_paket:
                    fragmentPaketTour = new FragmentPaketTour();
                    goToFragment(fragmentPaketTour,true);
                    return true;
                case R.id.navigation_pesanan:
                    fragmentListPesanan = new FragmentListPesanan();
                    goToFragment(fragmentListPesanan,true);
                    return true;
                case R.id.navigation_pengguna:
                    fragmentListPengguna = new FragmentListPengguna();
                    goToFragment(fragmentListPengguna,true);
                    return true;
                case R.id.navigation_laporan:
                    fragmentLaporan = new FragmentLaporan();
                    goToFragment(fragmentLaporan,true);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserpref = new UserPreference(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        fragmentPaketTour = new FragmentPaketTour();
        goToFragment(fragmentPaketTour,true);
    }

    void goToFragment(Fragment fragment, boolean isTop) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_admin_container, fragment);
        if (!isTop)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakan anda ingin logout dari akun admin ?");
        builder.setCancelable(false);

        listener = new DialogInterface.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    /*finishAffinity();
                    System.exit(0);*/
                    fAuth.signOut();
                    mUserpref.setBagian("none");
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }

                if(which == DialogInterface.BUTTON_NEGATIVE){
                    dialog.cancel();
                }
            }
        };
        builder.setPositiveButton("Ya",listener);
        builder.setNegativeButton("Tidak", listener);
        builder.show();
    }

}
