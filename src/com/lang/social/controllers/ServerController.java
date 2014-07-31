package com.lang.social.controllers;

import io.socket.SocketIO;

import java.net.MalformedURLException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.utils.MyToaster;

public class ServerController  {

	 private static final String TAG = "SOCKET"; 
	 
	 public static final String  LOCAL_SERVER_URL = "http://192.168.198.22:7979";
	 private static final String NODEJITSU_SERVER_URL = "http://lang.social.com.jit.su/";
	 private static final String HEROKU_SERVER_URL = "http://radiant-reef-4113.herokuapp.com/";
	 
	 //gottox socket.io-client
	 private static SocketIO socket;
	 
	 private ServerController(){}
	 
	 public static void connectToServer(Context context) throws MalformedURLException {
		 if(socket != null){
			 socket.disconnect();
		 }
		 
		 socket = new SocketIO();
	
		 //productionConnect(context);
		 
		 developmentConnect(context);

	 }
	 


	private static void developmentConnect(Context context) throws MalformedURLException {
		 socket.connect(LOCAL_SERVER_URL, IOCallBackHandler.getInstance()); 
	}

	private static void productionConnect(Context context) throws MalformedURLException {
		 WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		 if (wifi.isWifiEnabled()){
			 //main server (when wifi enabled)
			 socket.connect(NODEJITSU_SERVER_URL, IOCallBackHandler.getInstance()); 
			 //socket.connect(LOCAL_SERVER_URL, IOCallBackHandler.getInstance()); 
		 } else {
			 //backup server (when no wifi enabled)
			 socket.connect(HEROKU_SERVER_URL, IOCallBackHandler.getInstance()); 
			 //socket.connect(LOCAL_SERVER_URL, IOCallBackHandler.getInstance()); 
			 MyToaster.showToast((Activity)context, "Enable wifi for faster connection", Toast.LENGTH_SHORT);
		 }
	}

	public static boolean wasConnectionEstablished(){
		 return socket.isConnected();
	 }
	 
	 public static void sendJSONMessage(String key ,JSONObject jsonToSend) {
		 if(jsonToSend != null){
			 Log.d(TAG, "sending to server: " + jsonToSend.toString());
		 }
		 Log.d(TAG, "key: " + key);
		 socket.emit(key, jsonToSend);
	 }
	 
	 
	 
	 public static void sendStringMessage(String key ,String stringToSend) {
		 if(stringToSend != null){
			 Log.d(TAG, "sending to server: " + stringToSend);
		 }
		 Log.d(TAG, "key: " + key);
		 socket.emit(key, stringToSend);
	 }

}
