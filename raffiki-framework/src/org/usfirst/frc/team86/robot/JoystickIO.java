package org.usfirst.frc.team86.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;

import org.usfirst.frc.team86.util.Button;
import org.usfirst.frc.team86.util.IJoystick;

public class JoystickIO {
	// Joysticks
	  private static ArrayList<Button> buttons = new ArrayList<>();
	public static Joystick leftJoystick;
	public static Joystick rightJoystick;
	public static Joystick coJoystick;
	public static Joystick xBox;
	
	public static Button getCube;
	public static Button releaseCube;
	public static Button jogArmUp;
	public static Button jogArmDown;
	
	public static Button switchHeight;
	public static Button scaleLow;
	public static Button scaleHigh;
	public static Button gearShiftDown;
	
	  static {
		if(!SmartDashboard.getBoolean("Xbox Control", false)){
			 leftJoystick =  new Joystick(0);
			 rightJoystick = new Joystick(1);
			 coJoystick = new Joystick(2);
			 
			 getCube = createButton(coJoystick,1);
			 releaseCube = createButton(coJoystick,4);
			 jogArmUp = createButton(coJoystick, 10);
			 jogArmDown = createButton(coJoystick, 12);
			 
			 switchHeight = createButton(coJoystick, 11);
			 scaleLow = createButton(coJoystick, 9);
			 scaleHigh = createButton(coJoystick, 7);
			 gearShiftDown = createButton(rightJoystick, 1);
		} else if(SmartDashboard.getBoolean("Xbox Control", false)){
			xBox = new Joystick(0);
			
			//get cube is L trigger 
			 releaseCube = createButton(xBox,2);
			 jogArmUp = createButton(xBox,6);
			 jogArmDown = createButton(xBox,5);
			 gearShiftDown = createButton(xBox, 4);
		}
	}
	
	
	
	
	
	// Buttons
		
	
//	public static Button exchange = createButton(coJoystick, 2);
	
	
		
	//public static Button gearShiftUp = createButton(rightJoystick, 5);

	
	//public static Button toggleFront = createButton(leftJoystick, 1);	
	
	//public static Button runAuto = createButton(leftJoystick, 11);
	
	//public static Button climbUp = createButton(coJoystick, 5);
	//public static Button climbDown = createButton(coJoystick, 3);
	
	
	
	// test buttons
//	public static Button testFlippers = createButton(rightJoystick, 2);
//	public static Button testClaw = createButton(coJoystick, 3);
	
//	public static Button testRollers = createButton(leftJoystick, 3);
	
//	public static Button testSpeedDrive = createButton(leftJoystick, 1);
		
	public static void update() {
			
		for (Button b : buttons) {
			b.update();
		}
	}

	private static Button createButton(GenericHID stick, int button) {
		Button newButton = new Button(stick, button);
		buttons.add(newButton);
		return newButton;
	}
}
