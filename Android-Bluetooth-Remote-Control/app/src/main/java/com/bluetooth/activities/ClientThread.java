package com.bluetooth.activities;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientThread implements Runnable {

	public static final int SERVERPORT = 1234;
	public Socket socket;
//	public byte[] sendData;
	public String sendData;
	private InetAddress serverAddr;

	public ClientThread(String serverIp) {
		try {
			serverAddr = InetAddress.getByName(serverIp);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		sendData = "";
	}
	
	public void close() throws IOException {
		socket.close();
	}
	
    public void run() {
    	
    	try {
			socket = new Socket(serverAddr, SERVERPORT);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
    	while(true) {
        	try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	if(sendData != "") {
	        	try {
	            	System.out.println("Sending cmd: " + sendData);
	            	
	//    			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddr, SERVERPORT);
	    			
	    			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
	    			
	    			outToServer.writeBytes(sendData);
	    			
	    			sendData = "";
	//    			socket.close();
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }	
        	}
        }
    }
    
    public static void main(String[] args) {
    	ClientThread ct = new ClientThread("127.0.0.1");
    	Thread t = new Thread(ct);
		t.start();
		// create a scanner so we can read the command-line input
	    Scanner scanner = new Scanner(System.in);

	    while(true) {
	    	// get their input as a String
		    String cmd = scanner.next();
		    System.out.println("Received command: " + cmd);
		    ct.sendData = cmd + '\n';
	    }
	    

	    

	}

}