package Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import Adapter.AdapterAdminPaket;
import Adapter.AdapterListPengguna;
import Kelas.PaketTour;
import Kelas.UserModel;
import cn.pedant.SweetAlert.SweetAlertDialog;
import myproject.travelpms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListPengguna extends Fragment {


    public FragmentListPengguna() {
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
    AdapterListPengguna adapter;
    private List<UserModel> userModelList;
    private SweetAlertDialog pDialogLoading,pDialodInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_admin_list_pengguna, container, false);
        Firebase.setAndroidContext(getActivity());
        Firebase.setAndroidContext(this.getActivity());
        FirebaseApp.initializeApp(this.getActivity());
        ref = FirebaseDatabase.getInstance().getReference();

        userModelList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdapterListPengguna(this.getActivity(),userModelList);


        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        pDialogLoading = new SweetAlertDialog(this.getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Menampilkan data..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userModelList.clear();
                adapter.notifyDataSetChanged();

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String nama = child.child("displayName").getValue().toString();
                    String check = child.child("check").getValue().toString();
                    String email = child.child("email").getValue().toString();
                    String phone = child.child("phone").getValue().toString();
                    String token = child.child("token").getValue().toString();
                    String level = child.child("level").getValue().toString();
                    String last_login = child.child("last_login").getValue().toString();
                    String uid = child.child("uid").getValue().toString();

                    if (!level.equals("admin")){
                        UserModel userModel = new UserModel(
                            uid,
                                nama,
                                token,
                                last_login,
                                check,
                                phone,
                                level,
                                email
                        );

                        userModelList.add(userModel);
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
