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
  static final double deadzone = 0.043315;

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
  double x1; double y1;
  double x2; double y2;
  double hipotenusa; double sen; double hipotenusa1; double sen1;

  public Robot() {
    dt.setInverted(true);
    df.setInverted(true);

    dt.follow(df);
    et.follow(ef);

    dt.setNeutralMode(NeutralMode.Brake);
    df.setNeutralMode(NeutralMode.Brake);
    et.setNeutralMode(NeutralMode.Brake);
    ef.setNeutralMode(NeutralMode.Brake);

    dt.configNeutralDeadband(0.043315);
    df.configNeutralDeadband(0.043315);
    et.configNeutralDeadband(0.043315);
    ef.configNeutralDeadband(0.043315);
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
    y1 = -fred.getRawAxis(1);
    x2 = fred.getRawAxis(4);
    y2 = -fred.getRawAxis(5);
    
    // triggers
    trigelaD = fred.getRawAxis(2);
    trigelaE = fred.getRawAxis(3);
    trigelaE *= -1;

    // chamando as funções
    calculosEsq();
    calculosDir();

    if(hipotenusa > deadzone){
     analEsq();
    } else if (hipotenusa1 > deadzone){
      analDir();
    } else if(trigelaD > deadzone || trigelaE < -deadzone){
      triggers();
    } else if (fred.getPOV() != -1) {
      POV();
    }else {
      velEsq = 0; velDir = 0;
    }

    execute();
  }

  public void drive() {
    // setters
    setVelDir(velDir);
    setVelEsq(velEsq);
  }

  public void triggers() {
    if (fred.getRawAxis(2) > deadzone) {
      velDir = trigelaE;
      velEsq = trigelaE;
    } else if (fred.getRawAxis(3) < -deadzone) {
      velDir = trigelaD;
      velEsq = trigelaD;
    } else {
      velEsq = 0; velDir = 0;
    }
  }

  // analogicos/calculos
  public void calculosEsq(){
    hipotenusa = Math.hypot(x1, y1);
    if(hipotenusa > 1) {
      hipotenusa = 1;
    }
    sen = y1 / hipotenusa;
  }

  public void calculosDir(){
    hipotenusa1 = Math.hypot(x2, y2);
    if(hipotenusa1 > 1) {
      hipotenusa1 = 1;
    }
    sen1 = y2 / hipotenusa1;
  }

  public void analEsq() {
    // movimentos diagonais
    if (x1 > deadzone && y1 > deadzone) { // eixo I
      velEsq = hipotenusa;
      velDir = sen;
    } else if (x1 < -deadzone && y1 > deadzone) { // eixo II
      velEsq = -sen;
      velDir = hipotenusa;
    } else if (x1 < -deadzone && y1 < -deadzone) { // eixo III
      velEsq = sen;
      velDir = -hipotenusa;
    } else if (x1 > deadzone && y1 < -deadzone) { // eixo IV
      velEsq = -hipotenusa;
      velDir = sen;
    }

    // movimentos verticais/horizontais
    if(velEsq > 0.99 && velDir > 0.99) {
      velEsq = 1;
      velDir = 1;
    } else if(velEsq < -0.99 && velDir < -0.99) {
      velEsq = -1;
      velDir = -1;
    }
  }

  public void analDir() {
    // movimentos diagonais
    if (x2 > deadzone && y2 > deadzone) { // eixo I
      velEsq = hipotenusa1;
      velDir = sen1;
    } else if (x2 < -deadzone && y2 > deadzone) { // eixo II
      velEsq = -sen1;
      velDir = hipotenusa1;
    } else if (x2 < -deadzone && y2 < -deadzone) { // eixo III
      velEsq = sen1;
      velDir = -hipotenusa1;
    } else if (x2 > deadzone && y2 < -deadzone) { // eixo IV
      velEsq = hipotenusa1;
      velDir = sen1;
    }

    // movimentos verticais/horizontais
    if(velEsq > 0.99 && velDir > 0.99) {
      velEsq = 1;
      velDir = 1;
    } else if(velEsq < -0.99 && velDir < -0.99) {
      velEsq = -1;
      velDir = -1;
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
   SmartDashboard.putNumber("Velocidade botao", velBotao);
   SmartDashboard.putNumber("Velocidade do motor direito", velDir);
   SmartDashboard.putNumber("Velocidade do motor esquerdo", velEsq);
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