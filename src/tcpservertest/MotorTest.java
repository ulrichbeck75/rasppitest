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
public class MotorTest {

  // ### Fields ###
  public AdafruitMotorHatDcMotors Motors;
  private AdafruitMotorHatDcMotors.EMotorCommands[] motorCommands = {
    AdafruitMotorHatDcMotors.EMotorCommands.Release,
    AdafruitMotorHatDcMotors.EMotorCommands.Release,
    AdafruitMotorHatDcMotors.EMotorCommands.Release,
    AdafruitMotorHatDcMotors.EMotorCommands.Release
  };
  private byte[] motorSpeeds = {
    (byte) 0, (byte) 0, (byte) 0, (byte) 0
  };

  // ### Constructor ###
  public MotorTest() {
    Motors = new AdafruitMotorHatDcMotors(0x61, 1600.0);
  }

  // ### Public Methods ###
  // Drive motor for test
  public void MotorTest() {
    while (true) {
      for (int ij = 0; ij < 256; ij++) {
        motorCommands[AdafruitMotorHatDcMotors.Motor1Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor2Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor3Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor4Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;

        motorSpeeds[AdafruitMotorHatDcMotors.Motor1Index] = (byte) ij;
        motorSpeeds[AdafruitMotorHatDcMotors.Motor2Index] = (byte) ij;
        motorSpeeds[AdafruitMotorHatDcMotors.Motor3Index] = (byte) ij;
        motorSpeeds[AdafruitMotorHatDcMotors.Motor4Index] = (byte) ij;

        Motors.DriveMotor(motorCommands, motorSpeeds);

      }

      break;
    }

  }

}
