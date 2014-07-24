package com.lang.social.file;

import java.io.BufferedInputStream;
import java.io.IOException;

public class IOServerFileFetcher  {  
	
	private IOFileCallback ioFileCallback;
	
	public IOServerFileFetcher(IOFileCallback ioFileCallback) {
		this.ioFileCallback = ioFileCallback;
	}
	
	public void Get(final String key)  {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getStream(key);
			}
		}).start();
	}
	
	private void getStream(String key) {
		try {
			
			 FileServerController controller = new FileServerController();
			 BufferedInputStream inputStream = controller.Get(key);
			 ioFileCallback.onFileInputStreamRecieved(inputStream);
			 controller.Disconnect();
			 inputStream.close();
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
