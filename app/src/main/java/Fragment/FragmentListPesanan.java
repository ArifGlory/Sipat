package Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterListPengguna;
import Adapter.AdapterListPesanan;
import Kelas.ListPesanan;
import Kelas.Pesanan;
import Kelas.UserModel;
import cn.pedant.SweetAlert.SweetAlertDialog;
import myproject.travelpms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListPesanan extends Fragment {


    public FragmentListPesanan() {
        // Required empty public constructor
    }

    TextView txtNotif;
    public static ProgressBar progressBar;
    public static TextView txtNamaPSayur;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String statusPsayur;
    private RecyclerView recyclerView;
    AdapterListPesanan adapter;
    private List<Pesanan> listPesanan;
    private SweetAlertDialog pDialogLoading,pDialodInfo;

    private int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_admin_list_pesanan, container, false);
        Firebase.setAndroidContext(getActivity());
        Firebase.setAndroidContext(this.getActivity());
        FirebaseApp.initializeApp(this.getActivity());
        ref = FirebaseDatabase.getInstance().getReference();

        listPesanan = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdapterListPesanan(this.getActivity(),listPesanan);


        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        pDialogLoading = new SweetAlertDialog(this.getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

       ref.child("pesanan").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               listPesanan.clear();
               adapter.notifyDataSetChanged();
               for (DataSnapshot child : dataSnapshot.getChildren()){
                   String idUser = child.child("idUser").getValue().toString();
                   String idPaket = child.child("idPaket").getValue().toString();
                   String status = child.child("status").getValue().toString();
                   String jenisPaket = child.child("jenisPaket").getValue().toString();
                   String keyPesanan = child.getKey();
                   String foto = child.child("foto").getValue().toString();
                   String tanggal = child.child("tanggal").getValue().toString();
                   String namaPaket = child.child("namaPaket").getValue().toString();
                   String namaPengguna = child.child("namaPengguna").getValue().toString();
                   String keterangan = child.child("keterangan").getValue().toString();
                   String jmlPenumpang = child.child("jmlPenumpang").getValue().toString();
                   String totalHarga = child.child("totalHarga").getValue().toString();


                   Pesanan pesanan = new Pesanan(
                           idUser,
                           idPaket,
                           tanggal,
                           keterangan,
                           keyPesanan,
                           jenisPaket,
                           status,
                           namaPaket,
                           namaPengguna,
                           foto,
                           jmlPenumpang,
                           totalHarga
                   );

                   if (!status.equals("D")){
                       listPesanan.add(pesanan);
                       adapter.notifyDataSetChanged();
                   }

               }

               pDialogLoading.dismiss();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

        return view;
    }




}
