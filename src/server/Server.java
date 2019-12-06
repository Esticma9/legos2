/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uidk2372
 */
public class Server extends Thread {
    
    private static LinkedBlockingQueue<String> consumerQueue;
    
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
        consumerQueue = new LinkedBlockingQueue<>();
        
        while(true){
            try {
                String actMessage = consumerQueue.take();
                if(actMessage.equals("EndProgram")) {
                	System.exit(0);
                }
                System.out.printf("Main Thread - Last Message: %s\r\n", actMessage);
                BotActions.performAction(actMessage);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void startServer() {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(5);

        Runnable serverTask = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
	            try {
	                AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open();
	                serverSocket.bind(new InetSocketAddress("10.0.1.1", 8888));
	                Logger.getLogger(Server.class.getName()).log(Level.INFO, "Waiting for clients to connect");
	                System.out.println("Waiting for clients to connect...");
	                while (true) {
	                    Future<AsynchronousSocketChannel> acceptFuture = serverSocket.accept();
	                    AsynchronousSocketChannel worker = acceptFuture.get();
	                    clientProcessingPool.submit(new ClientTask(worker, consumerQueue));
	                }
	            } catch (IOException e) {
	                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Unable to process client request");
	                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
	            } catch (InterruptedException | ExecutionException ex) {
	                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
	            }
			}
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }
}


