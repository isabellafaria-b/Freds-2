
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

  // variáveis
  double velEsq = 0;
  double velDir = 0;
  double velBotao = 0;  
  int angulo;
  public static final double deadzone = 0.04;

  // Joystick
  Joystick fred = new Joystick(0);

  // botôes
  boolean BotaoA;
  boolean BotaoB;
  boolean BotaoC;
  boolean BotaoD;
  double trigelaD;
  double trigelaE;

  // analogicos
  double x1;
  double x2;
  double y1;
  double y2;
  double hipotenusa;
  double a;

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

    // analogicos
    x1 = fred.getRawAxis(0);
    y1 = fred.getRawAxis(1);
    x2 = -fred.getRawAxis(4);
    y2 = fred.getRawAxis(5);
    
    // triggers
    trigelaD = fred.getRawAxis(2);
    trigelaE = fred.getRawAxis(3);
    trigelaE *= -1;

    // chamando as funções
    execute();
    triggers();
    POV();
    if (fred.getPOV() == -1) {
      triggers();
    } else if (trigelaE == 0 && trigelaD == 0) {
      POV();
    }

    // setters
    setVelDir(velDir);
    setVelEsq(velEsq);
  }

  public void triggers() {
    if (fred.getRawAxis(2) > deadzone) {
      velDir = trigelaE;
      velEsq = trigelaE;
    } else if (fred.getRawAxis(3) > -deadzone) {
      velDir = trigelaD;
      velEsq = trigelaD;
    } else {
      velDir = 0;
      velEsq = 0;
    }
  }

  public void calculos(){
    hipotenusa = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
    a = hipotenusa * 0.25;
  }

  public void analEsq() {
    if (hipotenusa > 1) {
      hipotenusa = 1;
    }

    if (x1 > deadzone && y1 > deadzone) { // eixo I
      velEsq = hipotenusa;
      velDir = hipotenusa - a;
    } else if (x1 < -deadzone && y1 > deadzone) { // eixo II
      velEsq = hipotenusa - a;
      velDir = hipotenusa;
    } else if (x1 < -deadzone && y1 < -deadzone) { // eixo III
      velEsq = hipotenusa + a;
      velDir = hipotenusa;
    } else if (x1 > deadzone && y1 < -deadzone) { // eixo IV
      velEsq = hipotenusa;
      velDir = hipotenusa + a;


    if (x1 < deadzone && y1 > deadzone) {
      velEsq = hipotenusa;
      velDir = hipotenusa;
    } else if (x1 > deadzone && y1 < deadzone) {
      velEsq = hipotenusa;
      velDir = 0;
    } else if (x1 < deadzone && y1 > -deadzone) {
      velEsq = -hipotenusa;
      velDir = -hipotenusa;
    } else if (x1 > -deadzone && y1 < deadzone) {
      velEsq = 0;
      velDir = hipotenusa;
    }
    }
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
   SmartDashboard.putNumber("POV", angulo);
   SmartDashboard.putNumber("Velocidade botao", velBotao);
   SmartDashboard.putNumber("Velocidade do motor direito", velDir);
   SmartDashboard.putNumber("Velocidade do motor esquerdo", velEsq);
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