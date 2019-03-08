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

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Kelas.PaketTour;
import Kelas.SharedVariable;
import cn.pedant.SweetAlert.SweetAlertDialog;
import myproject.travelpms.DetailPaketTour;
import myproject.travelpms.DetailPaketTourAdmin;
import myproject.travelpms.R;
import myproject.travelpms.UbahWisata;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterDestinasi extends RecyclerView.Adapter<AdapterDestinasi.MyViewHolder> {

    private Context mContext;
    String namaDestinasi[] = {"Jogja","Bandung"};
    int gambar[] = {R.drawable.jogja,R.drawable.bandung};
    String durasi[] = {"Durasi : 5 Hari","Durasi : 4 Hari"};
    String sloganAtas[] = {"Wisata kota keraton","Kota Kembang"};
    private List<PaketTour> paketTourList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sloganAtas, namaDestinasi,durasi;
        public ImageView backdrop;
        public CardView cv_main;
        public RelativeLayout relaPaket;

        public MyViewHolder(View view) {
            super(view);
            sloganAtas = (TextView) view.findViewById(R.id.sloganAtas);
            namaDestinasi = (TextView) view.findViewById(R.id.namaDestinasi);
            durasi = (TextView) view.findViewById(R.id.durasi);
            backdrop = (ImageView) view.findViewById(R.id.backdrop);
            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
            relaPaket = view.findViewById(R.id.relaPaket);
        }
    }


    public AdapterDestinasi(Context mContext,List<PaketTour> paketTourList) {
        this.mContext = mContext;
        this.paketTourList = paketTourList;
        Firebase.setAndroidContext(mContext);
        FirebaseApp.initializeApp(mContext);
        ref = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_travel_destination, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (paketTourList.isEmpty()){
            Toast.makeText(mContext.getApplicationContext(),"Data Kosong !",Toast.LENGTH_LONG).show();
            Log.d("isiPaketList : ",""+paketTourList.size());
        }else {
            final PaketTour paketTour = paketTourList.get(position);

            holder.namaDestinasi.setText(paketTour.getNamaPaket());
            holder.sloganAtas.setText("");
            holder.durasi.setText("Durasi : "+paketTour.getDurasiPaket()+" hari");

            Glide.with(mContext)
                    .load(paketTour.getDownloadUrl())
                    .into(holder.backdrop);


            holder.cv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //tambahkan ke paket dilihat
                    //Creating parameters
                    Map<String,String> params = new Hashtable<String, String>();

                    //Adding parameters
                    params.put("keyPaket", paketTour.getKey());
                    params.put("namaPaket", paketTour.getNamaPaket());

                    ref.child("users").child(SharedVariable.userID).child("paketDilihat").child(paketTour.getKey()).setValue(params);

                    Intent intent = new Intent(mContext.getApplicationContext(),DetailPaketTour.class);
                    intent.putExtra("paketTour", paketTour);
                    mContext.startActivity(intent);
                }
            });

            holder.relaPaket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,String> params = new Hashtable<String, String>();

                    //Adding parameters
                    params.put("keyPaket", paketTour.getKey());
                    params.put("namaPaket", paketTour.getNamaPaket());

                    ref.child("users").child(SharedVariable.userID).child("paketDilihat").child(paketTour.getKey()).setValue(params);


                    Intent intent = new Intent(mContext.getApplicationContext(),DetailPaketTour.class);
                    intent.putExtra("paketTour", paketTour);
                    mContext.startActivity(intent);
                }
            });
        }


    }


    /**
     * Showing popup menu when tapping on 3 dots
     */


    /**
     * Click listener for popup menu items
     */


    @Override
    public int getItemCount() {
       // return namaDestinasi.length;
       return paketTourList.size();
    }
}
