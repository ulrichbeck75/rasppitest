/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpservertest;


/**
 *
 * @author Dell
 */
public abstract class AdafruitMotorHatBase {
  
  // Types 
  public enum EMotorHatConfiguration{DcMotors, StepperMotors}
 

  
   // ### Fields ###
  // Thread interlocking object
  protected final Object _lockObject = new Object();
  
  protected Pca9685Driver pca9685DriverInst;
  private EMotorHatConfiguration motorConfiguration;
    
  // ### Constructor ###
  protected AdafruitMotorHatBase(EMotorHatConfiguration pMotorConfiguration, int pAddrPca9685, double pPwmFrequncy)
  {
    // init driver for pwm chip
    pca9685DriverInst = new Pca9685Driver(pAddrPca9685, pPwmFrequncy);
    motorConfiguration = pMotorConfiguration;
    
  
  }
  
}
