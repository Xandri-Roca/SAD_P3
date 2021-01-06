// Bernat Xandri Zaragoza & Ramon Roca Oliver


import java.net.ServerSocket;
import java.io.*;


public class MyServerSocket{

	private ServerSocket sSocket;


	public MyServerSocket(int serverPort){
	// We create the Server Socket with the designated port.
		try{
			this.sSocket = new ServerSocket(serverPort);
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	public MySocket accept(){
	// Listens for connections from host.
		try{
			return new MySocket(sSocket.accept());
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}


	public void close(){
		try{
			this.sSocket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}