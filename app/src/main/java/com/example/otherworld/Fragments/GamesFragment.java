package com.example.otherworld.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.otherworld.Adapter.MyShoppingItemAdapter;
import com.example.otherworld.Common.SpaceItemDecoration;
import com.example.otherworld.Interface.IShoppingDataLoadListener;
import com.example.otherworld.Model.ShoppingItem;
import com.example.otherworld.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class GamesFragment extends Fragment implements IShoppingDataLoadListener {

    IShoppingDataLoadListener iShoppingDataLoadListener;
    CollectionReference shoppingItemRef;
    Unbinder unbinder;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;

    @BindView(R.id.chip_coupon)
    Chip chip_coupon;
    @OnClick(R.id.chip_coupon)
    void couponChipClick(){
        setSelectedChip(chip_coupon);
        loadShoppingItem("Coupon");

    }

    @BindView(R.id.recycle_items)
    RecyclerView recycler_items;

    private void loadShoppingItem(String itemMenu) {

        shoppingItemRef = FirebaseFirestore.getInstance().collection("Shopping").document(itemMenu).collection("Items");
        //Получаем инфу из бд
        shoppingItemRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iShoppingDataLoadListener.onShoppingLoadDataFailed(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<ShoppingItem> shoppingItems = new ArrayList<>();
                    for(QueryDocumentSnapshot itemSnapShot:task.getResult())
                    {
                        ShoppingItem shoppingItem = itemSnapShot.toObject(ShoppingItem.class);
                        shoppingItem.setId(itemSnapShot.getId());
                        shoppingItems.add(shoppingItem);
                    }
                    iShoppingDataLoadListener.onShoppingLoadDataSuccess(shoppingItems);
                }
            }
        });
    }

    private void setSelectedChip(Chip chip) {
        //Меняю цвет
        for(int i = 0;i<chipGroup.getChildCount();i++){
            Chip chipItem = (Chip)chipGroup.getChildAt(i);
            if(chipItem.getId() != chip.getId())//если не выбрана
            {
                chipItem.setChipBackgroundColorResource(android.R.color.holo_blue_light);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));
            }
            else  //если выбрана
            {
                chipItem.setChipBackgroundColorResource(android.R.color.holo_purple);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));
            }

        }
    }


    public GamesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_games, container, false);

        unbinder = ButterKnife.bind(this,itemView);
        iShoppingDataLoadListener = this;

        loadShoppingItem("Coupon");
        
        initView();

        return itemView;
    }

    private void initView() {
        recycler_items.setHasFixedSize(true);
        recycler_items.setLayoutManager(new GridLayoutManager(getContext(),1));
        recycler_items.addItemDecoration(new SpaceItemDecoration(8));
    }

    @Override
    public void onShoppingLoadDataSuccess(List<ShoppingItem> shoppingItemList) {
        MyShoppingItemAdapter adapter = new MyShoppingItemAdapter(getContext(),shoppingItemList);
        recycler_items.setAdapter(adapter);
    }

    @Override
    public void onShoppingLoadDataFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}