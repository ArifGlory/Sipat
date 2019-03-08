package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Adapter.AdapterWisata;
import Kelas.PaketTour;
import Kelas.SharedVariable;
import Kelas.Wisata;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailPaketTourAdmin extends AppCompatActivity {


    FloatingActionButton btnTambah;
    RecyclerView recyclerView;
    AdapterWisata adapterWisata;
    Intent i;
    TextView namaPaket,durasi,jmlPeserta,harga,txtRating,txtDiskon;
    ImageView backdrop;
    Button btnFasilitas,btnItinenary,btnUlasan;
    int hargaPaket,diskonPaket;
    private SweetAlertDialog pDialogInfo,pDialogLoading;
    private List<Wisata> wisataList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    public static String keyPaket,namaPaketTour;
    PaketTour paketTour;
    ArrayList<Integer> arrayRating = new ArrayList<>();
    AppBarLayout appbar;

    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_paket_tour_admin);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        i = getIntent();
        final PaketTour paketTour = (PaketTour) i.getSerializableExtra("paketTour");
        keyPaket = paketTour.getKey();
        namaPaketTour = paketTour.getNamaPaket();

        wisataList = new ArrayList<>();

        namaPaket = findViewById(R.id.txtNamaPaket);
        durasi = findViewById(R.id.txtDurasi);
        backdrop = findViewById(R.id.backdrop);
        btnFasilitas = findViewById(R.id.btnFasilitas);
        jmlPeserta = findViewById(R.id.txtJmlPeserta);
        harga = findViewById(R.id.txtHarga);
        txtRating = findViewById(R.id.txtRating);
        appbar = findViewById(R.id.appbar);
        btnTambah = findViewById(R.id.btnCreate);
        btnItinenary = findViewById(R.id.btnItinenary);
        btnUlasan = findViewById(R.id.btnUlasan);
        txtDiskon = findViewById(R.id.txtDiskon);

        fab_open = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        hargaPaket = Integer.parseInt(paketTour.getHargaPaket());

        namaPaket.setText(paketTour.getNamaPaket());
        durasi.setText("Durasi : "+paketTour.getDurasiPaket()+" hari");
        jmlPeserta.setText("Minimal Peserta : "+paketTour.getJumlahPeserta()+" Orang");
        harga.setText("Harga : "+formatRupiah.format((double) hargaPaket));
        txtRating.setText("-");

        Glide.with(this)
                .load(paketTour.getDownloadUrl())
                .into(backdrop);

        recyclerView = findViewById(R.id.recycler_view);

        adapterWisata = new AdapterWisata(this,wisataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterWisata);

        btnFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(DetailPaketTourAdmin.this)
                        .setTitleText("Fasilitas")
                        .setContentText(paketTour.getFasilitasPaket())
                        .setConfirmText("OK")
                        .show();
            }
        });
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),TambahWisata.class);
                i.putExtra("key",paketTour.getKey());
                i.putExtra("namaPaket",paketTour.getNamaPaket());
                startActivity(i);
            }
        });
        btnItinenary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ItitenaryActivity.class);
                i.putExtra("key",paketTour.getKey());
                i.putExtra("namaPaket",paketTour.getNamaPaket());
                startActivity(i);
            }
        });
        btnUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),UlasanActivity.class);
                i.putExtra("key",paketTour.getKey());
                i.putExtra("namaPaket",paketTour.getNamaPaket());
                startActivity(i);
            }
        });
        txtDiskon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),DiskonActivity.class);
                i.putExtra("jenis",SharedVariable.paket);
                i.putExtra("key",keyPaket);
                startActivity(i);
            }
        });


        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        ref.child("pakettour").child(SharedVariable.paket).child(paketTour.getKey()).child("wisataList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                wisataList.clear();
                adapterWisata.notifyDataSetChanged();

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String nama = child.child("namaWisata").getValue().toString();
                    String status = child.child("statusWisata").getValue().toString();
                    String downloadURL = child.child("downloadUrl").getValue().toString();
                    String key = child.child("key").getValue().toString();
                    String jenisPaket = child.child("jenisPaket").getValue().toString();
                    String keterangan = child.child("keterangan").getValue().toString();

                    Wisata wisata = new Wisata(
                            nama,
                            key,
                            downloadURL,
                            status,
                            jenisPaket,
                            keterangan
                    );
                    wisataList.add(wisata);
                    adapterWisata.notifyDataSetChanged();
                }

                pDialogLoading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("pakettour").child(SharedVariable.paket).child(paketTour.getKey()).child("ratingList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayRating.clear();
                int totalRating = 0;

                if (dataSnapshot.exists()){
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String rate = child.child("rate").getValue().toString();
                        int rating = Integer.parseInt(rate);
                        arrayRating.add(rating);
                        totalRating = totalRating + rating;
                    }
                    int showRating = totalRating/arrayRating.size();
                    txtRating.setText(""+showRating);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("pakettour").child(SharedVariable.paket).child(paketTour.getKey()).child("diskon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String diskon = dataSnapshot.getValue().toString();

                    NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    diskonPaket = Integer.parseInt(diskon);


                    Log.d("diskon:",diskon);
                    String strDiskon = String.valueOf(formatRupiah.format((double) diskonPaket));
                            txtDiskon.setText("Diskon "+strDiskon + " | Kelola Diskon");
                }else {
                    txtDiskon.setText("Tambahkan Diskon");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
