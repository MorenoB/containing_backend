package org.nhl.containing_backend.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server.
 */
public class Server implements Runnable {
    private final int portNumber = 6666;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private ListenRunnable listenRunnable;
    private SendRunnable sendRunnable;

    private boolean running;

    public Server() {

    }

    @Override
    public void run() {
        try {
            // Open up the socket.
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Waiting for client");

            // Wait for client.
            clientSocket = serverSocket.accept();
            System.out.println("Connected to " + clientSocket.toString());

            listenRunnable = new ListenRunnable(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
            sendRunnable = new SendRunnable(new PrintWriter(clientSocket.getOutputStream(), true));

            Thread listenThread = new Thread(listenRunnable);
            Thread sendThread = new Thread(sendRunnable);

            listenThread.start();
            sendThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        running = true;

        while (running) {
            try {
                // Do nothing.
                Thread.sleep(1000);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            // In case the client shut down the listener, shut down everything.
            if (!listenRunnable.isRunning()) {
                this.stop();
            }
        }
    }

    /**
     * Shuts down the listeners and itself.
     */
    public void stop() {
        System.out.println("Shutting down server");
        try {
            listenRunnable.stop();
        } catch (Throwable e) {
        }
        try {
            sendRunnable.stop();
        } catch (Throwable e) {
        }
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the latest message from the listener.
     *
     * @return XML message.
     */
    public String getMessage() {
        return listenRunnable.getMessage();
    }

    /**
     * Writes a message to the sender buffer.
     *
     * @param message XML message.
     */
    public void writeMessage(String message) {
        sendRunnable.writeMessage("<Controller>" + message + "</Controller>");
    }

    public boolean isRunning() {
        return running;
    }
}
