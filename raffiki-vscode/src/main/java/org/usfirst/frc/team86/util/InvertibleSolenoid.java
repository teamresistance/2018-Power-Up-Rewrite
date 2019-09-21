package org.usfirst.frc.team86.util;

import edu.wpi.first.wpilibj.Solenoid;

public class InvertibleSolenoid extends Solenoid implements ISolenoid {

  private final boolean isInverted;

  public InvertibleSolenoid(int module, int channel) {
    this(module, channel, false);
  }

  public InvertibleSolenoid(int module, int channel, boolean isInverted) {
	super(module, channel);
    this.isInverted = isInverted;
  }

  @Override
  public void set(boolean state) {
	if (isInverted) {
		super.set(!state);
	} else {
		super.set(state);
	}
  }
  
  public Boolean getInvertableState(){
	  return super.get();
  }

}
