/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team86.robot;

import org.usfirst.frc.team86.util.Time;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private Teleop teleop;
	
	private Arm arm;
	private Drive drive;
	private XboxArm xboxarm;

	


	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();
		SmartDashboard.putBoolean("Xbox Control", false);
		SmartDashboard.putBoolean("Arcade Xbox", false);
		
		drive = new Drive(IO.leftFront,
				IO.leftBack,
				IO.rightFront,
				IO.rightBack);
		arm = new Arm(IO.armMotor,
					IO.leftIntake,
					IO.rightIntake,
					IO.flippers,
					IO.claw,
					IO.armBrake);
		xboxarm = new XboxArm(IO.armMotor,
				IO.leftIntake,
				IO.rightIntake,
				IO.flippers,
				IO.claw,
				IO.armBrake);
		if(SmartDashboard.getBoolean("Xbox Control", false)){
			teleop = new Teleop(drive, xboxarm);
		} else{
			teleop = new Teleop(drive, arm);
		}
	
		
		
		
	}
	


	@Override
	public void autonomousInit() {
	
	}


	@Override
	public void autonomousPeriodic() {
		
	}
@Override
public void teleopInit(){
	IO.navX.reset();
	teleop.init();
	arm.setState(20);
}
	@Override
	public void teleopPeriodic() {
		IO.compressorRelay.set(IO.compressor.enabled() ? Relay.Value.kOn : Relay.Value.kOff);
		Time.update();
		JoystickIO.update();
		teleop.update();
		SmartDashboard.putNumber("State", arm.getState());
	}

	@Override
	public void testPeriodic() {
	}
}
