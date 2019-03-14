package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Kelas.LaporanPaketDilihat;
import Kelas.Ulasan;
import de.hdodenhof.circleimageview.CircleImageView;
import myproject.travelpms.R;
import myproject.travelpms.SummaryUserActivity;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterLaporanPaketDilihat extends RecyclerView.Adapter<AdapterLaporanPaketDilihat.MyViewHolder> {

    private Context mContext;
    String namaWisata[] = {"Tangkuban Perahu","Sari Ater"};
    private List<LaporanPaketDilihat> paketDilihatList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNamaPengguna,txtPaketDilihat,txtPaketDipesan;
        CircleImageView imgFoto;
        public CardView cv_main;
        public RelativeLayout relaList;

        public MyViewHolder(View view) {
            super(view);
            txtNamaPengguna = (TextView) view.findViewById(R.id.txtNamaPengguna);
            imgFoto = view.findViewById(R.id.imgFoto);
            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
            relaList = view.findViewById(R.id.relaList);
        }
    }


    public AdapterLaporanPaketDilihat(Context mContext, List<LaporanPaketDilihat> paketDilihatList) {
        this.mContext = mContext;
        this.paketDilihatList = paketDilihatList;

        Firebase.setAndroidContext(mContext);
        FirebaseApp.initializeApp(mContext);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_paket_dilihat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if (paketDilihatList.isEmpty()){
            Toast.makeText(mContext.getApplicationContext(),"Data Kosong !",Toast.LENGTH_LONG).show();
            Log.d("paketDilihatList : ",""+paketDilihatList.size());
        }else {

            final LaporanPaketDilihat laporanPaketDilihat = paketDilihatList.get(position);

            holder.txtNamaPengguna.setText(laporanPaketDilihat.getNama());


            if (laporanPaketDilihat.getFoto().equals("no")){
                holder.imgFoto.setImageResource(R.drawable.pemilik_kos);
            }else {
                Glide.with(mContext)
                        .load(laporanPaketDilihat.getFoto())
                        .into(holder.imgFoto);
            }


            holder.cv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.relaList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext,SummaryUserActivity.class);
                    i.putExtra("keyUser",laporanPaketDilihat.getKey());
                    mContext.startActivity(i);
                }
            });
            holder.relaList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {



                    return true;
                }
            });


        }


    }



    @Override
    public int getItemCount() {
       // return namaWisata.length;
        return  paketDilihatList.size();
    }
}
