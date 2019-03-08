package myproject.travelpms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class PilihLaporanPesanan extends AppCompatActivity {

    CardView cardInstansi,cardUmum,cardSekolah;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_pesanan_terlaris);

        cardInstansi = findViewById(R.id.cardInstansi);
        cardSekolah = findViewById(R.id.cardSekolah);
        cardUmum = findViewById(R.id.cardUmum);

        cardInstansi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jenis = "paket_instansi";
                i = new Intent(getApplicationContext(),LaporanTerlarisActivity.class);
                i.putExtra("jenis",jenis);
                startActivity(i);
            }
        });
        cardSekolah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jenis = "paket_sekolah";
                i = new Intent(getApplicationContext(),LaporanTerlarisActivity.class);
                i.putExtra("jenis",jenis);
                startActivity(i);
            }
        });
        cardUmum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jenis = "paket_umum";
                i = new Intent(getApplicationContext(),LaporanTerlarisActivity.class);
                i.putExtra("jenis",jenis);
                startActivity(i);
            }
        });
    }
}
