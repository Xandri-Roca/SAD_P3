// Bernat Xandri Zaragoza & Ramon Roca Oliver


import java.lang.Thread;
import java.io.*;


public class ClientConnection{

	public static final String SERVER_HOST = "localhost";
	public static final int SERVER_PORT = 6666;
         
	//public static void main(String hostAddress, int hostPort, String nick){
	public static void main(String[] args){//por algun motivo el compilador me obliga a declaralo asi?
		//MySocket mSocket = new MySocket(hostAddress, hostPort);
                
		MySocket mSocket = new MySocket(SERVER_HOST, SERVER_PORT);
  
		SwingClient swClient = new SwingClient("NewClient", mSocket);
		swClient.createAndShowGUI("NewClient");


		// Output Thread
		new Thread(){
			public void run(){
				String serverMessage;
				//while (hi ha línia del servidor)
    				//escriure línia per pantalla;
				while((serverMessage = mSocket.read()) != null){
					 swClient.addMessage(serverMessage); 
				}
			}
		}.start();
	}
}


// Input Thread
//while ((línia = in.readLine()) != null)
//    escriure línia per socket;

// Output Thread
//while (hi ha línia del servidor)
//    escriure línia per pantalla;