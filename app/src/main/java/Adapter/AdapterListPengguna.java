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
import Kelas.UserModel;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import myproject.travelpms.DetailPaketTourAdmin;
import myproject.travelpms.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterListPengguna extends RecyclerView.Adapter<AdapterListPengguna.MyViewHolder> {

    private Context mContext;
    private List<UserModel> userModelList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaPengguna,nope,email;
        public CircleImageView imgWisata;
        public CardView cv_main;
        public RelativeLayout relaList;

        public MyViewHolder(View view) {
            super(view);
            namaPengguna = (TextView) view.findViewById(R.id.txtNamaPengguna);
            email = (TextView) view.findViewById(R.id.txtEmail);
            nope = (TextView) view.findViewById(R.id.txtNope);

            cv_main = (CardView) view.findViewById(R.id.cardlist_item);
            relaList = view.findViewById(R.id.relaList);
        }
    }

    public AdapterListPengguna(Context mContext, List<UserModel> userModelList) {
        this.mContext = mContext;
        this.userModelList = userModelList;
        Firebase.setAndroidContext(mContext);
        FirebaseApp.initializeApp(mContext);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_list_pengguna, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (userModelList.isEmpty()){
            Toast.makeText(mContext.getApplicationContext(),"Data Kosong !",Toast.LENGTH_LONG).show();
            Log.d("isiuserList : ",""+userModelList.size());
        }else {

            UserModel userModel = userModelList.get(position);

            holder.namaPengguna.setText(userModel.getDisplayName());
            holder.nope.setText(userModel.getPhone());
            holder.email.setText(userModel.getEmail());


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
        return userModelList.size();
    }
}
