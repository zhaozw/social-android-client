package com.lang.social.file;

import java.io.BufferedInputStream;

import com.lang.social.controllers.ServerController;

public class FileServerController {
	
	private FileSocketClient socket;
	
	public FileServerController() {
		
		//socket = new FileSocketClient(
		//		ServerController.LOCAL_FILE_SERVER_IP,
		//		ServerController.LOCAL_FILE_SERVER_PORT);
	}
	
	public BufferedInputStream Get(final String key) {
		Connect();
		BufferedInputStream stream = socket.GetInputStream(key);
		return stream;
	}
	
	
	private void Connect(){
		socket.Connect();
	}
	
	public void Disconnect(){
		socket.Disconnect();
	}
	
}
