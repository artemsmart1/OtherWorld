package com.example.otherworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.otherworld.Common.Common;
import com.example.otherworld.Fragments.GamesFragment;
import com.example.otherworld.Fragments.HomeFragment;
import com.example.otherworld.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;

    CollectionReference userRef;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);

        //Init
        userRef = FirebaseFirestore.getInstance().collection("User");
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        //Проверка сети
        if(getIntent() != null){
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN,false);
            if(isLogin){
                //проверяем существование юзера
               dialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference currentUser = userRef.document(user.getPhoneNumber());
                currentUser.get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                DocumentSnapshot userSnapShot = task.getResult();
                                if(!userSnapShot.exists()){

                                    showUpdateDialog(user.getPhoneNumber());
                                }
                                else { //Если юзер уже в системе
                                    Common.currentUser = userSnapShot.toObject(User.class);
                                    bottomNavigationView.setSelectedItemId(R.id.action_home);

                                }
                                if(dialog.isShowing())
                                    dialog.dismiss();

                            }
                        });
             }
        }

        //Вид
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();
                else if(menuItem.getItemId() == R.id.action_playing)
                    fragment = new GamesFragment();
                return loadFragment(fragment);
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contain,fragment).commit();
            return true;
        }
        return false;
    }

    private void showUpdateDialog(final String phoneNumber) {


        //Создаю диалог
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("Последний шаг");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information,null);

        Button btn_update = (Button)sheetView.findViewById(R.id.btn_update);
        TextInputEditText edt_name = (TextInputEditText)sheetView.findViewById(R.id.edt_name);
        TextInputEditText edt_address = (TextInputEditText)sheetView.findViewById(R.id.edt_address);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dialog.isShowing())
                    dialog.show();

                final User user = new User(edt_name.getText().toString(),
                        edt_address.getText().toString(), phoneNumber);
                userRef.document(phoneNumber)
                        .set(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                bottomSheetDialog.dismiss();
                                if(dialog.isShowing())
                                    dialog.dismiss();

                                Common.currentUser = user;

                                Toast.makeText(HomeActivity.this, "Спасибо", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bottomSheetDialog.dismiss();
                        if(dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();

    }
}