package org.usfirst.frc.team86.robot;

import org.usfirst.frc.team86.util.IUpdate;
import org.usfirst.frc.team86.util.InvertibleSolenoid;
import org.usfirst.frc.team86.util.Time;
import org.usfirst.frc.team86.util.ISolenoid;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm implements IUpdate{
	
	private int state = 0;
	private boolean rollersEnabled = false; 
	
	// TODO set values
	private double positionRest = 50;
	private double positionAutoSwitch = 1750;
	private double positionSwitch = 2500;
	private double positionLowScale = 5400;
	private double positionHighScale = 7100;
	private double positionAutoHighScale = 6350;
	
	private double positionCubeClears = 500;
	
	private double initialTime;
	
	private double unGrabCubeDelay = 0.1;
	private double grabCubeDelay = 0.75;
	private double flipperOpenDelay = 1.0;
	
	private double armPos = 0;
	private double armFallPower = -0.6;
		
	private String armTag = "Arm Position";
	
	private double minJogDownVelocity = 100.0;
	private double minJogUpVelocity = 30.0;
	
	private double jogUpPower = 0.75;
	private double jogDownPower = -0.25;
	private int POVval;
	
	private TalonSRX armMotor;
	private VictorSP leftIntake;
	private VictorSP rightIntake;
	private ISolenoid flippers;
	private ISolenoid claw;
	private ISolenoid armBrake;
	

	public Arm(TalonSRX armMotor, VictorSP leftIntake, VictorSP rightIntake, ISolenoid flippers, ISolenoid claw, ISolenoid armBrake){
		this.armMotor = armMotor;
		this.leftIntake = leftIntake;
		this.rightIntake = rightIntake;
		this.flippers = flippers;
		this.claw = claw;
		this.armBrake = armBrake;
		
		
		// adjustable values
//		SmartDashboard.putNumber("Minimum Velocity", 30);
//		SmartDashboard.putNumber("Arm Move Power", 1);
//		SmartDashboard.putNumber("Jog Up Power", 0.75);
//		SmartDashboard.putNumber("Jog Down Power", -0.25);
//		SmartDashboard.putNumber("Arm Deadband", 0);
	}
	
	public void init(){
		armMotor.setInverted(true);
		armMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		armMotor.setSensorPhase(false);
		armMotor.setSelectedSensorPosition(0, 0, 0);
		armMotor.setNeutralMode(NeutralMode.Brake);
		armBrake.set(false);
		
		armMotor.enableVoltageCompensation(true);
		armMotor.configVoltageCompSaturation(12.0, 0);
		armMotor.configVoltageMeasurementFilter(32, 0);
		
		armMotor.configClosedloopRamp(1.0, 0);
	}
	public void update() {
	
		SmartDashboard.putNumber("Arm State", state);
		armPos = armMotor.getSelectedSensorPosition(0);
	
		if (rollersEnabled) {
			int POVval = JoystickIO.coJoystick.getPOV();
			
			switch(POVval) {
			case 0:
				leftIntake.set(-1);
				rightIntake.set(1);
				break;
			case 90:
				leftIntake.set(-0.75);
				rightIntake.set(-0.75);
				break;
			case 180:
				leftIntake.set(1);
				rightIntake.set(-1);
				break;
			case 270:
				leftIntake.set(0.75);
				rightIntake.set(0.75);
				break;
			case -1:
				leftIntake.set(0);
				rightIntake.set(0);
				break;
			}
		} else {
			leftIntake.set(0);
			rightIntake.set(0);
		}
		
		if (armPos >= positionCubeClears) {
			flippers.set(false);
		}
		
//		if (JoystickIO.climb.isDown()) {
//			state = 10;
//		} else if (JoystickIO.climb.onButtonReleased()) {
//			state = 21;
//		}
	//	if (JoystickIO.climbUp.onButtonPressed()) {
	//		state = 10;
//		} else if (JoystickIO.climbDown.isDown()) {
//			state = 17;
//		} else if (JoystickIO.climbDown.onButtonReleased()) {
//			state = 13;
//		}
		
		
		if (JoystickIO.jogArmUp.isDown()) {
			state = 11;
		} else if (JoystickIO.jogArmDown.isDown()) {
			state = 12;
		} //else if (JoystickIO.climb.isDown()) {
			//state = 17;
//		}
		
		if (IO.armBottomSwitch.get()) {
			armMotor.setSelectedSensorPosition(0, 0, 0);
		}
		
		switch(state) {
		case 0:
			claw.set(true);
			// open flippers while pushing button
			if (JoystickIO.getCube.isDown()) {
				flippers.set(true);
				rollersEnabled = true;
			}
			// close flippers on release
			if (JoystickIO.getCube.onButtonReleased() || IO.cubePartiallyIn.get()) {
				flippers.set(false);
				state = 1;
			}
			break;
		case 1:
			if (IO.rollersFullyIn.get()) {
				// check if intake failed
				rollersEnabled = false;
				state = 0;
			} else if (IO.cubeFullyIn.get()) {
				// check if intake successful
				claw.set(false); // grab
				rollersEnabled = false;
				initialTime = Time.getTime();
				state = 2;
			}
			break;
		case 2:
			// delay between claw grab and flipper open
			// give time to grab cube
			if(Time.getTime() - initialTime >= grabCubeDelay) {
				initialTime = Time.getTime();
				state = 3;
			}
			break;
		case 3:
			flippers.set(true); // open flippers to move arm
			if (Time.getTime() - initialTime >= flipperOpenDelay) {
				state = 14;
			}
		case 14:
			// set to rest position
			if (setPos(positionRest, 0.5)) {
				state = 4;
			}
			break;
		case 4:
			// Listen for button
//			if (JoystickIO.exchange.onButtonPressed()) {
//				state = 5;
//			} else
			if (armPos > 7000) {
				armBrake.set(false);
				armMotor.set(ControlMode.PercentOutput, 0.2);
			} else {
				armBrake.set(true);
				armMotor.set(ControlMode.PercentOutput, 0.0);
			}
			
			if (JoystickIO.switchHeight.onButtonPressed()) {
				flippers.set(true);
				state = 7;
			} else if (JoystickIO.scaleLow.onButtonPressed()) {
				flippers.set(true);
				state = 9;
			} else if (JoystickIO.scaleHigh.onButtonPressed()) {
				flippers.set(true);
				state = 10;
			} else if (JoystickIO.coJoystick.getPOV() == 0) {
				initialTime = Time.getTime();
				state = 5;
			} else if (JoystickIO.releaseCube.onButtonPressed()) {
				state = 8;
			} else {
				flippers.set(false); // grip cube with flippers
			}
			// TODO add delay before moving arm -- give time for flippers to open
			break;
		case 5:
			claw.set(true); // release
			// delay give time for claw to ungrab the cube
			if (Time.getTime() - initialTime >= unGrabCubeDelay) {
				if (JoystickIO.coJoystick.getPOV() == 0) {
					leftIntake.set(-1);
					rightIntake.set(1);
				} else if (JoystickIO.coJoystick.getPOV() == 180) {
					leftIntake.set(1);
					rightIntake.set(-1);
				} else {
					leftIntake.set(0);
					rightIntake.set(0);
				}
				if (IO.rollersFullyIn.get()) {
					leftIntake.set(0);
					rightIntake.set(0);
					state = 6;
				}
			}
			break;
		case 6:
			// set to ground position
			armBrake.set(false);		
			if (!IO.armBottomSwitch.get()) {
				if (armPos >= 500) {
					armMotor.set(ControlMode.PercentOutput, armFallPower);
				} else {
					armMotor.set(ControlMode.PercentOutput, 0);
				}
			} else {
				armMotor.set(ControlMode.PercentOutput, 0);
				state = 0;
			}
			break;
		case 7:
			// set to switch position
			if (setPos(positionSwitch, 1.0)) {
				state = 4;
			}
			break;
		case 8:
			if (JoystickIO.releaseCube.isDown()) {
				claw.set(true);
			}
			if (JoystickIO.releaseCube.onButtonReleased()) {
				state = 6;
			}
			break;
		case 9:
			// set position to low scale
			if (setPos(positionLowScale, 1.0)) {
				state = 4;
			}
			break;
		case 10:
			// set position to high scale
			if (setPos(positionHighScale, 1.0)) {
				state = 4;
			}
			break;
		case 11:
			armBrake.set(false);
			if (JoystickIO.jogArmUp.isDown()) {
				armMotor.set(ControlMode.PercentOutput, jogUpPower);
			} else if (JoystickIO.jogArmUp.onButtonReleased()) {
				state = 13;
			}
			break;
		case 12:
			armBrake.set(false);
			if (JoystickIO.jogArmDown.isDown()) {
				armMotor.set(ControlMode.PercentOutput, jogDownPower);
			} else if (JoystickIO.jogArmDown.onButtonReleased()) {
				state = 13;
			}
			break;
		case 13:
			armMotor.set(ControlMode.PercentOutput, 0);
			double armVel = armMotor.getSelectedSensorVelocity(0);
			if (armVel < 0 && armVel > (-1 * minJogDownVelocity)) {
				armBrake.set(true);
				state = 4;
			} else if (armVel > 0 && armVel < minJogUpVelocity) {
				armBrake.set(true);
				state = 4;
			}
			break;
		case 17:
			armBrake.set(false);
			armMotor.set(ControlMode.PercentOutput, -1.0);
//			if (JoystickIO.climbDown.isDown()) {
//				armMotor.set(ControlMode.PercentOutput, -1.0);
//			} else if (JoystickIO.climbDown.onButtonReleased()) {
//				state = 13;
//			}
			break;
		case 19:
			if (setPos(positionAutoSwitch, 1.0)) {
				state = 4;
			}
			break;
		case 20:
			armMotor.set(ControlMode.PercentOutput, 0);
			claw.set(true);
			if (JoystickIO.releaseCube.onButtonPressed()) {
				state = 6;
			}
			break;
		case 21:
			if (setPos(positionRest, -1.0)) {
				state = 4;
			}
			break;
		case 22:
			if (setPos(positionAutoHighScale, 1.0)) {
				state = 4;
			}
			break;
		}
		
	}
	
	public boolean onTarget(double target, double deadband) {
		return ((armPos > target - deadband) && (armPos < target + deadband));
	}
	
	public boolean setPos(double target, double armPower) {
		double armVel = armMotor.getSelectedSensorVelocity(0);
		if ((armPos >= target && armVel > 0) || (armPos <= target && armVel < 0)) {
			// if (going up and above target) OR (going down and below target)
			armMotor.set(ControlMode.PercentOutput, 0);
			if (armVel < 0 && armVel > (-1 * minJogDownVelocity)) {
				SmartDashboard.putString("Set Arm Position State", "Engage Brake");
				armBrake.set(true);
				return true;
			} else if (armVel > 0 && armVel < minJogUpVelocity) {
				SmartDashboard.putString("Set Arm Position State", "Engage Brake");
				armBrake.set(true);
				return true;
			} else {
				SmartDashboard.putString("Set Arm Position State", "Cut Power");
				armBrake.set(false);
			}
		} else {
			SmartDashboard.putString("Set Arm Position State", "Supply Power");
			armBrake.set(false);
			armMotor.set(ControlMode.PercentOutput, armPower);
		}
		return false;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
}