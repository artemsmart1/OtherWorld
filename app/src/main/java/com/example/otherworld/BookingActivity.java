package com.example.otherworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;

import com.example.otherworld.Adapter.MyViewPagerAdapter;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingActivity extends AppCompatActivity {

    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;

    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        setupStepView();
        setColorButton();

        //view
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);
                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setColorButton() {
        if (btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(R.color.btn_color);
        }
        else{
            btn_next_step.setBackgroundResource(R.color.btn_color_2);
        }

        if (btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(R.color.btn_color);
        }
        else{
            btn_previous_step.setBackgroundResource(R.color.btn_color_2);
        }

    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Клуб");
        stepList.add("Игровая зона");
        stepList.add("Время");
        stepList.add("Подтвердить");
        stepView.setSteps(stepList);
    }
}