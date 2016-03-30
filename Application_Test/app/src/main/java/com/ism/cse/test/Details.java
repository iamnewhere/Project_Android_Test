package com.ism.cse.test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Scanner;

public class Details extends Activity implements OnClickListener {

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if(flag)
            finish();
    }

    Button beg, select;
    String path;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        init();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void init() {
        beg = (Button) findViewById(R.id.bBegin);
        select = (Button) findViewById(R.id.bSelect);
        beg.setOnClickListener(this);
        select.setOnClickListener(this);
        flag = false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bBegin:
                if (flag) {
                    start_next_activity();
                } else {
                    Toast t = Toast.makeText(Details.this, "Please Select Correct File First", Toast.LENGTH_SHORT);
                    t.show();
                }
                break;
            case R.id.bSelect:
                //works with ES,File Comm,Astro,File Exp(Next App)
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("text/plain");
                startActivityForResult(i, 0);
                break;
        }
    }

    private void start_next_activity() {
        save_path();
        Intent i = new Intent(Details.this, Quest.class);
        startActivity(i);
        finish();
    }

    private void set_flag() {
        try {
            File f = new File(path);
            Scanner fr = new Scanner(f);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
    }

    private void save_path() {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PATH", path);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            path = data.getData().getPath();
            set_flag();
        }
    }
}
