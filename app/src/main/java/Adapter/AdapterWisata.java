package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import Kelas.PaketTour;
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
public class AdapterWisata extends RecyclerView.Adapter<AdapterWisata.MyViewHolder> {

    private Context mContext;
    String namaWisata[] = {"Tangkuban Perahu","Sari Ater"};
    int gambar[] = {R.drawable.tangkuban,R.drawable.sari_ater};
    private List<Wisata> wisataList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaWisata,keterangan;
        public CircleImageView imgWisata;
        public CardView cv_main;
        public RelativeLayout relaList;

        public MyViewHolder(View view) {
            super(view);
            namaWisata = (TextView) view.findViewById(R.id.txtNamaWisata);
            keterangan = view.findViewById(R.id.txtKeterangan);
            imgWisata = view.findViewById(R.id.imgWisata);
            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
            relaList = view.findViewById(R.id.relaList);
        }
    }


    public AdapterWisata(Context mContext,List<Wisata> wisataList) {
        this.mContext = mContext;
        this.wisataList = wisataList;

        Firebase.setAndroidContext(mContext);
        FirebaseApp.initializeApp(mContext);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_detailpaket, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if (wisataList.isEmpty()){
            Toast.makeText(mContext.getApplicationContext(),"Data Kosong !",Toast.LENGTH_LONG).show();
            Log.d("isiWisataList : ",""+wisataList.size());
        }else {
            final Wisata wisata = wisataList.get(position);

            holder.namaWisata.setText(wisata.getNamaWisata());
            holder.keterangan.setText(wisata.getKeterangan());
            Glide.with(mContext)
                    .load(wisata.getDownloadUrl())
                    .into(holder.imgWisata);

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


                    if (SharedVariable.level.equals("admin")){
                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Hapus atau Ubah Wisata ini ?")
                                .setContentText("Anda dapat mengubah data atau menghapus data wisata ini")
                                .setConfirmText("Hapus")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        ref.child("pakettour").child(SharedVariable.paket).child(DetailPaketTourAdmin.keyPaket)
                                                .child("wisataList").child(wisata.getKey()).setValue(null);
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .setCancelButton("Ubah", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent intent = new Intent(mContext.getApplicationContext(),UbahWisata.class);
                                        intent.putExtra("wisata", wisata);
                                        intent.putExtra("keyPaket", DetailPaketTourAdmin.keyPaket);
                                        intent.putExtra("namaPaket", DetailPaketTourAdmin.namaPaketTour);
                                        mContext.startActivity(intent);

                                    }
                                })
                                .show();
                    }

                    return true;
                }
            });


        }


    }



    @Override
    public int getItemCount() {
       // return namaWisata.length;
        return  wisataList.size();
    }
}
