
package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

// import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Robot extends TimedRobot {
  private final VictorSPX dt = new VictorSPX(1);
  private final VictorSPX df = new VictorSPX(2);
  private final VictorSPX et = new VictorSPX(3);
  private final VictorSPX ef = new VictorSPX(4);

  double velEsq = 0;
  double velDir = 0;

  // private final PWMSparkMax m_leftDrive = new PWMSparkMax(0);
  // private final PWMSparkMax m_rightDrive = new PWMSparkMax(1);

  // Joystick
  private Joystick fred;

  public Robot() {
    fred = new Joystick(1);
    dt.setInverted(true);
    df.setInverted(true);
  }

  int angulo = fred.getPOV();
  public void POV() {
    switch (angulo) {
      case 0: 
        this.setVelDir(1);
        this.setVelEsq(1);
       break;
      case 45:
        this.setVelDir(0.8);
        this.setVelEsq(1);
        break;
      case 90:
        this.setVelDir(0.2);
        this.setVelEsq(0.9);
        break;
      case 180:
        this.setVelDir(-1);
        this.setVelEsq(-1);
        break;
    }
  }

  double vel = 0;
  @Override
  public void teleopPeriodic() {
    // botôes
    boolean BotaoA = fred.getRawButton(1);
    boolean BotaoB = fred.getRawButton(2);
    boolean BotaoC = fred.getRawButton(3);
    boolean BotaoD = fred.getRawButton(4);

    if (BotaoA == true) {
      this.setVel(0.25);
    } else if (BotaoB == true) {
      this.setVel(0.5);
    } else if (BotaoC == true) {
      this.setVel(0.75);
    } else if (BotaoD == true){
      this.setVel(1);
    } else {
      this.getVel();
    }
  }


  // getter e setter
  public double getVel() {
    return vel;
  }


  public void setVel(double vel) {
    this.vel = vel;
  }


  public double getVelEsq() {
    return velEsq;
  }


  public void setVelEsq(double velEsq) {
    this.velEsq = velEsq;
  }


  public double getVelDir() {
    return velDir;
  }


  public void setVelDir(double velDir) {
    this.velDir = velDir;
  }

  
}
