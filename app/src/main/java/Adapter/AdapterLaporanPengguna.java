package Adapter;

import android.content.Context;
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

import Kelas.UserModel;
import de.hdodenhof.circleimageview.CircleImageView;
import myproject.travelpms.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterLaporanPengguna extends RecyclerView.Adapter<AdapterLaporanPengguna.MyViewHolder> {

    private Context mContext;
    private List<String> jmlUserList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private String[] listBulan = {"Januari","Februari","Maret","April","Mei"
    ,"Juni","July","Agustus","September","Oktober","November","Desember"};


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaBulan,Jumlah;
        public CardView cv_main;
        public RelativeLayout relaList;

        public MyViewHolder(View view) {
            super(view);
            namaBulan = (TextView) view.findViewById(R.id.txtNamaBulan);
            Jumlah = (TextView) view.findViewById(R.id.txtJumlah);
            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
            relaList = view.findViewById(R.id.relaList);
        }
    }

    public AdapterLaporanPengguna(Context mContext, List<String> jmlUserList) {
        this.mContext = mContext;
        this.jmlUserList = jmlUserList;
        Firebase.setAndroidContext(mContext);
        FirebaseApp.initializeApp(mContext);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_list_laporan_pengguna, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (jmlUserList.isEmpty()){
            //Toast.makeText(mContext.getApplicationContext(),"Data Kosong !",Toast.LENGTH_LONG).show();
            Log.d("isiuserList : ",""+jmlUserList.size());
        }else {


            holder.namaBulan.setText(listBulan[position].toString());
            holder.Jumlah.setText(jmlUserList.get(position).toString());


            holder.cv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.relaList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
        //return namaWisata.length;
        return listBulan.length;
    }
}
