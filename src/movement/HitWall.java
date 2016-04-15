package movement;

import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import sensors.TouchSensor;

/**
 * The Hit Wall behaviour is required to reverse the EV3 away from any objects it collides with. Collisions
 * are detected using the touch sensor.
 * @author Oliver Palmer
 *
 */
public class HitWall implements Behavior {

	private boolean suppressed = false;
	private MovePilot pilot;
	private TouchSensor touch;
	
	public HitWall(MovePilot pilot, EV3TouchSensor sensor) {
		this.pilot = pilot;
		this.touch = new TouchSensor(sensor);
	}
	
	/**
	 * Takes control when the touch sensor is pressed
	 */
	@Override
	public boolean takeControl() {
		return touch.pressed();
	}

	/**
	 * Reverses the EV3 200mm, turns 90 degrees
	 */
	@Override
	public void action() {
		suppressed = false;
		pilot.travel(-200);
		pilot.rotate(-90);
		while (!suppressed) {
			Thread.yield();
		}
		
	}

	/**
	 * Suppressed by boolean flag
	 */
	@Override
	public void suppress() {
		suppressed = true;
	}
	
	

}
