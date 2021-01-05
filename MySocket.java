// Bernat Xandri Zaragoza & Ramon Roca Oliver


import java.net.Socket;
import java.io.*;
import java.net.SocketException;


public class MySocket{

	private Socket socket;
	private BufferedReader bReader; // Will be used to recieve messages.
	private PrintWriter pWriter; // Will be used to send messages.


	public MySocket(String hostAddress, int hostPort){
	// Here we create the socket with the address and port specified.
		try{
			this.socket = new Socket(hostAddress, hostPort);
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// We need to set the autoflush to True for the print method.
			pWriter = new PrintWriter(socket.getOutputStream(), true);
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	public MySocket(Socket soc){
		try{
			this.socket = soc;
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pWriter = new PrintWriter(socket.getOutputStream(), true);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void write(String text){
	// basic type writing method.
		pWriter.println(text);
	}


	public String read(){
	// basic type readding method.

	// In case readLine returns some data, this method will return the data. If an exception
	// is thrown, we will return an empty String (null).
		String data = null;
		try{
			data = bReader.readLine();
		}catch(IOException e){
			e.printStackTrace();
		}
		return data;
	}


	public void close(){
	// This Method will close the socket and its different objects when it must be closed
		try{
			socket.close();
			bReader.close();
			pWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}