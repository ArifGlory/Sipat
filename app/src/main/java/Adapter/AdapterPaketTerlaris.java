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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import Kelas.PaketTour;
import Kelas.SharedVariable;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import myproject.travelpms.DetailPaketTourAdmin;
import myproject.travelpms.R;
import myproject.travelpms.UbahPaketTour;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterPaketTerlaris extends RecyclerView.Adapter<AdapterPaketTerlaris.MyViewHolder> {

    private Context mContext;
    private List<PaketTour> paketTourList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaWisata,harga,durasi;
        public CircleImageView imgWisata;
        public CardView cv_main;
        public RelativeLayout relaList;

        public MyViewHolder(View view) {
            super(view);
            namaWisata = (TextView) view.findViewById(R.id.txtNamaWisata);
            harga = (TextView) view.findViewById(R.id.txtHarga);
            durasi = (TextView) view.findViewById(R.id.txtDurasi);
            imgWisata = view.findViewById(R.id.imgWisata);
            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
            relaList = view.findViewById(R.id.relaList);
        }
    }

    public AdapterPaketTerlaris(Context mContext, List<PaketTour> paketTourList) {
        this.mContext = mContext;
        this.paketTourList = paketTourList;
        Firebase.setAndroidContext(mContext);
        FirebaseApp.initializeApp(mContext);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_list_paket_tour, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (paketTourList.isEmpty()){
            Toast.makeText(mContext.getApplicationContext(),"Data Kosong !",Toast.LENGTH_LONG).show();
            Log.d("isiPaketList : ",""+paketTourList.size());
        }else {

            final PaketTour paketTour = paketTourList.get(position);
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            int hargaPaket = Integer.parseInt(paketTour.getHargaPaket());

            holder.namaWisata.setText(paketTour.getNamaPaket());
            holder.harga.setText("Jumlah Dipesan : "+hargaPaket);
            holder.durasi.setText("");
            Glide.with(mContext)
                    .load(paketTour.getDownloadUrl())
                    .into(holder.imgWisata);

            holder.cv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.relaList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getApplicationContext(),DetailPaketTourAdmin.class);
                    intent.putExtra("paketTour", paketTour);
                    mContext.startActivity(intent);
                }
            });


            holder.relaList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (SharedVariable.level.equals("admin")){
                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Hapus atau Ubah Paket Tour ini ?")
                                .setContentText("Anda dapat mengubah data atau menghapus data paket tour ini")
                                .setConfirmText("Hapus")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        ref.child("pakettour").child(SharedVariable.paket).child(paketTour.getKey())
                                                .setValue(null);
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .setCancelButton("Ubah", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(mContext.getApplicationContext(),UbahPaketTour.class);
                                        intent.putExtra("paketTour", paketTour);
                                        mContext.startActivity(intent);
                                        sDialog.dismissWithAnimation();
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
        //return namaWisata.length;
        return paketTourList.size();
    }
}
