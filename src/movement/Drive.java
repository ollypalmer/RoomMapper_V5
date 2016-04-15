package movement;

import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import mapping.Mapping;

/**
 * The Drive Behaviour is the lowest level priority and will move the EV3 forwards whilst logging
 * its position every 1000 milliseconds. The drive method will always take control if no other
 * behaviours require focus.
 * @author Oliver Palmer
 *
 */
public class Drive implements Behavior {

	private boolean suppressed = false;
	private MovePilot pilot;
	private OdometryPoseProvider odoPose;
	private Mapping map;
	
	public Drive(MovePilot pilot, OdometryPoseProvider odoPose) {
		this.pilot = pilot;
		this.odoPose = odoPose;
		this.map = Mapping.getInstance();
	}
	
	/**
	 * This behaviour will always take control as it is the lowest level priority
	 */
	@Override
	public boolean takeControl() {
		return true;
	}

	/**
	 * Drives forwards whilst logging location data every 1000 milliseconds
	 */
	@Override
	public void action() {
		suppressed = false;
		pilot.forward();
		while (!suppressed) {
			Thread.yield();
			map.logPosition(odoPose.getPose());
			Delay.msDelay(1000);
		}
		pilot.stop();
	}

	/**
	 * Suppressed by boolean flag
	 */
	@Override
	public void suppress() {
		suppressed = true;
	}

}
