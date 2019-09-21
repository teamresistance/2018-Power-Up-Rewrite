package org.usfirst.frc.team86.util;

public interface IJoystick {
	
	public double getX();
	
	public double getY();
	
	public int getPOV();
	
	public boolean getRawButton(int button);

	public void update();

}
