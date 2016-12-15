/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpservertest;


import java.io.IOException;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Dell
 */
public class Pca9685Driver {
  
  
  
  // Properties
  private I2CBus _i2cBusAccess;
  private I2CDevice _i2cPca9685Device;
  
  // Registers/etc.
  public static final int MODE1 = (int)0x00;
  public static final int MODE2 = (int)0x01;
  public static final int SUBADR1 = (int)0x02;
  public static final int SUBADR2 = (int)0x03;
  public static final int SUBADR3  = (int)0x04;
  public static final int PRESCALE = (int)0xFE;
  public static final int LED0_ON_L = (int)0x06;
  public static final int LED0_ON_H = (int)0x07;
  public static final int LED0_OFF_L = (int)0x08;
  public static final int LED0_OFF_H = (int)0x09;
  public static final int ALL_LED_ON_L = (int)0xFA;
  public static final int ALL_LED_ON_H = (int)0xFB;
  public static final int ALL_LED_OFF_L = (int)0xFC;
  public static final int ALL_LED_OFF_H = (int)0xFD;

  // Bits
  public static final byte RESTART = (byte)0x80;
  public static final byte SLEEP = (byte)0x10;
  public static final byte ALLCALL = (byte)0x01;
  public static final byte INVRT = (byte)0x10;
  public static final byte OUTDRV = (byte)0x04;
 
  
  /*
  *  Constructor
  */
  public Pca9685Driver(int pI2cAddr, double pPwmFrequency)
  {
    try {
      // Init/Set up pwm chip 
      Init(pI2cAddr, pPwmFrequency);
      
    } catch (Exception e) {
      Logger.getLogger(Pca9685Driver.class.getName()).log(Level.SEVERE, null, e);
    }
  }
  
  
  // ### Public methods ###
  
  // Set single output pin on pwm chip
  public void SetPwmPin(byte pPwmPin, int on, int off) throws IOException
  {     
    _i2cPca9685Device.write(LED0_ON_L + (4 * pPwmPin), (byte)(on & 0xFF));
    _i2cPca9685Device.write(LED0_ON_H + (4 * pPwmPin), (byte)(on >> 8));
    _i2cPca9685Device.write(LED0_OFF_L + (4 * pPwmPin), (byte)(off & 0xFF));
    _i2cPca9685Device.write(LED0_OFF_H + (4 * pPwmPin), (byte)(off >> 8));
  }
  
  // Set single output pin on pwm chip
  public void SetPwmPin(byte pPwmPin, Boolean pSetToHigh) throws IOException
  {     
    if (pSetToHigh){
      // Set output pin to 1, max. PWM Signal
      _i2cPca9685Device.write(LED0_ON_L + (4 * pPwmPin), (byte)(0x0));
      _i2cPca9685Device.write(LED0_ON_H + (4 * pPwmPin), (byte)(0x0));
      _i2cPca9685Device.write(LED0_OFF_L + (4 * pPwmPin), (byte)(4096 & 0xFF));
      _i2cPca9685Device.write(LED0_OFF_H + (4 * pPwmPin), (byte)(4096 >> 8));
    }
    else{
      // Set output pin to 0, no PWM Signal
      _i2cPca9685Device.write(LED0_ON_L + (4 * pPwmPin), (byte)(4096 & 0xFF));
      _i2cPca9685Device.write(LED0_ON_H + (4 * pPwmPin), (byte)(4096 >> 8));
      _i2cPca9685Device.write(LED0_OFF_L + (4 * pPwmPin), (byte)(0x0));
      _i2cPca9685Device.write(LED0_OFF_H + (4 * pPwmPin), (byte)(0x0));
    }
    
  }
  
  // Set all outputs on pwm chip
  public void SetAllPwmPins(int on, int off) throws IOException
  {     
    _i2cPca9685Device.write(ALL_LED_ON_L, (byte)(on & 0xFF));
    _i2cPca9685Device.write(ALL_LED_ON_H, (byte)(on >> 8));
    _i2cPca9685Device.write(ALL_LED_OFF_L, (byte)(off & 0xFF));
    _i2cPca9685Device.write(ALL_LED_OFF_H, (byte)(off >> 8));
  }
  
  
  
  // ### Private Methods ###
  
  // run initialization for pwm chip
  private void Init(int pI2cAddr, double pPwmFrequency) throws IOException, UnsupportedBusNumberException, InterruptedException
  {
    // get the I2C bus driver and chip to communicate on
    _i2cBusAccess = I2CFactory.getInstance(I2CBus.BUS_1);
    _i2cPca9685Device = _i2cBusAccess.getDevice(pI2cAddr);
    
     // set all pwm output pins to 0
    SetAllPwmPins((byte)0, (byte)0);
    
    // configure "totem pole" structure configuration on 
    _i2cPca9685Device.write(MODE2, OUTDRV);
    // Enable AllCall on pwm chip
    _i2cPca9685Device.write(MODE1, ALLCALL);    
    // Wait for oszillator
    Thread.sleep(15); 
    
    // read mode1 register
    int readResult =_i2cPca9685Device.read(MODE1);    
    if (readResult < 0) {
      throw new IOException("Failed reading from I2C Bus/Device");
    }
    // take chip to active mode
    byte ctlCode = (byte)(readResult & ~SLEEP);    
    _i2cPca9685Device.write(MODE1, ctlCode);
     // Wait for oszillator
    Thread.sleep(15); 
    
   
    // Set PWM Frequency
    SetPwmFrequency(pPwmFrequency);
    
  }
  // set PWM Frequency
  private void SetPwmFrequency(double pPwmFrequency) throws IOException, InterruptedException
  {        
    // read mode1 register
    int readResultRegisterMode1 =_i2cPca9685Device.read(MODE1);    
    if (readResultRegisterMode1 < 0) {
      throw new IOException("Failed reading from I2C Bus/Device");
    }
    // set chip to sleep mode for applying new prescale 
    byte ctlCodeSetSleepMode = (byte)(readResultRegisterMode1 | SLEEP);    
    _i2cPca9685Device.write(MODE1, ctlCodeSetSleepMode);
     // Wait for oszillator
    Thread.sleep(15); 
    // set chip pwm frequency by equation provided:
    // prescale = round.base(osc_clock/4096*updateRate) -1
    double preScale = 25000000.0 /4096.0 / pPwmFrequency;
    byte preScaleFactor =(byte)( Math.floor(preScale) -1);
    // apply new prescale value
    _i2cPca9685Device.write(PRESCALE, preScaleFactor);
    
    // restore original values in Mode1 control register
    _i2cPca9685Device.write(MODE1, (byte)(readResultRegisterMode1));
    
    // Wait for oszillator
    Thread.sleep(15); 
    
    // restart chip with original values in Mode1 control register
    byte restartChip = (byte)( (byte)(readResultRegisterMode1) | RESTART );    
    _i2cPca9685Device.write(MODE1, restartChip);
  
  }
  
}
