package org.usfirst.frc.team86.util;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class NavX {
	//Declare a variable using SPI connector 
	public AHRS ahrs = new AHRS(SPI.Port.kMXP);
	
	//Returns actual Z position
	public double getAngle() {
		return ahrs.getAngle();
	}
	//Returns a normalized Z position between 0 and 360 degrees
	public double getNormalizedAngle() {
		return ((ahrs.getAngle()% 360) + 360) % 360;
	}
	//Resets Gyro to 0 degrees.
	public void reset() {
		ahrs.reset();
	}
	//Returns variable
	public AHRS getAHRS() {
		return ahrs;
	}

}