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

import Kelas.ListPesanan;
import Kelas.Pesanan;
import Kelas.UserModel;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import myproject.travelpms.DetailPesananAdmin;
import myproject.travelpms.InvoiceUser;
import myproject.travelpms.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterListPesanan extends RecyclerView.Adapter<AdapterListPesanan.MyViewHolder> {

    private Context mContext;
    private List<Pesanan> listPesanan;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaPengguna,txtPaket,txtJenis;
        public CircleImageView imgFoto;
        public CardView cv_main;
        public ImageView imgStatus;
        public RelativeLayout relaList;

        public MyViewHolder(View view) {
            super(view);
            namaPengguna = (TextView) view.findViewById(R.id.txtNamaPengguna);
            txtJenis = (TextView) view.findViewById(R.id.txtJenis);
            txtPaket = (TextView) view.findViewById(R.id.txtPaket);
            imgFoto = view.findViewById(R.id.imgFoto);
            imgStatus = view.findViewById(R.id.imgStatus);
            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
            relaList = view.findViewById(R.id.relaList);
        }
    }

    public AdapterListPesanan(Context mContext, List<Pesanan> listPesanan) {
        this.mContext = mContext;
        this.listPesanan = listPesanan;
        Firebase.setAndroidContext(mContext);
        FirebaseApp.initializeApp(mContext);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_list_pesanan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (listPesanan.isEmpty()){
            Toast.makeText(mContext.getApplicationContext(),"Data Kosong !",Toast.LENGTH_LONG).show();
            Log.d("isiListPesanan : ",""+listPesanan.size());
        }else {

            final Pesanan pesanan = listPesanan.get(position);

            holder.namaPengguna.setText(pesanan.getNamaPengguna());
            holder.txtPaket.setText(pesanan.getNamaPaket());

            if (pesanan.getJenisPaket().equals("paket_instansi")){
                holder.txtJenis.setText("Paket : Instansi");
            }else if (pesanan.getJenisPaket().equals("paket_sekolah")){
                holder.txtJenis.setText("Paket : Sekolah");
            }else if (pesanan.getJenisPaket().equals("paket_umum")){
                holder.txtJenis.setText("Paket : Umum");
            }

            if (pesanan.getFoto().equals("no")){
                holder.imgFoto.setImageResource(R.drawable.pemilik_kos);
            }else if (pesanan.getFoto().equals("user")){
                holder.imgFoto.setImageResource(R.drawable.tour2);
                holder.namaPengguna.setText(pesanan.getNamaPaket());
                holder.txtPaket.setText("");
            }
            else {
                Glide.with(mContext)
                        .load(pesanan.getFoto())
                        .into(holder.imgFoto);
            }

            if (pesanan.getStatus().equals("M")){
                holder.imgStatus.setImageResource(R.drawable.stopwatch);
            }else if (pesanan.getStatus().equals("T")){
                holder.imgStatus.setImageResource(R.drawable.check);
            }else if (pesanan.getStatus().equals("D")){
                holder.imgStatus.setImageResource(R.drawable.deny);
            }


            holder.cv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.relaList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if (pesanan.getFoto().equals("user")){
                        Intent i = new Intent(mContext,InvoiceUser.class);
                        i.putExtra("pesanan",pesanan);

                        mContext.startActivity(i);
                    }else {
                        Intent i = new Intent(mContext,DetailPesananAdmin.class);
                        i.putExtra("pesanan",pesanan);
                        mContext.startActivity(i);

                    }
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
        return listPesanan.size();
    }
}
