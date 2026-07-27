package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.NeutralMode;
// import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Robot extends TimedRobot {
  private final VictorSPX dt = new VictorSPX(1);
  private final VictorSPX df = new VictorSPX(2);
  private final VictorSPX et = new VictorSPX(3);
  private final VictorSPX ef = new VictorSPX(4);

  double velEsq = 0;
  double velDir = 0;
  double velBotao = 0;

  // Joystick
  Joystick fred = new Joystick(1);

  // botôes
  boolean BotaoA = fred.getRawButton(1);
  boolean BotaoB = fred.getRawButton(2);
  boolean BotaoC = fred.getRawButton(3);
  boolean BotaoD = fred.getRawButton(4);

  public Robot() {
    dt.setInverted(true);
    df.setInverted(true);
    et.setInverted(false);
    ef.setInverted(false);

    dt.setNeutralMode(NeutralMode.Brake);
    df.setNeutralMode(NeutralMode.Brake);
    et.setNeutralMode(NeutralMode.Brake);
    ef.setNeutralMode(NeutralMode.Brake);

    dt.configNeutralDeadband(0.04);
    df.configNeutralDeadband(0.04);
    et.configNeutralDeadband(0.04);
    ef.configNeutralDeadband(0.04);
  }

  // metodos
  int angulo = fred.getPOV();
  public void POV() {
    switch (angulo) {
      case 0: 
        this.setVelDir(velBotao * 1);
        this.setVelEsq(velBotao * 1);
       break;
      case 45:
        this.setVelDir(velBotao * 0.8);
        this.setVelEsq(velBotao * 1);
        break;
      case 90:
        this.setVelDir(velBotao * 0.2);
        this.setVelEsq(velBotao * 0.9);
        break;
      case 135:
        this.setVelDir(velBotao * 1);
        this.setVelEsq(velBotao * 0.3);
      case 180:
        this.setVelDir(velBotao * -1);
        this.setVelEsq(velBotao * -1);
        break;
      case 225:
        this.setVelDir(velBotao * 0.3);
        this.setVelEsq(velBotao * 1);
        break;
      case 270:
        this.setVelDir(velBotao * 0.9);
        this.setVelEsq(velBotao * 0.2);
        break;
      case 315:
        this.setVelDir(velBotao * 1);
        this.setVelEsq(velBotao * 0.8);
        break;
    }
  }

  @Override
  public void teleopPeriodic() {

    if (BotaoA == true) {
      velBotao = 0.25;
    } else if (BotaoB == true) {
      velBotao = 0.5;
    } else if (BotaoC == true) {
      velBotao = 0.75;
    } else if (BotaoD == true){
      velBotao = 1;
    } else {
      velBotao = 0.2;
    }
  }

  // dashboard
  public void execute() {
   SmartDashboard.putBoolean("Botao A ", BotaoA);
   SmartDashboard.putBoolean("Botao B ", BotaoB);
   SmartDashboard.putBoolean("Botao C ", BotaoC);
   SmartDashboard.putBoolean("Botao D ", BotaoD);
   SmartDashboard.putNumber("Velocidade do motor direito ", velDir);
   SmartDashboard.putNumber("Velocidade do motor esquerdo ", velEsq);
   SmartDashboard.putNumber("Velocidade ", velBotao);
   SmartDashboard.putNumber("Joystick X value", fred.getX());
  }

  // getter e setter
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