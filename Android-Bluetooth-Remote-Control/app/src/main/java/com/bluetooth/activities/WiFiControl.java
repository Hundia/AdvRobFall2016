/**
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bluetooth.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.Enumeration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bluetooth.BluetoothActivity;
import com.bluetooth.BluetoothRemoteControlApp;
import com.bluetooth.R;

/**
 * This activity creates a server with the absolute minimum to make HTTP
 * requests. Once launched the user enters the IP that is shown on the phone in
 * a browser on a computer and as long as the phone and the computer are on the
 * same network the user will be able to remotely control the Bluetooth device
 * from the browser.
 * <p>
 * There are some issues with this activity though: there's a very noticeable
 * lag between issuing commands in the browser and the Bluetooth device
 * reacting. Some messages are lost.
 * <p>
 * This was originally made to stream the phone's camera preview feed to the
 * browser (basically an IP camera), but due to lack knowledge it was abandoned.
 * If you know enough about Java and Android I encourage you to add more
 * features to the front end.
 */
public class WiFiControl extends BluetoothActivity
{
	private static final String ALL_PATTERN = "*";

	private boolean isRunning = false;
	private String log;

	ClientThread ct = null;

	private EditText tvIP;
	private LogView tvData;
	private Button bToggle;

//	private ServerSocket serverSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_control);

		tvData = (LogView) findViewById(R.id.tvData);
		tvIP = (EditText) findViewById(R.id.serverIpTf);
		bToggle = (Button) findViewById(R.id.bToggle);
		log = "";
	}

	public boolean handleMessage(Message msg)
	{
		// Update UI
		switch(msg.what)
		{
		case BluetoothRemoteControlApp.MSG_1:
			TextView tv = (TextView) findViewById(msg.arg1);
			tv.setText(msg.obj.toString());
			break;
		case BluetoothRemoteControlApp.MSG_READ:
			tvData.append(msg.obj.toString() + "\n");
			log += msg.obj + "<br />";
			break;
		case BluetoothRemoteControlApp.MSG_WRITE:
			tvData.append(msg.obj.toString() + "\n");
			log += msg.obj + "<br />";
			break;
		}
		return super.handleMessage(msg);
	}

	public void buttonClick(View v)
	{
		isRunning = !isRunning;
		if(isRunning)
		{
			// This is a blocking function, it will wait for a client to connect
			ct = new ClientThread(tvIP.getText().toString());
			Thread t = new Thread(ct);
			t.start();
			//	Set the client thread
			setClientThread(ct);
			bToggle.setText(R.string.serverStop);
		}
		else
		{
			tvIP.setText("");
			bToggle.setText(R.string.serverStart);
		}
	}

	/**
	 * This method gets an IPv4 address for the user to enter in the browser.
	 * 
	 * @return The IP
	 */
	private String getLocalIpAddress()
	{
		try
		{
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if(!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress()))
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		catch(SocketException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if(ct != null)
		{
//			try
//			{
//				ct.close();
//			}
//			catch(IOException e)
//			{
//				e.printStackTrace();
//			}
		}
	}

	@Override
	public void onBackPressed()
	{
		isRunning = false;
		super.onBackPressed();
	}
}
