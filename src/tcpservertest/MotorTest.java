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
      for (int ij = 0; ij < 360; ij += 10) {
        /*  motorCommands[AdafruitMotorHatDcMotors.Motor1Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor2Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor3Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor4Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;

        motorSpeeds[AdafruitMotorHatDcMotors.Motor1Index] = (byte) ij;
        motorSpeeds[AdafruitMotorHatDcMotors.Motor2Index] = (byte) ij;
        motorSpeeds[AdafruitMotorHatDcMotors.Motor3Index] = (byte) ij;
        motorSpeeds[AdafruitMotorHatDcMotors.Motor4Index] = (byte) ij;

        Motors.DriveMotor(motorCommands, motorSpeeds);*/

        try {
          System.err.println("Current Angel: " + ij);
          MoveOnVector((double) ij, (byte) 50);

          Thread.sleep(5000);
        } catch (Exception e) {
        }

      }

      break;
    }

  }

  // Power motors for moving along the passed vector
  public void MoveOnVector(double pMoveAngel, byte pSpeedFactor) {
    // Limit SpeedFactor
    pSpeedFactor = (pSpeedFactor > 200) ? (byte) 200 : pSpeedFactor;

    // calulate amplifiers
    double moveAngleRad = Math.toRadians(pMoveAngel);
    double leftSide = (Math.sin(moveAngleRad) + Math.cos(moveAngleRad)) * pSpeedFactor;
    double rightSide = (Math.sin(moveAngleRad) - Math.cos(moveAngleRad)) * pSpeedFactor;

    // set left side wheel directions
    if (leftSide > 0) {
      motorCommands[AdafruitMotorHatDcMotors.Motor1Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
      motorCommands[AdafruitMotorHatDcMotors.Motor2Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
    } else {
      // move backward
      motorCommands[AdafruitMotorHatDcMotors.Motor1Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
      motorCommands[AdafruitMotorHatDcMotors.Motor2Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
    }

    // set right side wheel directions
    if (rightSide > 0) {
      motorCommands[AdafruitMotorHatDcMotors.Motor3Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
      motorCommands[AdafruitMotorHatDcMotors.Motor4Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
    } else {
      // move backward
      motorCommands[AdafruitMotorHatDcMotors.Motor3Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
      motorCommands[AdafruitMotorHatDcMotors.Motor4Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
    }

    // set wheel moving speeds
    motorSpeeds[AdafruitMotorHatDcMotors.Motor1Index] = (byte) Math.abs(leftSide);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor2Index] = (byte) Math.abs(leftSide);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor3Index] = (byte) Math.abs(rightSide);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor4Index] = (byte) Math.abs(rightSide);

    // Write to pwm chip for actual movement
    Motors.DriveMotor(motorCommands, motorSpeeds);

    System.out.println("m1/2: " + (byte) (leftSide) + "  m3/4: " + (byte) (rightSide));
  }
}
