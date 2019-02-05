package myproject.travelpms;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.firebase.client.Firebase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Kelas.MyValueFormatter;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LaporanPemesananActivity extends AppCompatActivity {

    PieChart chart;
    Spinner spBulan;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    private String bulan = "";
    List<Integer> arrayInstansi = new ArrayList<Integer>();
    List<Integer> arraySekolah = new ArrayList<Integer>();
    List<Integer> arrayUmum = new ArrayList<Integer>();
    List<PieEntry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_pemesanan);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        chart = findViewById(R.id.chart);
        spBulan = findViewById(R.id.sp_bulan);

        Calendar c = Calendar.getInstance();
        String[]monthName={"January","February","March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        String month=monthName[c.get(Calendar.MONTH)];
        System.out.println("Month name:"+month);
        System.out.println("Month index :"+c.get(Calendar.MONTH));
        int indexBulan = c.get(Calendar.MONTH);

        switch (indexBulan){
            case 0:
                bulan = "01";
                break;
            case 1:
                bulan = "02";
                break;
            case 2:
                bulan = "03";
                break;
            case 3:
                bulan = "04";
                break;
            case 4:
                bulan = "05";
                break;
            case 5:
                bulan = "06";
                break;
            case 6:
                bulan = "07";
                break;
            case 7:
                bulan = "08";
                break;
            case 8:
                bulan = "09";
                break;
            case 9:
                bulan = "10";
                break;
            case 10:
                bulan = "11";
                break;
            case 11:
                bulan = "12";
                break;
        }


        entries = new ArrayList<PieEntry>();

        ref.child("pesanan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayInstansi.clear();
                arraySekolah.clear();
                arrayUmum.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String tanggal = child.child("tanggal").getValue().toString();
                    String paket = child.child("jenisPaket").getValue().toString();

                    tanggal = tanggal.substring(3,5);
                    Log.d("bulanSkrg:",tanggal);
                    Log.d("switchBulan:",bulan);

                    if (tanggal.equals(bulan)){

                        if (paket.equals("paket_instansi")){
                            arrayInstansi.add(1);
                        }else if (paket.equals("paket_umum")){
                            arrayUmum.add(1);
                        }else if (paket.equals("paket_sekolah")){
                            arraySekolah.add(1);
                        }

                    }

                }

                if (!arrayInstansi.isEmpty()){
                    entries.add(new PieEntry(arrayInstansi.size(),"Paket Instansi"));
                }else {
                    entries.add(new PieEntry(0,"Paket Instansi"));
                }

                if (!arrayUmum.isEmpty()){
                    entries.add(new PieEntry(arrayUmum.size(),"Paket Umum"));
                }else {
                    entries.add(new PieEntry(0,"Paket Umum"));
                }

                if (!arraySekolah.isEmpty()){
                    entries.add(new PieEntry(arraySekolah.size(),"Paket Sekolah"));
                }else {
                    entries.add(new PieEntry(0,"Paket Sekolah"));
                }


                PieDataSet dataSet = new PieDataSet(entries,"label");
                dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                dataSet.setValueTextColor(R.color.album_title);
                dataSet.setValueFormatter(new  MyValueFormatter());

                Description description = new Description();
                description.setText("Laporan Pemesanan Bulanan");

                PieData pieData = new PieData(dataSet);
                chart.setData(pieData);
                chart.setDescription(description);
                chart.invalidate();
                chart.animateXY(1400,1400);


                pDialogLoading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        spBulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int posisi  = position - 1;

                switch (posisi){
                    case 0:
                        bulan = "01";
                        getDataByBulan(bulan);
                        break;
                    case 1:
                        bulan = "02";
                        getDataByBulan(bulan);
                        break;
                    case 2:
                        bulan = "03";
                        getDataByBulan(bulan);
                        break;
                    case 3:
                        bulan = "04";
                        getDataByBulan(bulan);
                        break;
                    case 4:
                        bulan = "05";
                        getDataByBulan(bulan);
                        break;
                    case 5:
                        bulan = "06";
                        getDataByBulan(bulan);
                        break;
                    case 6:
                        bulan = "07";
                        getDataByBulan(bulan);
                        break;
                    case 7:
                        bulan = "08";
                        getDataByBulan(bulan);
                        break;
                    case 8:
                        bulan = "09";
                        getDataByBulan(bulan);
                        break;
                    case 9:
                        bulan = "10";
                        getDataByBulan(bulan);
                        break;
                    case 10:
                        bulan = "11";
                        getDataByBulan(bulan);
                        break;
                    case 11:
                        bulan = "12";
                        getDataByBulan(bulan);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getDataByBulan(final String getBulan){

        entries.clear();
        pDialogLoading.show();

        ref.child("pesanan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayInstansi.clear();
                arraySekolah.clear();
                arrayUmum.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String tanggal = child.child("tanggal").getValue().toString();
                    String paket = child.child("jenisPaket").getValue().toString();

                    tanggal = tanggal.substring(3,5);

                    if (tanggal.equals(getBulan)){

                        if (paket.equals("paket_instansi")){
                            arrayInstansi.add(1);
                        }else if (paket.equals("paket_umum")){
                            arrayUmum.add(1);
                        }else if (paket.equals("paket_sekolah")){
                            arraySekolah.add(1);
                        }

                    }

                }

                if (!arrayInstansi.isEmpty()){
                    entries.add(new PieEntry(arrayInstansi.size(),"Paket Instansi"));
                }else {
                    entries.add(new PieEntry(0,"Paket Instansi"));
                }

                if (!arrayUmum.isEmpty()){
                    entries.add(new PieEntry(arrayUmum.size(),"Paket Umum"));
                }else {
                    entries.add(new PieEntry(0,"Paket Umum"));
                }

                if (!arraySekolah.isEmpty()){
                    entries.add(new PieEntry(arraySekolah.size(),"Paket Sekolah"));
                }else {
                    entries.add(new PieEntry(0,"Paket Sekolah"));
                }


                PieDataSet dataSet = new PieDataSet(entries,"label");
                dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                dataSet.setValueTextColor(R.color.album_title);
                dataSet.setValueFormatter(new  MyValueFormatter());


                Description description = new Description();
                description.setText("Laporan Pemesanan Bulanan");

                PieData pieData = new PieData(dataSet);
                chart.setData(pieData);
                chart.setDescription(description);
                chart.invalidate();
                chart.animateXY(1400,1400);


                pDialogLoading.dismiss();

                if (arraySekolah.isEmpty() && arrayUmum.isEmpty() && arrayInstansi.isEmpty()){
                    new SweetAlertDialog(LaporanPemesananActivity.this,SweetAlertDialog.NORMAL_TYPE)
                            .setContentText("Data Kosong !")
                            .show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
