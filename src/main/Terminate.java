package main;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import mapping.Mapping;
import utilities.SaveData;

/**
 * The terminate behaviour class runs when the escape button on the EV3 is pressed. It
 * saves the map data to a file and exits the program. The terminate behaviour cannot be
 * suppressed as it is the highest priority behaviour.
 * @author Oliver Palmer
 *
 */
public class Terminate implements Behavior{
	
	private EV3TouchSensor touch;
	private NXTUltrasonicSensor ultraSensor;
	private Mapping map;
	
	public Terminate(EV3TouchSensor touch, NXTUltrasonicSensor ultraSensor) {
		this.touch = touch;
		this.ultraSensor = ultraSensor;
		this.map = Mapping.getInstance();
	}
	
	/**
	 * Takes control when the escape button is pressed
	 */
	@Override
	public boolean takeControl() {
		return Button.ESCAPE.isDown();
	}

	/**
	 * Saves the map data to .csv, closes the sensors and exits the program
	 */
	@Override
	public void action() {
		new SaveData(map.getMap());
		touch.close();
		ultraSensor.close();
		System.exit(0);
	}

	/**
	 * The terminate method does not require a suppressed method as it
	 * is the highest priority method used to exit the program.
	 */
	@Override
	public void suppress() {
	}

}
