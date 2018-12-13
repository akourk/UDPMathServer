// Author       :   Alex Kourkoumelis
// Date         :   11/4/2018
// Title        :   UDPMathServer
// Description  :   Creates an application layer protocol that uses UDP as the
//              :   transport layer protocol for a server that receives simple
//              :   math operations, +, -, *, /, executes the operations, and
//              :   prints the result to the screen

import java.net.*;
import java.util.Scanner;

class UDPMathClient {
    private static final int Port = 1604;

    // main entry point for the application
    public static void main(String args[]) {
        try {
            boolean shouldQuit = false;
            while (!shouldQuit) {

                // print a menu with 4 math operations (add, subtract, multiple, divide) and "quit"
                System.out.println("Welcome to WolframBeta! Please choose one of our many options:");
                System.out.println("To ADD, enter 'A'");
                System.out.println("To SUBTRACT, enter 'S'");
                System.out.println("To MULTIPLY, enter 'M'");
                System.out.println("To DIVIDE, enter 'D'");
                System.out.println("To QUIT, enter 'Q'");

                // why not have a default number like 42?
                String mathMessage = "42";

                // making a scanner to accept user input
                Scanner scanner = new Scanner(System.in);
                String operation;
                operation = scanner.next();
                operation = operation.toUpperCase();

                // changes simple letters into operations
                // if "q" then exit this loop and guilt the user a little
                switch (operation) {
                    case "A": operation = "add";
                    break;
                    case "S": operation = "subtract";
                    break;
                    case "M": operation = "multiply";
                    break;
                    case "D": operation = "divide";
                    break;
                    case "Q": shouldQuit = true;
                    System.out.println("You're a quitter.");
                }

                // ask user for the two numbers to perform the math operation on
                if (!operation.equals("Q")) {
                    String n1;
                    String n2;
                    System.out.println("What numbers would you like to " + operation + "?");
                    n1 = scanner.next();
                    n2 = scanner.next();

                    // can't say "divideing"... that's not a word
                    if (operation == "divide") {
                        operation = "divid";
                    }
                    System.out.println(operation + "ing " + n1 + " and " + n2);

                    // send this bad boy over the in the format "Operation Num1 Num2"
                    mathMessage = operation + " " + n1 + " " + n2;
                }

                try {

                    // create datagram socket
                    InetAddress localIpAddress = InetAddress.getLocalHost();
                    DatagramSocket datagramSocket = new DatagramSocket();

                    // create a datagram packet going to localIpAddress and Port containing the mathProtocol string
                    DatagramPacket sendPacket = new DatagramPacket(mathMessage.getBytes(),mathMessage.length(),localIpAddress,Port);

                    // send the packet
                    datagramSocket.send(sendPacket);

                    // closing the socket
                    datagramSocket.close();

                } catch (Exception e) {
                    System.err.println("Failed to create socket and send packet: " + e.toString());
                    e.printStackTrace();
                    shouldQuit = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Math client error: " + e.toString());
        }
    }
}