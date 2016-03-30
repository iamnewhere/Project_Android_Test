package com.ism.cse.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@SuppressLint("ShowToast")
public class Result extends Activity implements OnClickListener {

    SharedPreferences pref;
    TextView adm, noq, out, mrk;
    Button b;
    Toast tout;
    String response,result;

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (!f1) {
            onClick(null);
        }
    }

    boolean flag, f1;
    int press;

    private void init() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        adm = (TextView) findViewById(R.id.tvRadm);
        noq = (TextView) findViewById(R.id.tvTotalQ);
        out = (TextView) findViewById(R.id.tvFinalOutput);
        mrk = (TextView) findViewById(R.id.tvMrkObtd);
        b = (Button) findViewById(R.id.bResult);
        b.setOnClickListener(this);
        tout = Toast.makeText(Result.this,
                "Result Received !! \nPress Again to View your Result",
                Toast.LENGTH_LONG);
        flag = false;
        press = 1;
        result="";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        f1 = false;
        init();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!pref.getString("ADM", "20XXJEXXXX").isEmpty())
            adm.setText(pref.getString("ADM", "20XXJEXXXX"));
        else
            adm.setText("20XXJEXXXX");

        noq.setText("No. of Questions = " + pref.getInt("TOTALQ", 3));

        Bundle get = getIntent().getExtras();
        response = get.getString("ANS");
        response+="$"+pref.getString("ADM","null");

        out.setText("Questions Attempted = "+pref.getInt("ATTEMPTS", 0));

    }

    public class Client extends AsyncTask<Void, Void, Void> {
        String ipAddr;  //str;
        int sPort;

        Client() {
            ipAddr = pref.getString("IP", "10.0.0.5");
            sPort = pref.getInt("PORT", 8080);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            Socket socket = null;
            DataOutputStream dout = null;
            DataInputStream din = null;
            try {
                socket = new Socket(ipAddr, sPort);
                dout = new DataOutputStream(socket.getOutputStream());
                din = new DataInputStream(socket.getInputStream());
                dout.writeUTF(response);
                result = din.readUTF();     //str=din.readUTF();
                if (!result.isEmpty()) {    //str.equals("result");
                    flag = true;
                    press = 2;
                    tout.show();
                }
                socket.close();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (socket != null)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                if (dout != null) {
                    try {
                        dout.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (din != null) {
                    try {
                        din.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (press == 1) {
            f1 = true;
            Client c = new Client();
            c.execute();
        }
        if (flag) {
            mrk.setText(result);
            out.setVisibility(android.view.View.VISIBLE);
            mrk.setVisibility(android.view.View.VISIBLE);
        }
    }

}
