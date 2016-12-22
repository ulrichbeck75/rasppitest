/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpservertest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
        import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
        import org.eclipse.paho.client.mqttv3.MqttException;
        import org.eclipse.paho.client.mqttv3.MqttMessage;
        import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Dell
 */
public class mqttTest { 

  // ### Fields ###
  public static final String topic        = "/hello/t01";
  public static final int qos             = 2;
  public static final String broker       = "tcp://127.0.0.1:1883";
  public static final String clientId     = "JavaSample01";
  public static final MemoryPersistence persistence = new MemoryPersistence();
 public  MqttClient sampleClient;
 public Thread PublishThread;

  // ### Constructor ###
  public mqttTest(){
    
    try {
      sampleClient = new MqttClient(broker, clientId, persistence);
      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      System.out.println("Connecting to broker: "+broker);
      sampleClient.connect(connOpts);
      System.out.println("Connected");
      PublishThread = StartPublishThread();
      
    } catch (MqttException ex) {
      Logger.getLogger(mqttTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }       
      
  
  private Thread StartPublishThread() {
    System.out.println("Starting new MQTT publish Thread: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    Thread ServerThread = new Thread(new Runnable() {
      public void run() {
        try {
         
          while (true) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


            String msg = sdf.format(new Date());
            System.out.println("publishing new MQTT message: " + msg);
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            Thread.sleep(100);                 
          }
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      }
    });
    
    ServerThread.start();
    
    return ServerThread;
  }




  




}
