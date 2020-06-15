package com.example.otherworld.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otherworld.Adapter.HomeSliderAdapter;
import com.example.otherworld.Adapter.LookbookAdapter;
import com.example.otherworld.BookingActivity;
import com.example.otherworld.Common.Common;
import com.example.otherworld.Database.CartDatabase;
import com.example.otherworld.Database.DatabaseUtits;
import com.example.otherworld.Interface.IBannerLoadListener;
import com.example.otherworld.Interface.IBookinginfoLoadListener;
import com.example.otherworld.Interface.ICountItemInCartListener;
import com.example.otherworld.Interface.ILookbookLoadListener;
import com.example.otherworld.Model.Banner;
import com.example.otherworld.Model.BookingInformation;
import com.example.otherworld.R;
import com.example.otherworld.Service.PicassoImageLoadingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;


public class HomeFragment extends Fragment implements IBannerLoadListener, ILookbookLoadListener, IBookinginfoLoadListener, ICountItemInCartListener {

    private Unbinder unbinder;

    CartDatabase cartDatabase;

    @BindView(R.id.notification_badge)
    NotificationBadge notificationBadge;

    @BindView(R.id.layout_user_information)
    LinearLayout layout_user_information;

    @BindView(R.id.txt_user_name)
    TextView txt_user_name;

    @BindView(R.id.banner_slider)
    Slider banner_slider;

    @BindView(R.id.recycler_bool_look)
    RecyclerView recycler_look_book;

    @BindView(R.id.card_booking_info)
    CardView card_booking_info;

    @BindView(R.id.txt_club_address)
    TextView txt_club_address;

    @BindView(R.id.txt_club_gamezone)
    TextView txt_club_gamezone;

    @BindView(R.id.txt_time)
    TextView txt_time;

    @BindView(R.id.txt_time_remain)
    TextView txt_time_remain;


    @OnClick(R.id.card_view_booking)
    void booking(){
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }

    public HomeFragment() {
        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        lookbookRef = FirebaseFirestore.getInstance().collection("Lookbook");
    }

    @Override
    public void onResume() {
        super.onResume();
        countCartItem();
        loadUserBooking();
    }

    //а теперь FireStore
    CollectionReference bannerRef,lookbookRef;
    // Интерфэйс
    IBannerLoadListener iBannerLoadListener;
    ILookbookLoadListener iLookbookLoadListener;
    IBookinginfoLoadListener iBookinginfoLoadListener;

    private void loadUserBooking() {
        CollectionReference userBooking = FirebaseFirestore.getInstance().collection("User").document(Common.currentUser.getPhoneNumber()).collection("Booking");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);

        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());

        userBooking
                .whereGreaterThanOrEqualTo("timestamp",toDayTimeStamp)
                .whereEqualTo("done",false)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().isEmpty())
                            {
                                for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                                {
                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                    iBookinginfoLoadListener.onBookinginfoSuccess(bookingInformation);
                                    break;
                                }

                            }
                            else
                                iBookinginfoLoadListener.onBookinginfoLoadEmpty();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBookinginfoLoadListener.onBookinginfoFailed(e.getMessage());
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this,view);
        cartDatabase = CartDatabase.getInstance(getContext());

        //Init
        Slider.init(new PicassoImageLoadingService());
        iBannerLoadListener = this;
        iLookbookLoadListener = this;
        iBookinginfoLoadListener = this;


        //Check is logged
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            layout_user_information.setVisibility(View.VISIBLE);
            setUserInformation();
            loadBanner();
            loadLookBook();
            loadUserBooking();
            countCartItem();
        }

        return view;
    }

    private void countCartItem() {
        DatabaseUtits.countItemInCart(cartDatabase,this);
    }

    private void loadLookBook() {
        lookbookRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Banner> lookbooks = new ArrayList<>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot bannerSnapShot:task.getResult())
                    {
                        Banner banner = bannerSnapShot.toObject(Banner.class);
                        lookbooks.add(banner);
                    }
                    iLookbookLoadListener.onLookbookLoadSuccess(lookbooks);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLookbookLoadListener.onLookbookLoadFailed(e.getMessage());
            }
        });
    }

    private void loadBanner() {
        bannerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Banner> banners = new ArrayList<>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot bannerSnapShot:task.getResult())
                    {
                        Banner banner = bannerSnapShot.toObject(Banner.class);
                        banners.add(banner);
                    }
                    iBannerLoadListener.onBannerLoadSuccess(banners);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBannerLoadListener.onBannerLoadFailed(e.getMessage());
            }
        });
    }

    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        try{
            txt_user_name.setText(Common.currentUser.getName());
        }catch (Exception e){ }

    }


    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));
    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLookbookLoadSuccess(List<Banner> banners) {
        recycler_look_book.setHasFixedSize(true);
        recycler_look_book.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_look_book.setAdapter(new LookbookAdapter(getActivity(),banners));
    }

    @Override
    public void onLookbookLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBookinginfoLoadEmpty() {
        card_booking_info.setVisibility(View.GONE);
    }

    @Override
    public void onBookinginfoSuccess(BookingInformation bookingInformation) {
        txt_club_address.setText(bookingInformation.getClubAddress());
        txt_club_gamezone.setText(bookingInformation.getGamezoneId());
        txt_time.setText(bookingInformation.getTime());
        String dateRemain = DateUtils.getRelativeTimeSpanString(Long.valueOf(bookingInformation.getTimestamp().toDate().getTime()),
                Calendar.getInstance().getTimeInMillis(),0).toString();
        txt_time_remain.setText(dateRemain);
    }

    @Override
    public void onBookinginfoFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartItemCountSuccess(int count) {
        notificationBadge.setText(String.valueOf(count));
    }
}