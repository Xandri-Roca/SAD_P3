// Bernat Xandri Zaragoza & Ramon Roca Oliver


import java.lang.Thread;
import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import java.util.Map;


public class ServerConnection{
	private static final int SERVER_PORT = 6666;
	private static Lock lk = new ReentrantLock();
	private static HashMap<String, MySocket> users = new HashMap<>();
        

	public static void main(String[] args){//por algun motivo el compilador me obliga a declaralo asi?

		MyServerSocket msSocket = new MyServerSocket(SERVER_PORT);
		System.out.println("Initialized server. Waiting for users.");

		while(true){
			MySocket client = msSocket.accept(); 
			new Thread(){
				public void run(){
                                        client.write("[SERVER]> Text *EXIT* to leave the chat ");
					client.write("[SERVER]> Enter your nick for this chat: ");
					String nick = client.read();
					addUser(nick, client);

					String line;
					while((line = client.read()) != null){
						broadcast(line, nick);
						System.out.println(nick + " texted: "+ line);
                                                if(line.equals("EXIT")){
                                                    break;
                                                }
					}
					removeUser(nick);
					client.close();
				}
			}.start();
		}
	}


	public static void addUser(String user, MySocket socket){
		lk.lock();
		users.put(user, socket);
		System.out.println("New User <"+ user + "> joined the chat.");
		lk.unlock();
	}

	// Not Used
	public static void removeUser(String user){
		lk.lock();
		users.remove(user);
		System.out.println("<"+ user + "> left the chat.");
		lk.unlock();
	}


	public static void broadcast(String message, String nick){
		lk.lock();
                
		for(Map.Entry<String, MySocket> entry : users.entrySet()){
                    
			MySocket s = entry.getValue();
			if(!nick.equals(entry.getKey())){
				s.write(nick+"> "+message);
			}
		}
		lk.unlock();
	}
}