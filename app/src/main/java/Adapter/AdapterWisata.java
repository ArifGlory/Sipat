package Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import myproject.travelpms.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterWisata extends RecyclerView.Adapter<AdapterWisata.MyViewHolder> {

    private Context mContext;
    String namaWisata[] = {"Tangkuban Perahu","Sari Ater"};
    int gambar[] = {R.drawable.tangkuban,R.drawable.sari_ater};


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaWisata;
        public CircleImageView imgWisata;
        public CardView cv_main;

        public MyViewHolder(View view) {
            super(view);
            namaWisata = (TextView) view.findViewById(R.id.txtNamaWisata);
            imgWisata = view.findViewById(R.id.imgWisata);
            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
        }
    }


    public AdapterWisata(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_detailpaket, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.namaWisata.setText(namaWisata[position]);
        holder.imgWisata.setImageResource(gambar[position]);

        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext.getApplicationContext(), "Di klik", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return namaWisata.length;
    }
}
