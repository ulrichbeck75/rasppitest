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
public class AdafruitMotorHatStepperMotors extends AdafruitMotorHatBase
{
  // Fields
  public enum EMotorCommands { Foreward, Backward, Brake, Release }
  public enum EStepperMode { SingleStep, DoubleStep, Interleave, MicroStep }
   // ### Fields ###
  // Motor Index Constants
  public final int Motor1Index = 0;
  public final int Motor2Index = 1;
 
 
  // Container Class
  protected class StepperMotorConfiguration{
    //Fields
    private int MotorIndex;
    private byte PwmPinPowerA;
    private byte PwmPinDirection1A;
    private byte PwmPinDirection2A;
    private byte PwmPinPowerB;
    private byte PwmPinDirection1B;
    private byte PwmPinDirection2B;
        
    // Constructor
    private StepperMotorConfiguration(int pMotorIndex
            , byte pPwmPinPowerA, byte pPwmPinDirection1A, byte pPwmPinDirection2A
            , byte pPwmPinPowerB, byte pPwmPinDirection1B, byte pPwmPinDirection2B){
      MotorIndex = pMotorIndex;
      PwmPinPowerA = pPwmPinPowerA;
      PwmPinDirection1A = pPwmPinDirection1A;
      PwmPinDirection2A = pPwmPinDirection2A;
      PwmPinPowerB = pPwmPinPowerB;
      PwmPinDirection1B = pPwmPinDirection1B;
      PwmPinDirection2B = pPwmPinDirection2B;
    }
            
     // Read only accessors
    public byte PwmPinPowerA(){ return PwmPinPowerA; }
    public byte PwmPinDirection1A(){ return PwmPinDirection1A; }
    public byte PwmPinDirection2A(){ return PwmPinDirection2A; }    
    public byte PwmPinPowerB(){ return PwmPinPowerB; }
    public byte PwmPinDirection1B(){ return PwmPinDirection1B; }
    public byte PwmPinDirection2B(){ return PwmPinDirection2B; }
  }
 
  // Array of all 4 Motor Configurations
  private StepperMotorConfiguration[] dcMotorConfigurations = {    
    new StepperMotorConfiguration(Motor1Index, (byte) 8, (byte) 10, (byte) 9, (byte) 13, (byte) 11, (byte) 12),
    new StepperMotorConfiguration(Motor2Index, (byte) 2, (byte) 4, (byte) 3, (byte) 7, (byte) 5, (byte) 6)
  };
  
  
  
  // ### Constructor ###
  public AdafruitMotorHatStepperMotors(int pAddrPca9685, double pPwmFrequncy) {
    // call base class constructor
    super(EMotorHatConfiguration.DcMotors, pAddrPca9685, pPwmFrequncy);
  }
}
