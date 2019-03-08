package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import Kelas.SharedVariable;
import myproject.travelpms.LaporanPaketDilihatActivity;
import myproject.travelpms.LaporanPemesananActivity;
import myproject.travelpms.LaporanPenggunaActivity;
import myproject.travelpms.ListPaketInstansi;
import myproject.travelpms.ListPaketSekolah;
import myproject.travelpms.ListPaketUmum;
import myproject.travelpms.PilihLaporanPesanan;
import myproject.travelpms.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterAdminLaporan extends RecyclerView.Adapter<AdapterAdminLaporan.MyViewHolder> {

    private Context mContext;
    String laporan[] = {"Laporan Pemesanan","Laporan Pengguna"};
    int gambar[] = {R.drawable.jogja,R.drawable.bandung};
    int color[] = {R.color.ijoTua,R.color.colorPrimary2};
    String durasi[] = {"","",""};
    String sloganAtas[] = {"","",""};

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sloganAtas, namaDestinasi,durasi;
        public ImageView backdrop;
        public CardView cv_main;

        public MyViewHolder(View view) {
            super(view);
            sloganAtas = (TextView) view.findViewById(R.id.sloganAtas);
            namaDestinasi = (TextView) view.findViewById(R.id.namaDestinasi);
            durasi = (TextView) view.findViewById(R.id.durasi);
            backdrop = (ImageView) view.findViewById(R.id.backdrop);
            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
        }
    }


    public AdapterAdminLaporan(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_admin_paket, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.namaDestinasi.setText(laporan[position]);
        holder.sloganAtas.setText(sloganAtas[position]);
        holder.durasi.setText(durasi[position]);
        holder.backdrop.setBackgroundColor(color[position]);

        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == 0){
                    String laporan = "laporan_pemesanan";
                    Intent intent = new Intent(mContext.getApplicationContext(),PilihLaporanPesanan.class);
                    intent.putExtra("keyLaporan",laporan);
                      mContext.startActivity(intent);
                }else if (position == 1){
                    String laporan = "laporan_pengguna";
                    Intent intent = new Intent(mContext.getApplicationContext(),LaporanPaketDilihatActivity.class);
                  //  intent.putExtra("keyLaporan",laporan);
                    mContext.startActivity(intent);
                }

                //Toast.makeText(mContext.getApplicationContext(), "Di klik", Toast.LENGTH_SHORT).show();


            }
        });

    }


    /**
     * Showing popup menu when tapping on 3 dots
     */


    /**
     * Click listener for popup menu items
     */


    @Override
    public int getItemCount() {
        return laporan.length;
    }
}
