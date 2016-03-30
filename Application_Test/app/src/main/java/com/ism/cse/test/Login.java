package com.ism.cse.test;

import java.io.DataInputStream;
//import android.support.v7.app.AppCompatActivity;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	Button log;
	EditText ip, port, adm;
	Toast out, out1, err;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	int prt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@SuppressLint("ShowToast")
	private void init() {
		log = (Button) findViewById(R.id.bLogin);
		ip = (EditText) findViewById(R.id.etIp);
		port = (EditText) findViewById(R.id.etPort);
		adm = (EditText) findViewById(R.id.etAdm);
		out = Toast.makeText(Login.this, "Login Failed... Try Again!",
				Toast.LENGTH_SHORT);
		out1 = Toast.makeText(Login.this, "Successfully Logged-In!!",
				Toast.LENGTH_SHORT);
		err = Toast.makeText(Login.this, "Please Input Admission No. First!!",
				Toast.LENGTH_SHORT);
		log.setOnClickListener(this);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = pref.edit();
		editor.putString("PASSWORD", "pass");
		editor.apply();
	}

	@Override
	public void onClick(View var) {
		if (!port.getText().toString().equals(""))
			prt = Integer.parseInt(port.getText().toString());
		else
			prt = 8080;
		if(!adm.getText().toString().equals("")){
			Client c = new Client(ip.getText().toString(), prt);
			c.execute();
		}else
			err.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// editor.putString("PASSWORD", "");
		// editor.apply();
		// finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		super.onOptionsItemSelected(item);
		finish();
		return true;
	}

	public class Client extends AsyncTask<Void, Void, Void> {
		String ipAddr, password = "kill", s;
		int sPort;

		Client(String addr, int port) {
			ipAddr = addr;
			sPort = port;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			Socket socket = null;
			DataOutputStream dout = null;
			DataInputStream din = null;
			try {
				socket = new Socket(ipAddr, sPort);
				dout = new DataOutputStream(socket.getOutputStream());
				din = new DataInputStream(socket.getInputStream());
				s = adm.getText().toString();
				dout.writeUTF(s);
				password = din.readUTF();
				if (password.equals(pref.getString("PASSWORD", "pass"))) {
					editor.putInt("PORT", sPort);
					editor.putString("IP", ipAddr);
					editor.putString("ADM", s);
					editor.apply();
					Intent i = new Intent(Login.this, Details.class);
					out1.show();
					startActivity(i);
				} else {
					out.show();
				}
			} catch (UnknownHostException e) {
				out.show();
				e.printStackTrace();
			} catch (IOException e) {
				out.show();
				e.printStackTrace();
			} finally {
				if (socket != null)
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				if (dout != null) {
					try {
						dout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (din != null) {
					try {
						din.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

	}
}
