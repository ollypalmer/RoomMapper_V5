package movement;

import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import mapping.Mapping;
import sensors.UltrasonicSensor;

/**
 * The Detect Object Behaviour class is responsible for dictating the movement of the EV3 when an
 * object is detected by the ultrasonic sensor. It uses a 90 degree turn, travels 200mm then
 * performs another 90 degree turn while checking there is enough space to execute these movements.
 * The angle of rotation is inverted if two successful turns are performed. Environment data is logged
 * through out the process of the behaviour.
 * @author Oliver Palmer
 *
 */
public class DetectObject implements Behavior {

	private boolean suppressed = false;
	private MovePilot pilot;
	private UltrasonicSensor ultraSensor;
	private OdometryPoseProvider odoPose;
	private Mapping map;
	private double direction = -90;
	
	public DetectObject(MovePilot pilot, NXTUltrasonicSensor sensor, OdometryPoseProvider odoPose) {
		this.pilot = pilot;
		this.ultraSensor = new UltrasonicSensor(sensor);
		this.odoPose = odoPose;
		map = Mapping.getInstance();
	}
	
	/**
	 * Takes control when the Ultrasonic sensor returns a figure less then the specified distance
	 */
	@Override
	public boolean takeControl() {
		return ultraSensor.distance() < 400;
	}

	/**
	 * Dictates movement to avoid a detected object, logs any detected landmarks.
	 */
	@Override
	public void action() {
		suppressed = false;
		map.logLandmark(odoPose.getPose(), ultraSensor.distance());
		pilot.rotate(direction);
		
		// Delay so the reading is not taken before the robot has stopped moving
		Delay.msDelay(100);
		// Checks if the EV3 has space to drive in this direction, if false log the obstruction
		if (ultraSensor.distance() > 500) {
			pilot.travel(200);
			
			// Delay so the reading is not taken before the robot has stopped moving
			Delay.msDelay(1000);
			float sample = ultraSensor.distance();
			// Checks the sample is within the sensors accuracy range
			if (sample < 1000) {
				map.logLandmark(odoPose.getPose(), sample);
			}
			pilot.rotate(direction);
			// Log the EV3's new position before continuing
			map.logPosition(odoPose.getPose());
			// Changes the angle of rotation
			direction *= -1;
		} else {
			map.logLandmark(odoPose.getPose(), ultraSensor.distance());

		}
		while (pilot.isMoving() && !suppressed) {
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
