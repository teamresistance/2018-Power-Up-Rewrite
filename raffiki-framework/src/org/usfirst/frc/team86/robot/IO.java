package org.usfirst.frc.team86.robot;



import org.usfirst.frc.team86.util.ISolenoid;
import org.usfirst.frc.team86.util.InvertibleDigitalInput;
import org.usfirst.frc.team86.util.InvertibleSolenoid;
import org.usfirst.frc.team86.util.NavX;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class IO {
	
	// all ports checked
	public static ISolenoid flippers = new InvertibleSolenoid(1, 1, false);
	public static ISolenoid claw = new InvertibleSolenoid(1, 0, false);
	public static ISolenoid gearShifter = new InvertibleSolenoid(1, 2, false);
	public static ISolenoid armBrake = new InvertibleSolenoid(1, 3, true);
	
	public static VictorSP rightIntake = new VictorSP(0);
	public static VictorSP leftIntake = new VictorSP(1);
	
	public static InvertibleDigitalInput rollersFullyIn = new InvertibleDigitalInput(2, true);
	public static InvertibleDigitalInput armBottomSwitch = new InvertibleDigitalInput(0, false);
	public static InvertibleDigitalInput cubeFullyIn = new InvertibleDigitalInput(1, true);
	public static InvertibleDigitalInput cubePartiallyIn = new InvertibleDigitalInput(3, true);
	
	public static TalonSRX armMotor = new TalonSRX(58);
	
	public static TalonSRX leftFront = new TalonSRX(57);
	public static TalonSRX leftBack = new TalonSRX(55);
	public static TalonSRX rightFront = new TalonSRX(56);
	public static TalonSRX rightBack = new TalonSRX(59);
	
	public static ISolenoid frontLight1 = new InvertibleSolenoid(1, 4, false);
	public static ISolenoid frontLight2 = new InvertibleSolenoid(1, 5, false);
	
	public static Compressor compressor = new Compressor(1);
	public static Relay compressorRelay = new Relay(0);
	
	public static PowerDistributionPanel pdp = new PowerDistributionPanel(0);
	
	public static NavX navX = new NavX();
	
}
