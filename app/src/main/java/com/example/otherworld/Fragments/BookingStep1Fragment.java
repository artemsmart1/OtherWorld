package com.example.otherworld.Fragments;

import android.app.AlertDialog;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otherworld.Adapter.MyClubAdapter;
import com.example.otherworld.Common.Common;
import com.example.otherworld.Common.SpaceItemDecoration;
import com.example.otherworld.Interface.IAllClubLoadListener;
import com.example.otherworld.Interface.IBannerLoadListener;
import com.example.otherworld.Interface.IBranchLoadListener;
import com.example.otherworld.Model.Banner;
import com.example.otherworld.Model.Club;
import com.example.otherworld.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;


public class BookingStep1Fragment extends Fragment implements IAllClubLoadListener, IBranchLoadListener {

    CollectionReference allClubRef;
    CollectionReference branchRef;

    IAllClubLoadListener iAllClubLoadListener;
    IBranchLoadListener iBranchLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;

    @BindView(R.id.recycler_club)
    RecyclerView recycler_club;

    Unbinder unbinder;
    AlertDialog dialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance(){
        if (instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allClubRef = FirebaseFirestore.getInstance().collection("Address");
        iAllClubLoadListener = this;
        iBranchLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View itemView = inflater.inflate(R.layout.fragmet_booking_step_one,container,false);
         unbinder = ButterKnife.bind(this,itemView);

         initView();
         loadAllClub();

         return itemView;
    }

    private void initView() {
        recycler_club.setHasFixedSize(true);
        recycler_club.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_club.addItemDecoration(new SpaceItemDecoration(4));
    }

    private void loadAllClub() {
        allClubRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<String> list = new ArrayList<>();
                    list.add("Пожалуйста выберете клуб");
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                        list.add(documentSnapshot.getId());
                    iAllClubLoadListener.onAllClubLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllClubLoadListener.onAllClubLoadFailed(e.getMessage());
            }
        });


    }

    @Override
    public void onAllClubLoadSuccess(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0 )
                {
                    loadBranchOfCity(item.toString());
                }
                else
                    recycler_club.setVisibility(View.GONE);
            }
        });
    }

    private void loadBranchOfCity(String cityName) {
        dialog.show();

        Common.city = cityName;

        branchRef = FirebaseFirestore.getInstance().collection("Address").document(cityName).collection("Branch");

        branchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Club> list = new ArrayList<>();
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        Club club = documentSnapshot.toObject(Club.class);
                        club.setClubId(documentSnapshot.getId());
                        list.add(club);
                    }

                    iBranchLoadListener.onBranchLoadSuccess(list);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public void onAllClubLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBranchLoadSuccess(List<Club> clubList) {
        MyClubAdapter adapter = new MyClubAdapter(getActivity(),clubList);
        recycler_club.setAdapter(adapter);
        recycler_club.setVisibility(View.VISIBLE );
        dialog.dismiss();
    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
