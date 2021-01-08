// Bernat Xandri Zaragoza & Ramon Roca Oliver


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;

public class ServerNIO{

    private static Selector selector = null;
    private static HashMap<SelectionKey,String> map = new HashMap<>();

    public static void main(String[] args) {
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 6666));
            serverSocketChannel.configureBlocking(false);
            int validops = serverSocketChannel.validOps();
            serverSocketChannel.register(selector, validops, null);
            
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> i = selectedKeys.iterator();
                while (i.hasNext()) {
                    SelectionKey key = i.next();
                    if (key.isAcceptable()) {                  
                        newUserAdded_NIO(serverSocketChannel);
                    }else if (key.isReadable()) {
                        messageNIO(key);
                    }
                    i.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// This part is in charge of adding and accepting the new client connection
    private static void newUserAdded_NIO(ServerSocketChannel mySocket) throws IOException {
        System.out.println("New User joined the chat.");
        SocketChannel client = mySocket.accept();
        client.configureBlocking(false);                                    
        int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        client.register(selector, interestSet, buffer);
    }

// This method establishes the relationships between the graphical viewer of the connected client 
//and the messages received on the server.
    private static void messageNIO(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        int bytes_read = client.read(buffer);
        buffer.flip();
        if (bytes_read > 0) {
            byte[] bytes = new byte[bytes_read];    int i = 0;
            while(buffer.hasRemaining()){
                bytes[i] = (byte) buffer.get(); 
                i++;
            }
            String data = new String(bytes);

            System.out.print(data);
            broadcast(data, key);
        }
    }

    private static void broadcast(String data, SelectionKey senderKey){
        ByteBuffer messageByteBuffer =ByteBuffer.wrap(data.getBytes());
        for(SelectionKey key : selector.keys()) {
            try{
                if(key.isWritable() && (key.channel() instanceof SocketChannel) && !key.equals(senderKey)) {
                    SocketChannel sChannel=(SocketChannel) key.channel();
                    sChannel.write(messageByteBuffer);
                    messageByteBuffer.rewind();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }   
        }
    }

   
}