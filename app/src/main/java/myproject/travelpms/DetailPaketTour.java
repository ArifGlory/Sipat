package myproject.travelpms;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import Adapter.AdapterWisata;
import Kelas.DaftarPaketKelas;
import Kelas.PaketTour;
import Kelas.Rating;
import Kelas.SharedVariable;
import Kelas.Wisata;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailPaketTour extends AppCompatActivity implements RatingDialogListener {

    RecyclerView recyclerView;
    AdapterWisata adapterWisata;
    Intent i;
    TextView namaPaket,durasi,jmlPeserta,harga,txtKey,txtRating,txtDiskon;
    ImageView backdrop,imgRate;
    Button btnFasilitas,btnItinenary,btnUlasan;
    int hargaPaket,diskonPaket;
    private SweetAlertDialog pDialogInfo,pDialogLoading;
    private List<Wisata> wisataList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    Button btnKeCheckout;
    RelativeLayout relaRating;
    AppBarLayout appbar;
    PaketTour paketTour;
    ArrayList<Integer> arrayRating = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_paket_tour);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        i = getIntent();
        paketTour = (PaketTour) i.getSerializableExtra("paketTour");

        wisataList = new ArrayList<>();

        namaPaket = findViewById(R.id.txtNamaPaket);
        durasi = findViewById(R.id.txtDurasi);
        backdrop = findViewById(R.id.backdrop);
        btnFasilitas = findViewById(R.id.btnFasilitas);
        jmlPeserta = findViewById(R.id.txtJmlPeserta);
        harga = findViewById(R.id.txtHarga);
        btnKeCheckout = findViewById(R.id.btnTerima);
        txtKey = findViewById(R.id.txtKey);
        txtRating = findViewById(R.id.txtRating);
        relaRating = findViewById(R.id.relaRating);
        imgRate = findViewById(R.id.imgRate);
        appbar = findViewById(R.id.appbar);
        btnItinenary = findViewById(R.id.btnItinenary);
        btnUlasan = findViewById(R.id.btnUlasan);
        txtDiskon = findViewById(R.id.txtDiskon);

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        hargaPaket = Integer.parseInt(paketTour.getHargaPaket());
        SharedVariable.tempHarga = paketTour.getHargaPaket();


        namaPaket.setText(paketTour.getNamaPaket());
        durasi.setText("Durasi : "+paketTour.getDurasiPaket()+" hari");
        jmlPeserta.setText("Minimal Peserta : "+paketTour.getJumlahPeserta()+" Orang");
        SharedVariable.tempJmlPenumpang = paketTour.getJumlahPeserta();
        harga.setText("Harga : "+formatRupiah.format((double) hargaPaket));
        txtKey.setText(paketTour.getKey());
        txtKey.setVisibility(View.INVISIBLE);
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
                new SweetAlertDialog(DetailPaketTour.this)
                        .setTitleText("Fasilitas")
                        .setContentText(paketTour.getFasilitasPaket())
                        .setConfirmText("OK")
                        .show();
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

        appbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRating();
            }
        });

        btnKeCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyPaket = txtKey.getText().toString();
                SharedVariable.keyPaketUser = keyPaket;
                SharedVariable.namaPaketPesanan = paketTour.getNamaPaket();
                Intent intent = new Intent(getApplicationContext(), DaftarPaket.class);
                intent.putExtra("keyPaket",keyPaket);
                intent.putExtra("namaPaket",paketTour.getNamaPaket());
                startActivity(intent);
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
                    SharedVariable.tempDiskon = diskon;

                    Log.d("diskon:",diskon);
                    txtDiskon.setText("Diskon "+formatRupiah.format((double) diskonPaket));
                }else {
                    txtDiskon.setText("belum ada diskon");
                    SharedVariable.tempDiskon = "0";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showDialogRating(){
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(4)
                .setTitle("Berikan Penilaian mu")
                .setDescription("Silakan berikan penilaian mu tentang paket tour ini")
                .setCommentInputEnabled(true)
                .setDefaultComment("Keren banget !")
                .setStarColor(R.color.startblue)
                .setNoteDescriptionTextColor(R.color.kuningGelap)
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimaryDark)
                .setHint("Tulis komentar mu disini ...")
                .setHintTextColor(R.color.colorlight2)
                .setCommentTextColor(R.color.album_title)
                .setCommentBackgroundColor(R.color.photo_placeholder)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(DetailPaketTour.this)
               // .setTargetFragment(this, TAG) // only if listener is implemented by fragment
                .show();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
      //  Toast.makeText(getApplicationContext(),"submit",Toast.LENGTH_SHORT).show();
       String keyRating =  ref.child("pakettour").child(SharedVariable.paket).child(paketTour.getKey())
                .child("ratingList").push().getKey();

        String rate = String.valueOf(i);
        Rating rating = new Rating(
                rate,
                s
        );
        ref.child("pakettour").child(SharedVariable.paket).child(paketTour.getKey())
                .child("ratingList").child(keyRating).setValue(rating);

        new SweetAlertDialog(DetailPaketTour.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sukses!")
                .setContentText("Terima kasih atas penilaian anda")
                .show();

    }
}
