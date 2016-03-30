package com.ism.cse.test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import java.io.File;
import java.util.Scanner;

public class Quest extends Activity implements OnClickListener,
        OnCheckedChangeListener {

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (!flag) {
            calc();
            result();
        }
        finish();
    }

    boolean flag;
    Button next, back, submit;
    RadioGroup rg;
    RadioButton r1, r2, r3, r4;
    TextView ques;
    String q[], o1[], o2[], o3[], o4[], path, ans;
    int n, i, score, attempts, correct, total, mul[], opt[];    //ans[];
    SharedPreferences pref;
    SharedPreferences.Editor e;

    private void read_file() {
        try {
            path = pref.getString("PATH", Environment.getExternalStorageDirectory() + "/dat.test");

            File f = new File(path);
            Scanner fr = new Scanner(f);

            n = Integer.valueOf(fr.nextLine());

            set_vars();

            for (int k = 0; k < n; k++) {
                //s+="1";
                q[k] = fr.nextLine();
                o1[k] = fr.nextLine();
                o2[k] = fr.nextLine();
                o3[k] = fr.nextLine();
                o4[k] = fr.nextLine();
                //ans[k] = Integer.valueOf(fr.nextLine());
                mul[k] = Integer.valueOf(fr.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void set_vars() {
        total = n;
        mul = new int[n];
        q = new String[n];
        o1 = new String[n];
        o2 = new String[n];
        o3 = new String[n];
        o4 = new String[n];
        //ans = new int[n];
        opt = new int[n];
        for (i = 0; i < n; i++)
            opt[i] = -1;
        e.putInt("TOTALQ", n);
        e.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest);

        flag = false;
        init();
        read_file();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        i = 0;
        set_pg();
    }

    private void init() {
        next = (Button) findViewById(R.id.bNext);
        back = (Button) findViewById(R.id.bBack);
        submit = (Button) findViewById(R.id.bSubmit);
        rg = (RadioGroup) findViewById(R.id.rgQues);
        r1 = (RadioButton) findViewById(R.id.r1);
        r2 = (RadioButton) findViewById(R.id.r2);
        r3 = (RadioButton) findViewById(R.id.r3);
        r4 = (RadioButton) findViewById(R.id.r4);
        ques = (TextView) findViewById(R.id.tvQues);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        rg.setOnCheckedChangeListener(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        e = pref.edit();
    }

    private void calc() {
        total = 0;
        attempts = 0;
        ans = "";
        char ch;
        for (int i = 0; i < n; i++) {
            ch=(char)('A' - 1 + opt[i]);
            ans +=  ch;
            if (opt[i] != -1)
                attempts++;
            total += mul[i];
        }
        e.putInt("TOTALMARKS", total);
        e.putInt("ATTEMPTS", attempts);
        e.apply();
        ques.setText("abcd");
    }

    private void result() {
        Bundle data = new Bundle();
        data.putString("ANS",ans);
        Intent i = new Intent(Quest.this, Result.class);
        i.putExtras(data);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        rg.clearCheck();
        switch (v.getId()) {
            case R.id.bNext:
                i = (i + 1) % n;
                break;
            case R.id.bBack:
                i = (i - 1) % n;
                if (i < 0)
                    i = n - 1;
                break;
            case R.id.bSubmit:
                flag = true;
                calc();
                result();
                break;
        }
        set_pg();
    }

    private void set_pg() {
        switch (opt[i]) {
            case 1:
                r1.toggle();
                break;
            case 2:
                r2.toggle();
                break;
            case 3:
                r3.toggle();
                break;
            case 4:
                r4.toggle();
                break;
        }
        ques.setText((i + 1) + ". " + q[i] + "\nMax. Mrks = " + mul[i]);
        r1.setText(o1[i]);
        r2.setText(o2[i]);
        r3.setText(o3[i]);
        r4.setText(o4[i]);
    }

    @Override
    public void onCheckedChanged(RadioGroup v, int v1) {
        // TODO Auto-generated method stub
        switch (v1) {
            case R.id.r1:
                opt[i] = 1;
                break;
            case R.id.r2:
                opt[i] = 2;
                break;
            case R.id.r3:
                opt[i] = 3;
                break;
            case R.id.r4:
                opt[i] = 4;
                break;
        }
    }

}
