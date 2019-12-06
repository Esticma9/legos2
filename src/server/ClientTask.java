/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uidk2372
 */
public class ClientTask implements Runnable {
    private final AsynchronousSocketChannel clientSocket;
    public final LinkedBlockingQueue<String> queue;

    ClientTask(AsynchronousSocketChannel clientSocket, LinkedBlockingQueue<String> queue) {
        this.clientSocket = clientSocket;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            if ((clientSocket != null) && (clientSocket.isOpen())) {
                Logger.getLogger(Server.class.getName()).log(Level.INFO, "Connection with client: {0} started", clientSocket.getRemoteAddress());
                int invalidMessageCounter = 0;
                while (true && (clientSocket.isOpen())) {
                    ByteBuffer buffer = ByteBuffer.allocate(32);
                    Future<Integer> readResult  = clientSocket.read(buffer);
                    readResult.get();

                    String readMessage = new String(buffer.array());
                    String response;
                    Future<Integer> writeResult;

                    if(readMessage.isEmpty()){
                        Logger.getLogger(Server.class.getName()).log(Level.FINE, "EmptyMessage");
                        buffer.clear();
                    }
                    else if(readMessage.startsWith("EndConnection")){
                        response = "Connection with client: " + clientSocket.getRemoteAddress() + " terminated\n";
                        Logger.getLogger(Server.class.getName()).log(Level.INFO, response);
                        queue.put("EndProgram");
                        writeResult = clientSocket.write(ByteBuffer.wrap(response.getBytes())); 
                        writeResult.get();
                        buffer.clear();
                        break;
                    }
                    else if(readMessage.startsWith("M:")) {
                        //Get Message variable
                        String message = readMessage.split(":")[1].trim();

                        response = "Message Received: " + message + "\n";
                        Logger.getLogger(Server.class.getName()).log(Level.INFO, response);
                        queue.put(message);
                        writeResult = clientSocket.write(ByteBuffer.wrap("rec".getBytes())); 
                        writeResult.get();
                        buffer.clear();
                    }
                    else {
                        Logger.getLogger(Server.class.getName()).log(Level.WARNING, "Message Not Recognized:{0}", readMessage);
                        if(invalidMessageCounter>9){
                            break;
                        }
                        else{
                            invalidMessageCounter++;
                        }
                    }
                } 
            }
        } catch (InterruptedException | ExecutionException | IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
