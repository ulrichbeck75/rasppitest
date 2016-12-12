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

  final static int BUFFER_SIZE = 65536;

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
              
              if ( (inputBuffer[totalRead-2] == 0x13) && (inputBuffer[totalRead-1] == 0x10) ) { 
                break;
              }
            }
            byte[] retFrame = ProcessFrame(inputBuffer);

            clientOutputStream.write(retFrame);
            //System.out.println(totalRead + " bytes read in " + (endTime - startTime) + " ms.");
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
