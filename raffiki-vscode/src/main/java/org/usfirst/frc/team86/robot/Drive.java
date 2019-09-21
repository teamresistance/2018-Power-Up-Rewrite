package org.usfirst.frc.team86.robot;
import org.usfirst.frc.team86.util.IUpdate;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive implements IUpdate{
private TalonSRX leftFront;
private TalonSRX leftBack;
private TalonSRX rightFront;
private TalonSRX rightBack;
private double speed;
private double turnD;
private boolean xbControl;
private boolean arcD;

	public Drive(TalonSRX leftFront, TalonSRX leftBack, TalonSRX rightFront, TalonSRX rightBack){
	this.leftFront = leftFront;
	this.leftBack = leftBack;
	this.rightFront = rightFront;
	this.rightBack = rightBack;

}
	public void init(){
		
		leftFront.setInverted(false);
		leftBack.setInverted(false);
		rightFront.setInverted(true);
		rightBack.setInverted(true);
		
		leftFront.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		leftBack.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		rightFront.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		rightBack.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		
		leftFront.setSensorPhase(false);
		leftBack.setSensorPhase(false);
		rightFront.setSensorPhase(false);
		rightBack.setSensorPhase(false);
		
		leftBack.set(ControlMode.Follower, IO.leftFront.getDeviceID());
		rightBack.set(ControlMode.Follower, IO.rightFront.getDeviceID());
		
		leftFront.enableVoltageCompensation(true);
		leftBack.enableVoltageCompensation(true);
		rightFront.enableVoltageCompensation(true);
		rightBack.enableVoltageCompensation(true);
		
		leftFront.configVoltageCompSaturation(12.0, 0);
		leftBack.configVoltageCompSaturation(12.0, 0);
		rightFront.configVoltageCompSaturation(12.0, 0);
		rightBack.configVoltageCompSaturation(12.0, 0);
		
		leftFront.configVoltageMeasurementFilter(32, 0);
		leftBack.configVoltageMeasurementFilter(32, 0);
		rightFront.configVoltageMeasurementFilter(32, 0);
		rightBack.configVoltageMeasurementFilter(32, 0);

		leftFront.configClosedloopRamp(1.0, 0);
		rightFront.configClosedloopRamp(1.0, 0);
		

	}
	public void update(){
		xbControl = SmartDashboard.getBoolean("Xbox Control", false);
		arcD = SmartDashboard.getBoolean("Arcade Xbox", false);
		if(!xbControl){
			leftFront.set(ControlMode.PercentOutput, -1*(JoystickIO.leftJoystick.getY()));
			rightFront.set(ControlMode.PercentOutput, -1*(JoystickIO.rightJoystick.getY()));
		} else if(arcD && xbControl){
			
			speed = JoystickIO.xBox.getRawAxis(5);
			turnD = JoystickIO.xBox.getRawAxis(4);
			
				leftFront.set(ControlMode.PercentOutput, -1 * (speed - turnD));
				rightFront.set(ControlMode.PercentOutput, -1 * (-speed + turnD));
				//leftFront.set(ControlMode.PercentOutput, -1*(JoystickIO.control.getRawAxis(5)-JoystickIO.control.getRawAxis(4)));
				//rightFront.set(ControlMode.PercentOutput, -1*(JoystickIO.control.getRawAxis(5)+JoystickIO.control.getRawAxis(4)));
				
		}
		
		else  if (xbControl && !arcD) {
				leftFront.set(ControlMode.PercentOutput, -1*(JoystickIO.xBox.getRawAxis(1)));
				rightFront.set(ControlMode.PercentOutput, -1*(JoystickIO.xBox.getRawAxis(5)));
					}
				
		
		
		if (JoystickIO.gearShiftDown.onButtonPressed()) {
			if (IO.gearShifter.getInvertableState()==true){
				IO.gearShifter.set(false);
			}else
				IO.gearShifter.set(true);
		}
		
	}


	}


