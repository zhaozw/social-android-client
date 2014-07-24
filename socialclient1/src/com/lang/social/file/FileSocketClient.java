package com.lang.social.file;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class FileSocketClient
{
	  private int port;
	  private String serverIp;
	  private Socket socket;
	  
	  public FileSocketClient(String serverIp, int port) {
		  this.port = port;
		  this.serverIp = serverIp;
	  }
	  
	  public void Connect(){
	      try {
			socket = connectToServer(serverIp, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  public void Disconnect(){
	      try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	  
	  public BufferedInputStream GetInputStream(String key) {
	      // write-to, and read-from the socket.
	      // in this case just write a simple command to a web server.
		   BufferedInputStream stream = null;
			try {
				stream = getInputStreamFromServer(key);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return stream;
	  }

	  private BufferedInputStream getInputStreamFromServer(String writeTo) throws IOException
	  {
		BufferedInputStream inputStream = null;
	    try
	    {
	      // write key to the socket
	      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	      bufferedWriter.write(writeTo);
	      bufferedWriter.flush();
	       
	      // read image from the socket stream
	      inputStream = new BufferedInputStream(socket.getInputStream());
	    } 

	    catch (IOException e) {
	      e.printStackTrace();
	      throw e;
	    }
	    
	    // return the inputStream stream from the server
		return inputStream;
	  }
	   
	  /**
	   * Open a socket connection to the given server on the given port.
	   * This method currently sets the socket timeout value to 10 seconds.
	   * (A second version of this method could allow the user to specify this timeout.)
	   */
	  private Socket connectToServer(String server, int port) throws Exception
	  {
	    // create a socket with a timeout
	    try
	    {
	      InetAddress inteAddress = InetAddress.getByName(server);
	      SocketAddress socketAddress = new InetSocketAddress(inteAddress, port);
	   
	      // create a socket
	      socket = new Socket();
	   
	      // this method will block no more than timeout ms.
	      int timeoutInMs = 5*1000;   // 5 seconds
	      socket.connect(socketAddress, timeoutInMs);
	       
	      return socket;
	    } 
	    catch (SocketTimeoutException ste) {
	      System.err.println("Timed out waiting for the socket.");
	      ste.printStackTrace();
	      throw ste;
	    }
	  }
 
}