package engine.network;

import java.net.*;
import java.nio.ByteBuffer;

import engine.Global;
import engine.Scheduler;
import engine.entity.Player;
import main.Main;

import java.io.*;

public class Network {
    public static final String SERVER_IP = "192.168.1.14";
    public static final String SERVER_IP2 = "71.80.151.161";
    public static final int SERVER_PORT = 500;
    private static final int BUFFER_SIZE = 16 * 1024; // 16 KB buffer size
    private static Socket socket;
    private static InputStream input;
    private static OutputStream output;

    private static int status = 0;
    private static boolean playerListenerRunning = true;

    public static void start() {
        if(Global.development){
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                System.out.println("Connected to server at " + SERVER_IP + ":" + SERVER_PORT);
                input = socket.getInputStream();
                output = socket.getOutputStream();
                new Thread(Network::listenForMessages).start();
                startPlayerListener();
            } catch (IOException e){
                System.err.println("Failed to connect to server: " + e.getMessage());
                Main.closeWindow = true;
                Main.cleanup();
            }
        } else {
            try {
            
                socket = new Socket(SERVER_IP2, SERVER_PORT);
                System.out.println("Connected to server at " + SERVER_IP2 + ":" + SERVER_PORT);
                input = socket.getInputStream();
                output = socket.getOutputStream();
                new Thread(Network::listenForMessages).start();
                startPlayerListener();
            } catch (IOException e) {
                System.err.println("Failed to connect to server: " + e.getMessage());
                
            }
        }
    }

    public static void stop() {
        status = 0;
        try {
            if (socket != null) {
                socket.close();
                System.out.println("Connection closed.");
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    private static void listenForMessages() {
        byte[] buffer = new byte[BUFFER_SIZE]; // 16KB buffer
        int bytesRead;
    
        try {
            // Continuously listen for messages from the server
            while ((bytesRead = input.read(buffer)) != -1) {
                // System.out.println("Received " + bytesRead + " bytes from server:");
                
                // for(int i = 0; i<bytesRead;i++){
                //     System.out.print((buffer[i] & 0xFF) + " ");
                // }
                // System.out.println();

                ByteBuffer wrapped = ByteBuffer.wrap(buffer, 0, bytesRead);
                
                byte packetType = wrapped.get(); // 1 byte for packet type

                if (status == 0) {
                    // Handle different packet types
                    if (packetType == ConnectionTypes.Accepted) {
                        Global.whoAmI = wrapped.getInt();
                        Global.player.id = Global.whoAmI;  // Make sure the player's ID is set
                        status = 1;
                        sendPlayerJoinData();  // Send join data after getting correct whoAmI
                        // startPlayerListener();
                        Scheduler.start();
                    }
                } else if(status == 1){
                    // System.out.println("packetType: " + packetType);
                    if(packetType == GameplayPacket.PlayerJoined){
                        PlayerJoinPacket PJP = (PlayerJoinPacket) deserializeData(buffer);
                        if(PJP==null){
                            System.err.println("Attempted to let player join but couldnt render their id usable (NULL JOIN PACKET)");
                            continue;
                        }
                        System.out.println(PJP.name+" has joined the server with id "+PJP.id);
                        if(PJP.id==Global.whoAmI) continue;
                        Global.players.add(PJP.get());
                    }
                    if(packetType == GameplayPacket.PlayerUpdate){
                        PlayerUpdatePacket update = (PlayerUpdatePacket) deserializeData(buffer);
                        for (Player player : Global.players) {
                            if(player == null || update == null){
                                continue;
                            }
                            if(player.id == update.id){
                                update.set(player);
                            }
                        }
                    }
                    if(packetType == GameplayPacket.PlayerLeft){
                        int pid = wrapped.getInt();
                        System.out.println(pid + " Requested removal");
                        for (int i = 0; i < Global.players.size(); i++) {
                            System.out.println((i+1)+"/"+Global.players.size());
                            if(Global.players.get(i).id == pid){
                                Global.players.remove(i);
                                System.out.println("Removed " + pid);
                            }
                        }
                    }
                    if(packetType == GameplayPacket.GetPlayers){
                        int iterator = wrapped.getInt();
                        int[] array = new int[iterator];
                        for(int i = 0; i < iterator; i++){
                            array[i] = wrapped.getInt();
                        }
                        PlayerManager.filterPlayers(Global.players, array);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from server: " + e.getMessage());
        }
    }

    public static void runOnMainThread(Runnable task) {
        Global.mainThreadTaskQueue.add(task);
        System.out.println("Task added. Queue size is now: " + Global.mainThreadTaskQueue.size());
    }

    public static void sendMessage(byte[] buffer){
        synchronized (buffer) {
            try {
                output.write(buffer);
                output.flush();
                // System.out.println("sent "+buffer.length+" bytes to the server.");
            } catch (IOException e) {
                playerListenerRunning = false;
                e.printStackTrace();
                Main.cleanup();
            }
        }
    }

    public static Object deserializeData(byte[] buffer) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
    
            // Read packet type (1 byte)
            /* byte packetType = */ byteBuffer.get();
    
            // Read 'whoAmI' (4 bytes)
            /* int whoAmI = */ byteBuffer.getInt();
    
            // Read the length of the serialized object (4 bytes)
            int dataLength = byteBuffer.getInt();
    
            // Validate the length to ensure it's not absurdly large
            if (dataLength < 0 || dataLength > 1024*16) {  // Assuming max object size is 8192 bytes
                System.err.println("Invalid data length: " + dataLength);
                return null;
            }
    
            // Check if there are enough bytes in the buffer
            if (byteBuffer.remaining() < dataLength) {
                System.err.println("Data length mismatch: Expected " + dataLength + " bytes, but only " + byteBuffer.remaining() + " bytes available.");
                return null;
            }
    
            // Read the serialized object
            byte[] objectBytes = new byte[dataLength];
            byteBuffer.get(objectBytes);
    
            // Deserialize the object
            ByteArrayInputStream bais = new ByteArrayInputStream(objectBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
    
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to deserialize data: " + e.getMessage());
        }
        return null;
    }
    
    
    public static byte[] serializeData(Object object, int packetType) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
    
            // Serialize the object
            oos.writeObject(object);
            oos.flush();
            byte[] bytes = baos.toByteArray(); // Get the serialized data
    
            // Create a ByteBuffer to hold the packet
            ByteBuffer buffer = ByteBuffer.allocate(1 + 4 + 4 + bytes.length); // 1 byte for packet type, 4 bytes for 'whoAmI', 4 bytes for length, serialized data
            buffer.put((byte) packetType);      // Packet type
            buffer.putInt(Global.whoAmI);       // Player identifier
            buffer.putInt(bytes.length);        // Length of serialized data
            buffer.put(bytes);                  // Serialized data
    
            return buffer.array();              // Return the final byte array
        } catch (IOException e) {
            System.err.println("Failed to serialize object: " + e.getMessage());
        }
        return new byte[]{};  // Return empty array on failure
    }

    public static void connect() {
        sendMessage(new byte[]{1});
    }

    public static void getPlayersIdList(){
        sendMessage(new byte[]{13});
    }

    public static void startPlayerListener(){
        new Thread(Network::playerListener).start();
    }

    public static void playerListener(){
        while (playerListenerRunning){
            try {
                Thread.sleep(5000);
                getPlayersIdList();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendPlayerJoinData() {
        sendMessage(serializeData(new PlayerJoinPacket(Global.player), GameplayPacket.PlayerJoined));
    }

    public static void sendPlayerPosition() {
        sendMessage(serializeData(new PlayerUpdatePacket(Global.player), GameplayPacket.PlayerUpdate));
    }
    

}
