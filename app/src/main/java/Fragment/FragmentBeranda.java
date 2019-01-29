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
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterDestinasi;
import Kelas.PaketTour;
import Kelas.UserPreference;
import cn.pedant.SweetAlert.SweetAlertDialog;
import myproject.travelpms.BerandaActivity;
import myproject.travelpms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBeranda extends Fragment {


    public FragmentBeranda() {
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
    AdapterDestinasi adapterDestinasi;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    private List<PaketTour> paketTourList;
    String level,paketTour;
    UserPreference mUserpref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_container, container, false);
        Firebase.setAndroidContext(getActivity());
        Firebase.setAndroidContext(this.getActivity());
        FirebaseApp.initializeApp(this.getActivity());
        ref = FirebaseDatabase.getInstance().getReference();
        final BerandaActivity berandaActivity = new BerandaActivity();

        mUserpref = new UserPreference(this.getActivity());
        level = mUserpref.getBagian();
        if (level.equals("Instansi")){
            paketTour = "paket_instansi";
            Log.d("paketTour : ",paketTour);
            Log.d("levelUser : ",level);

        }else if (level.equals("Umum")){
            paketTour = "paket_umum";
            Log.d("paketTour : ",paketTour);
            Log.d("levelUser : ",level);

        }else if (level.equals("Sekolah")){
            paketTour = "paket_sekolah";
            Log.d("paketTour : ",paketTour);
            Log.d("levelUser : ",level);
        }

        paketTourList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view);
        adapterDestinasi = new AdapterDestinasi(this.getActivity(),paketTourList);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterDestinasi);

        pDialogLoading = new SweetAlertDialog(this.getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        ref.child("pakettour").child(paketTour).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                paketTourList.clear();
                adapterDestinasi.notifyDataSetChanged();

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String nama = child.child("namaPaket").getValue().toString();
                    String status = child.child("statusPaket").getValue().toString();
                    String downloadURL = child.child("downloadUrl").getValue().toString();
                    String durasi = child.child("durasiPaket").getValue().toString();
                    String harga = child.child("hargaPaket").getValue().toString();
                    String jmlPeserta = child.child("jumlahPeserta").getValue().toString();
                    String key = child.child("key").getValue().toString();
                    String fasilitas = child.child("fasilitasPaket").getValue().toString();

                    PaketTour paketTour = new PaketTour(
                     nama,
                     harga,
                     durasi,
                     jmlPeserta,
                     fasilitas,
                     key,
                     downloadURL,
                     status
                    );
                    paketTourList.add(paketTour);
                    adapterDestinasi.notifyDataSetChanged();

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
