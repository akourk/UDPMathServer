// Author       :   Alex Kourkoumelis
// Date         :   11/4/2018
// Title        :   UDPMathServer
// Description  :   Creates an application layer protocol that uses UDP as the
//              :   transport layer protocol for a server that receives simple
//              :   math operations, +, -, *, /, executes the operations, and
//              :   prints the result to the screen

import java.io.*;
import java.net.*;

class UDPMathServer implements Runnable {
    private static final int Port = 1604;
    private boolean running = false;

    // main entry point for the application
    public static void main(String args[]) {
        try {

            // startup
            UDPMathServer server = new UDPMathServer();
            Thread serverThread = new Thread(server);
            serverThread.start();

            // wait until finished
            System.out.println("Math server running, press enter to quit...");
            Console cons = System.console();
            String enterString = cons.readLine();

            // should actually quit when enter is pressed... maybe
            if(enterString == "\n"){
                server.running = false;
            }

            // shutdown gracefully. sort of.
            server.running = false;
            serverThread.interrupt();

        } catch (Exception e) {
            System.err.println("Math server error: " + e.toString());
        }
    }

    // constructor
    public UDPMathServer() {
        this.running = true;
    }

    // this is the server thread
    public void run() {

        // initializing this ish
        DatagramSocket serverSocket = null;

        // create the UDP server datagram socket
        try {
            // TODO: create datagram socket using the Port constant above
            serverSocket = new DatagramSocket(Port);

        } catch (Exception e) {
            System.err.println("Couldn't create datagram socket: " + e.toString());
        }

        // while running...
        while (this.running == true) {
            try {
                // create buffer and packet, 1024 bytes. Probably more than enough.
                DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);

                // receive packet via the datagram socket
                serverSocket.receive(receivePacket);

                // convert the buffer in the packet into string variable message
                String message = new String(receivePacket.getData());

                // pass this to the handleMathMessage method to do some number crunching
                double mathAnswer = this.handleMathMessage(message);
                System.out.println("The math answer to the message was : " + Double.toString(mathAnswer));
                Thread.yield();

            } catch (Exception e) {
                System.err.println("Failed to receive UDP packet, general exception: " + e.toString());
                this.running = false;
                break;
            }
        }
    }

    private double handleMathMessage(String message) {

        // break this thing up
        String[] nums = message.split(" ");
        String operator = nums[0];
        double num1 = Double.parseDouble(nums[1]);
        double num2 = Double.parseDouble(nums[2]);

        // doing these operations
        switch (operator) {
            case "add":
                return num1 + num2;
            case "subtract":
                return num1 - num2;
            case "multiply":
                return num1 * num2;
            case "divid":   // whatever
                return num1 / num2;
            default:
                return -1;
        }
    }
}