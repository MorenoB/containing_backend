package org.nhl.containing_backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Main controller class.
 */
public class Controller {

    private Communication server;

    public Controller() {
        server = new Communication();
    }

    public void startServer() {
        server.Start();
    }

    public void prepareMessage() throws IOException {
        System.out.println("Please input a string to send to the simulator :");
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        server.sendMessage(inFromUser.readLine());
    }
}
