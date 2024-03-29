package com.ixdcth.android.client;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Activity {

	private EditText serverIp;

	private String serverIpAddress = "";
	private int SERVERPORT = 8080;
	private boolean connected = false;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);

		serverIp = (EditText) findViewById(R.id.serverIP);
	}

	public void onClick(View v) {
		if (!connected) {
			serverIpAddress = serverIp.getText().toString();
			if (!serverIpAddress.equals("")) {
				Thread cThread = new Thread(new ClientThread());
				cThread.start();
			}
		}
	}

	public class ClientThread implements Runnable {

		public void run() {
			try {
				InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
				Log.d("ClientActivity", "C: Connecting...");
				Socket socket = new Socket(serverAddr, SERVERPORT);
				connected = true;
				while (connected) {
					try {
						Log.d("ClientActivity", "C: Sending command.");
						PrintWriter out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										socket.getOutputStream())), true);
						// WHERE YOU ISSUE THE COMMANDS
						out.println("Hey Server!");
						Log.d("ClientActivity", "C: Sent.");
					} catch (Exception e) {
						Log.e("ClientActivity", "S: Error", e);
					}
				}
				socket.close();
				Log.d("ClientActivity", "C: Closed.");
			} catch (Exception e) {
				Log.e("ClientActivity", "C: Error", e);
				connected = false;
			}
		}
	}

}
