package com.example.otherworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otherworld.Common.Common;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.provider.FirebaseInitProvider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int APP_REQEST_CODE=1488;

    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.txt_skip)
    TextView txt_skip;

    @OnClick(R.id.btn_login)
    void login_user()
    {
        startActivityForResult(AuthUI.getInstance().
                createSignInIntentBuilder().
                setAvailableProviders(providers).build(),APP_REQEST_CODE);

    }

    @OnClick(R.id.txt_skip)
    void skipLogin(){
        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra(Common.IS_LOGIN,false);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        if(authStateListener != null)
            firebaseAuth.removeAuthStateListener(authStateListener);
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth1 -> {
            FirebaseUser user = firebaseAuth1.getCurrentUser();
            if(user != null)
            {

               checkUserFromFirebase();
            }

        };


        Dexter.withActivity(this).withPermissions(new String[]{
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
        }).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                firebaseAuth = FirebaseAuth.getInstance();
                authStateListener = FirebaseAuth ->
                {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null)//уже залогинин
                    {

                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            Common.updateToken(task.getResult().getToken());

                                            Log.d("Token",task.getResult().getToken());

                                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                            intent.putExtra(Common.IS_LOGIN, true);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra(Common.IS_LOGIN, true);
                                startActivity(intent);
                                finish();
                            }
                        });



                    } else {
                        setContentView(R.layout.activity_main);
                        ButterKnife.bind(MainActivity.this);
                    }
                };
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
    }

    private void checkUserFromFirebase() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null)//уже залогинин
        {

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful()) {
                                Common.updateToken(task.getResult().getToken());

                                Log.d("Token", task.getResult().getToken());

                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra(Common.IS_LOGIN, true);
                                startActivity(intent);
                                finish();
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra(Common.IS_LOGIN, true);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == APP_REQEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            }
            else
                {
                    Toast.makeText(this,"Не удалось войти",Toast.LENGTH_SHORT).show();
                }

        }

    }


}