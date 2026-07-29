
package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Robot extends TimedRobot {

  private final VictorSPX dt = new VictorSPX(1);
  private final VictorSPX df = new VictorSPX(2);
  private final VictorSPX et = new VictorSPX(3);
  private final VictorSPX ef = new VictorSPX(4);

  double velEsq = 0;
  double velDir = 0;
  double velBotao = 0;
  int angulo;


  // Joystick
  Joystick fred = new Joystick(0);

  // botôes
  boolean BotaoA;
  boolean BotaoB;
  boolean BotaoC;
  boolean BotaoD;
  double trigelaD;
  double trigelaE;
 

  public Robot() {
    dt.setInverted(true);
    df.setInverted(true);

    dt.follow(df);
    et.follow(ef);

    dt.setNeutralMode(NeutralMode.Brake);
    df.setNeutralMode(NeutralMode.Brake);
    et.setNeutralMode(NeutralMode.Brake);
    ef.setNeutralMode(NeutralMode.Brake);

    dt.configNeutralDeadband(0.04);
    df.configNeutralDeadband(0.04);
    et.configNeutralDeadband(0.04);
    ef.configNeutralDeadband(0.04);
  }

  @Override
  public void teleopPeriodic() {
    angulo = fred.getPOV();

    BotaoA = fred.getRawButton(1);
    BotaoB = fred.getRawButton(2);
    BotaoC = fred.getRawButton(3);
    BotaoD = fred.getRawButton(4);

    if (BotaoA) {
      velBotao = 0.25;
    } else if(BotaoB) {
      velBotao = 0.5;
    } else if (BotaoC) {
      velBotao = 0.75;
    } else if (BotaoD) {
      velBotao = 1;
    }

    // triggers
    trigelaD = fred.getRawAxis(2);
    trigelaE = fred.getRawAxis(3);
    trigelaE *= -1;

    et.set(ControlMode.PercentOutput, trigelaD);
    dt.set(ControlMode.PercentOutput, trigelaE);
    et.set(ControlMode.PercentOutput, trigelaD);
    dt.set(ControlMode.PercentOutput, trigelaE);

    // chamando as funnções
    execute();
    POV();

    // setters
    setVelDir(velDir);
    setVelEsq(velEsq);
  }

  public void POV() {
    switch (angulo) {
      case -1:
        velEsq = velBotao * 0;
        velDir = velBotao * 0;
      case 0: 
       velEsq = velBotao * 1;
       velDir = velBotao * 1;
       break;
      case 45:
       velEsq = velBotao * 0.5;
       velDir = velBotao * -0.5;
        break;
      case 90:
       velEsq = velBotao * 1;
       velDir = velBotao * 0;
        break;
      case 135:
       velEsq = velBotao * 1;
       velDir = velBotao * 0.3;
      case 180:
       velEsq = velBotao * -1;
       velDir = velBotao * -1;
        break;
      case 225:
       velEsq = velBotao * 0.3;
       velDir = velBotao * 1;
        break;
      case 270:
       velEsq = velBotao * 0;
       velDir = velBotao * 1;
        break;
      case 315:
       velEsq = velBotao * -0.5;
       velDir = velBotao * 0.5;
        break;
    }
  }


  // dashboard
  public void execute() {
   SmartDashboard.putBoolean("Botao A", BotaoD);
   SmartDashboard.putBoolean("Botao B", BotaoB);
   SmartDashboard.putBoolean("Botao C", BotaoC);
   SmartDashboard.putBoolean("Botao D", BotaoA);
   SmartDashboard.putNumber("Velocidade do motor direito", velDir);
   SmartDashboard.putNumber("Velocidade do motor esquerdo", velEsq);
   SmartDashboard.putNumber("Velocidade botao", velBotao);
   SmartDashboard.putNumber("POV", angulo);
   SmartDashboard.putNumber("Trigger Direita", trigelaD);
   SmartDashboard.putNumber("Trigger Esquerda", trigelaE);
}

// setters
  public void setVelEsq(double velEsq) {
    ef.set(ControlMode.PercentOutput, velEsq);
    et.set(ControlMode.PercentOutput, velEsq);
  }

  public void setVelDir(double velDir) {
    df.set(ControlMode.PercentOutput, velDir);
    dt.set(ControlMode.PercentOutput, velDir);
  }

}