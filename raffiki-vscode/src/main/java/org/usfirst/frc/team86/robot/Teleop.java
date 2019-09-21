package org.usfirst.frc.team86.robot;

import org.usfirst.frc.team86.util.IUpdate;

public class Teleop implements IUpdate {
private IUpdate[] subsystems;

public Teleop(IUpdate... subsystems) {
	this.subsystems = subsystems;
}

	@Override
	public void init() {
		for (IUpdate subsystem: subsystems) {
			subsystem.init();
		}
	}

	@Override
	public void update() {
		for (IUpdate subsystem: subsystems) {
			subsystem.update();
		}
		
	}

}
