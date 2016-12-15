/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpservertest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dell
 */
public class AdafruitMotorHatDcMotors extends AdafruitMotorHatBase {

  // ### Fields ###
  // Motor Index Constants
  public static final int Motor1Index = 0;
  public static final int Motor2Index = 1;
  public static final int Motor3Index = 2;
  public static final int Motor4Index = 3;
 
  // Container Class
  protected class DcMotorConfiguration{
    //Fields
    private int MotorIndex;
    private byte PwmPinPower;
    private byte PwmPinDirection1;
    private byte PwmPinDirection2;
        
    // Constructor
    private DcMotorConfiguration(int pMotorIndex, byte pPwmPinPower, byte pPwmPinDirection1, byte pPwmPinDirection2){
      MotorIndex = pMotorIndex;
      PwmPinPower = pPwmPinPower;
      PwmPinDirection1 = pPwmPinDirection1;
      PwmPinDirection2 = pPwmPinDirection2;
    }
            
     // Read only accessors
    public byte PwmPinPower(){ return PwmPinPower; }
    public byte PwmPinDirection1(){ return PwmPinDirection1; }
    public byte PwmPinDirection2(){ return PwmPinDirection2; }
  }
 
  // Array of all 4 Motor Configurations
  private DcMotorConfiguration[] dcMotorConfigurations = {    
    new DcMotorConfiguration(Motor1Index, (byte)  8, (byte) 10, (byte)  9),
    new DcMotorConfiguration(Motor2Index, (byte) 13, (byte) 11, (byte) 12),
    new DcMotorConfiguration(Motor3Index, (byte)  2, (byte)  4, (byte)  3),
    new DcMotorConfiguration(Motor4Index, (byte)  7, (byte)  5, (byte)  6)
  };
  public enum EMotorCommands {Foreward, Backward, Release }
  
  
  // ### Constructor ###
  public AdafruitMotorHatDcMotors(int pAddrPca9685, double pPwmFrequncy) {
    // call base class constructor
    super(EMotorHatConfiguration.DcMotors, pAddrPca9685, pPwmFrequncy);
  }
  

  // ### Public Methods ###
  public void DriveMotor(EMotorCommands[] pMotorCommands, byte[] pMotorSpeeds){  
    // set direction for each motor
    for (int ij = 0; ij < pMotorCommands.length; ij++){      
      try {
        // Set motor direction befor applying speed set points
        SetMotorDirection(ij, pMotorCommands[ij]); 
        // Set motor speed
        SetMotorSpeed(ij, pMotorSpeeds[ij]);
      } catch (Exception e) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
      }
           
    }
  }
  
  
  // ### Private Methods ###
  
  // Set motor direction
  private void SetMotorDirection(int pMotorIndex, EMotorCommands pMotorCommand) throws IOException {
  // set direction
  synchronized(_lockObject){
      switch (pMotorCommand)
      {
        case Foreward:
          pca9685DriverInst.SetPwmPin( dcMotorConfigurations[pMotorIndex].PwmPinDirection2(), false);
          pca9685DriverInst.SetPwmPin( dcMotorConfigurations[pMotorIndex].PwmPinDirection1(), true);
          break;
        case Backward:          
          pca9685DriverInst.SetPwmPin( dcMotorConfigurations[pMotorIndex].PwmPinDirection1(), false);
          pca9685DriverInst.SetPwmPin( dcMotorConfigurations[pMotorIndex].PwmPinDirection2(), true);
          break;
        case Release:
          pca9685DriverInst.SetPwmPin( dcMotorConfigurations[pMotorIndex].PwmPinDirection2(), false);
          pca9685DriverInst.SetPwmPin( dcMotorConfigurations[pMotorIndex].PwmPinDirection1(), false);
          break;
      }
  }
  }
  
  // Set motor speed
  private void SetMotorSpeed(int ij, byte pMotorSpeed) throws IOException {
    synchronized(_lockObject){
    pca9685DriverInst.SetPwmPin(dcMotorConfigurations[ij].PwmPinPower(), 0, pMotorSpeed*16);
    }
  }
  
  
}
