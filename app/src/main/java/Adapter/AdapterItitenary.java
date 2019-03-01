package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import Kelas.Ititenary;
import Kelas.SharedVariable;
import Kelas.Wisata;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import myproject.travelpms.DetailPaketTourAdmin;
import myproject.travelpms.R;
import myproject.travelpms.UbahWisata;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterItitenary extends RecyclerView.Adapter<AdapterItitenary.MyViewHolder> {

    private Context mContext;
    String namaWisata[] = {"Tangkuban Perahu","Sari Ater"};
    int warna[] = {R.drawable.btn_circle2,R.drawable.btn_circle3,R.drawable.btn_circle4,R.drawable.btn_circle5,
            R.drawable.btn_circle6,R.drawable.btn_circle7,};
    private List<Ititenary> ititenaryList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaTempat,alamat;
        public Button btnWarna;
        public CardView cv_main;
        public RelativeLayout relaList;

        public MyViewHolder(View view) {
            super(view);
            namaTempat = (TextView) view.findViewById(R.id.txtNamaTempat);
            alamat = view.findViewById(R.id.txtAlamat);
            btnWarna = view.findViewById(R.id.btnWarna);
            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
            relaList = view.findViewById(R.id.relaList);
        }
    }


    public AdapterItitenary(Context mContext, List<Ititenary> ititenaryList) {
        this.mContext = mContext;
        this.ititenaryList = ititenaryList;

        Firebase.setAndroidContext(mContext);
        FirebaseApp.initializeApp(mContext);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_ititenary, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if (ititenaryList.isEmpty()){
            Toast.makeText(mContext.getApplicationContext(),"Data Kosong !",Toast.LENGTH_LONG).show();
            Log.d("isiItitenaryList : ",""+ititenaryList.size());
        }else {

            Ititenary ititenary = ititenaryList.get(position);

            holder.namaTempat.setText(ititenary.getNamaTempat());
            holder.alamat.setText(ititenary.getAlamat());


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
       // return namaWisata.length;
        return  ititenaryList.size();
    }
}
