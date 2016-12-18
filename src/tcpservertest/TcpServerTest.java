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
  private static Boolean stopServer;
  private static Thread _serverThread;
  private static ServerSocket serverSocket;
  
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

    stopServer = false;
    motorTest = new MotorTest();
    _serverThread = StartTcpServerThread();

    while(true){
      if (!promptEnterKey()){
        stopServer = true;
        _serverThread.interrupt();
        break;
      }
    }
    
    System.out.println("Quitted");
    
  }

  private static Thread StartTcpServerThread() {
    System.out.println("Starting new TCP Server Thread: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    Thread ServerThread = new Thread(new Runnable() {
      public void run() {
        try {
          byte[] retFrame = ProcessFrame("Start".getBytes());
          while (!stopServer) {
            if (serverSocket==null){
            serverSocket = new ServerSocket(12345);
            }
            Socket clientSocket = serverSocket.accept();
            
            byte[] inputBuffer = new byte[BUFFER_SIZE];
            byte[] rxBuffer = new byte[512];
            int read;
            int totalRead = 0;
                        
            InputStream clientInputStream = clientSocket.getInputStream();
            DataOutputStream clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            
            while ((read = clientInputStream.read(rxBuffer)) != 0) {
              if (read < 0){break;}
              
              System.arraycopy(rxBuffer, 0, inputBuffer, totalRead, rxBuffer.length);
              totalRead += read;
              if (totalRead > 3){              
                synchronized(_lockObject){
                  if ( (inputBuffer[totalRead-2] == 13) && (inputBuffer[totalRead-1] == 10) ) {                  
                    System.out.println(LocalDate.now().format(DateTimeFormatter.ISO_DATE)+": RX String: " + inputBuffer.toString());

                    MotorTest.EBasicMoveDirections tmpMoveDir = MotorTest.EBasicMoveDirections.Stop;

                    switch(inputBuffer[0]-48){
                      case 5: tmpMoveDir = MotorTest.EBasicMoveDirections.Stop; break;                   
                      case 8: tmpMoveDir = MotorTest.EBasicMoveDirections.MoveForward; break;
                      case 2: tmpMoveDir = MotorTest.EBasicMoveDirections.MoveBackward; break;
                      case 4: tmpMoveDir = MotorTest.EBasicMoveDirections.MoveCcw; break;
                      case 6: tmpMoveDir = MotorTest.EBasicMoveDirections.MoveCw; break;
                      default:tmpMoveDir = MotorTest.EBasicMoveDirections.Stop; break; 
                    }
                    retFrame = ProcessFrame(inputBuffer, tmpMoveDir);
                    motorTest.MoveBasic(tmpMoveDir, inputBuffer[1]);
                    totalRead = 0;
                  }
                }
              }
              else{
              retFrame = ProcessFrame(inputBuffer);
              }
               
                    clientOutputStream.write(retFrame);
                    System.out.println(LocalDate.now().format(DateTimeFormatter.ISO_DATE)+": TX String: " + retFrame.toString());
                    
            }            
          }
        } catch (IOException e) {
          System.err.println(e.getMessage());
        }
      }
    });
    
    ServerThread.start();
    
    return ServerThread;
  }

  
   private static byte[] ProcessFrame(byte[] inputFrame ) {
   

    return inputFrame;
  }
  private static byte[] ProcessFrame(byte[] inputFrame, MotorTest.EBasicMoveDirections pBasicMoveDirection ) {
    byte[] retFrame = pBasicMoveDirection.toString().getBytes();

    //retFrame = inputFrame;

    return retFrame;
  }

  public static Boolean promptEnterKey() {
    System.out.println("Press \"ENTER\" to stop Server...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
    
    if (scanner.hasNextInt()){
      double angle = (double)scanner.nextInt();
      motorTest.MoveOnVector(angle, (byte) 150);
      System.out.println("New Angle: " + angle);
      return true;
    }
      
    System.out.println("No Int -> quitting");
    return false;
    
  }
}
