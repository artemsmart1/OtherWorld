package com.example.otherworld;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int APP_REQEST_CODE=1488;

    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.txt_skip)
    TextView txt_skip;

    @OnClick({R.id.btn_login)
    void login_user(){

      //  final intent intent = new intent (this, Account)
    }
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}