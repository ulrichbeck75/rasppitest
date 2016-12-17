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
  public enum EBasicMoveDirections {Stop, MoveForward, MoveBackward, MoveCw, MoveCcw}
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
    Motors = new AdafruitMotorHatDcMotors(0x61, 1500.0);
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
          MoveOnVector((double) ij, (byte) 150);

          Thread.sleep(5000);
        } catch (Exception e) {
        }

      }

      break;
    }

  }

  
  /*
  Logik funktioniert, problematisch sind die Motoren im Dagu da diese eine rel. hohe Grundspannung brauchen
  daher diese logik auf anderer Platform mit besseren Motoren anwenden.
  Für Dagu bleibt nur, auf Grundbewegungen mit vor, zurück und drehen auszuweichen
  */
  // Power motors for moving along the passed vector
  public void MoveOnVector(double pMoveAngel, double pSpeedFactor) {
    // Limit SpeedFactor
    //pSpeedFactor = (pSpeedFactor > 170) ? (byte) 170 : pSpeedFactor;

    // calulate amplifiers
    double moveAngleRad = Math.toRadians(pMoveAngel);
    int leftSide  = (int)( (Math.sin(moveAngleRad) + Math.cos(moveAngleRad)) * pSpeedFactor);
    int rightSide = (int)( (Math.sin(moveAngleRad) - Math.cos(moveAngleRad)) * pSpeedFactor);

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

    // Werte normieren
    leftSide = Math.abs(leftSide);
    leftSide = (leftSide > 255)? 255 : leftSide;
    rightSide = Math.abs(rightSide);
    rightSide = (rightSide > 255)? 255 : rightSide;
    
    
    // set wheel moving speeds
    byte tmp = (byte) Math.abs(leftSide);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor1Index] = (byte) (leftSide & 0xFF);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor2Index] = (byte) (leftSide & 0xFF);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor3Index] = (byte) (rightSide & 0xFF);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor4Index] = (byte) (rightSide & 0xFF);

    // Write to pwm chip for actual movement
    Motors.DriveMotor(motorCommands, motorSpeeds);

    System.out.println("m1/2: " + (byte) (leftSide) + "  m3/4: " + (byte) (rightSide));
  }
  
  
  // Basic vehincle movement
  public void MoveBasic(EBasicMoveDirections pBasicMoveDirections, int pSpeed){
     // Werte normieren
    pSpeed = Math.abs(pSpeed);
    pSpeed = (pSpeed > 255)? 255 : pSpeed;

    // Set motor directions for requested movement
    System.out.println("Setting movement direction to " + pBasicMoveDirections);
    switch(pBasicMoveDirections){
      case MoveForward: 
        motorCommands[AdafruitMotorHatDcMotors.Motor1Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor2Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor3Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor4Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        break; 
      case MoveBackward: 
        motorCommands[AdafruitMotorHatDcMotors.Motor1Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
        motorCommands[AdafruitMotorHatDcMotors.Motor2Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
        motorCommands[AdafruitMotorHatDcMotors.Motor3Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
        motorCommands[AdafruitMotorHatDcMotors.Motor4Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
        break;
      case MoveCw: 
        motorCommands[AdafruitMotorHatDcMotors.Motor1Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor2Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor3Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
        motorCommands[AdafruitMotorHatDcMotors.Motor4Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
         pSpeed *= 0.5;
        break;
      case MoveCcw: 
        motorCommands[AdafruitMotorHatDcMotors.Motor1Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
        motorCommands[AdafruitMotorHatDcMotors.Motor2Index] = AdafruitMotorHatDcMotors.EMotorCommands.Backward;
        motorCommands[AdafruitMotorHatDcMotors.Motor3Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        motorCommands[AdafruitMotorHatDcMotors.Motor4Index] = AdafruitMotorHatDcMotors.EMotorCommands.Foreward;
        pSpeed *= 0.5;
        break;
      case Stop:
        motorCommands[AdafruitMotorHatDcMotors.Motor1Index] = AdafruitMotorHatDcMotors.EMotorCommands.Release;
        motorCommands[AdafruitMotorHatDcMotors.Motor2Index] = AdafruitMotorHatDcMotors.EMotorCommands.Release;
        motorCommands[AdafruitMotorHatDcMotors.Motor3Index] = AdafruitMotorHatDcMotors.EMotorCommands.Release;
        motorCommands[AdafruitMotorHatDcMotors.Motor4Index] = AdafruitMotorHatDcMotors.EMotorCommands.Release;
        break;
    }
 
    
    // set wheel moving speeds
    motorSpeeds[AdafruitMotorHatDcMotors.Motor1Index] = (byte) (pSpeed & 0xFF);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor2Index] = (byte) (pSpeed & 0xFF);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor3Index] = (byte) (pSpeed & 0xFF);
    motorSpeeds[AdafruitMotorHatDcMotors.Motor4Index] = (byte) (pSpeed & 0xFF);

    // Write to pwm chip for actual movement
    Motors.DriveMotor(motorCommands, motorSpeeds);
  }
}
