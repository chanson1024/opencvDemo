package com.xqs.cvdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by xiaoqingsong
 * Date: 14/07/2017
 * Time: 9:29 PM
 */

public class HomeActivity extends Activity {

    private Button mBtn01;
    private Button mBtn02;
    private Button mBtn03;
    private Button mBtn04;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBtn01 = (Button)findViewById(R.id.btn_01);
        mBtn02 = (Button)findViewById(R.id.btn_02);
        mBtn03 = (Button)findViewById(R.id.btn_03);
        mBtn04 = (Button)findViewById(R.id.btn_04);

        mBtn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
            }
        });

        mBtn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,Test01Activity.class));
            }
        });

        mBtn03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,Test02Activity.class));
            }
        });

        mBtn04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,Test03Activity.class));
            }
        });

    }
}
