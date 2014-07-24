package com.lang.social.controllers;

import io.socket.SocketIO;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.json.JSONObject;

import android.util.Log;

import com.google.gson.JsonObject;
import com.lang.social.iocallback.IOCallBackHandler;

public class ServerController  {

	 private static final String TAG = "SOCKET"; 
	 
	 public static final String LOCAL_SERVER_URL = "http://10.0.0.9:8000";
	 
	 private static final String NODEJITSU_SERVER_URL = "http://lang.social.com.jit.su/";
	 private static final String HEROKU_SERVER_URL = "http://ancient-taiga-2396.herokuapp.com/";
	 
	 private static SocketIO socket;
	 
	 private ServerController(){}
	 
	 public static void connectToServer() throws MalformedURLException {
		 if(socket != null){
			 socket.disconnect();
		 }

		socket = new SocketIO();
		socket.connect(NODEJITSU_SERVER_URL, IOCallBackHandler.getInstance()); 
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
		 socket.emit(key, stringToSend);
	 }

}
