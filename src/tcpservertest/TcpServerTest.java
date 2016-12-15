/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpservertest;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class TcpServerTest {

  private final static int BUFFER_SIZE = 1024;
  private final static Object _lockObject = new Object();
  private static MotorTest motorTest;
  
  public static void main(String argv[]) throws Exception {
//    String clientSentence;
//    String capitalizedSentence;
//    ServerSocket welcomeSocket = new ServerSocket(6799);
//
//    while (true) {
//      Socket connectionSocket = welcomeSocket.accept();
//      BufferedReader inFromClient
//              = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//      DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//      clientSentence = inFromClient.readLine();
//      System.out.println("Received: " + clientSentence);
//      GpioControl tmp = new GpioControl();
//      //tmp.GpioTest();
//      capitalizedSentence = clientSentence.toUpperCase() + '\n';
//      outToClient.writeBytes(capitalizedSentence);
//    }

    motorTest = new MotorTest();
    StartTcpServerThread();

    promptEnterKey();
  }

  private static void StartTcpServerThread() {
    System.out.println("Starting new TCP Server Thread: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    new Thread(new Runnable() {
      public void run() {
        try {
          
          while (true) {
            ServerSocket serverSocket = new ServerSocket(12345);
            Socket clientSocket = serverSocket.accept();
            byte[] inputBuffer = new byte[BUFFER_SIZE];
            byte[] rxBuffer = new byte[512];
            int read;
            int totalRead = 0;
                        
            InputStream clientInputStream = clientSocket.getInputStream();
            DataOutputStream clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            
            while ((read = clientInputStream.read(rxBuffer)) != 0) {
              System.arraycopy(rxBuffer, 0, inputBuffer, totalRead, rxBuffer.length);
              totalRead += read;
              
              synchronized(_lockObject){
                if ( (inputBuffer[totalRead-2] == 13) && (inputBuffer[totalRead-1] == 10) ) {                  
                  System.out.println(LocalDate.now().format(DateTimeFormatter.ISO_DATE)+": RX String:");
                  System.out.println(inputBuffer);
                  byte[] retFrame = ProcessFrame(inputBuffer);
                  motorTest.MotorTest();
                  clientOutputStream.write(retFrame);
                  System.out.println(LocalDate.now().format(DateTimeFormatter.ISO_DATE)+": TX String:");
                  System.out.println(retFrame);
                  totalRead = 0;
                }
              }
           
            }
            
          }
        } catch (IOException e) {
        }
      }
    }).start();
  }

  private static byte[] ProcessFrame(byte[] inputFrame) {
    byte[] retFrame = "NAK".getBytes();

    retFrame = inputFrame;

    return retFrame;
  }

  public static void promptEnterKey() {
    System.out.println("Press \"ENTER\" to continue...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
  }
}
