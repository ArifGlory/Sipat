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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myproject.travelpms.DetailPaketTour;
import myproject.travelpms.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterDestinasi extends RecyclerView.Adapter<AdapterDestinasi.MyViewHolder> {

    private Context mContext;
    String namaDestinasi[] = {"Jogja","Bandung"};
    int gambar[] = {R.drawable.jogja,R.drawable.bandung};
    String durasi[] = {"Durasi : 5 Hari","Durasi : 4 Hari"};
    String sloganAtas[] = {"Wisata kota keraton","Kota Kembang"};

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


    public AdapterDestinasi(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_travel_destination, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.namaDestinasi.setText(namaDestinasi[position]);
        holder.sloganAtas.setText(sloganAtas[position]);
        holder.durasi.setText(durasi[position]);
        holder.backdrop.setImageResource(gambar[position]);

        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == 1){

                }
                //Toast.makeText(mContext.getApplicationContext(), "Di klik", Toast.LENGTH_SHORT).show();

               // Intent intent = new Intent(mContext.getApplicationContext(),DetailPaketTour.class);
              //  mContext.startActivity(intent);
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
        return namaDestinasi.length;
    }
}
