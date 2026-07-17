package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;


public class Robot extends TimedRobot {
  private Joystick fred;

  public Robot() {
    fred = new Joystick(0);

    // Botões
    boolean botaoA = fred.getRawButton(1);
    boolean botaoB = fred.getRawButton(2);
    boolean botaoC = fred.getRawButton(3);
    boolean botaoD = fred.getRawButton(4);

    if (botaoA.getRawButtonPressed(true)){

    }
  }

  @Override
  public void teleopPeriodic() {

  }
}