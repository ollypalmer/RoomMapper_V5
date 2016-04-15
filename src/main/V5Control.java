package main;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import movement.DetectObject;
import movement.Drive;
import movement.HitWall;

/**
 * The control class for the EV3. Responsible for creating and organising the behaviours for subsumption 
 * including other essential objects such as the move pilot and the odometry pose provider.
 * @author Oliver Palmer
 *
 */
public class V5Control {
	
	static MovePilot pilot;
	static Brick brick;
	static OdometryPoseProvider odoPose;
	static EV3TouchSensor touchSensor;
	static NXTUltrasonicSensor ultraSensor;

	/**
	 * Main method. Creates Behaviours and the Arbitrator to run them. Behaviours are stored in priority
	 * order, with the lowest priority at position 0.
	 * @param args
	 */
	public static void main(String[] args) {
		new V5Control();
		Behavior drive = new Drive(pilot, odoPose);
		Behavior detectObject = new DetectObject(pilot, ultraSensor, odoPose);
		Behavior hitWall = new HitWall(pilot, touchSensor);
		Behavior exit = new Terminate(touchSensor, ultraSensor);
		Behavior[] behaviors = { drive, detectObject, hitWall, exit };
		Arbitrator arbit = new Arbitrator(behaviors);
		arbit.go();
	}
	
	/**
	 * Creates the sensors and other necessary objects
	 */
	public V5Control() {
		pilot = getPilot();
		brick = BrickFinder.getDefault();
		odoPose = new OdometryPoseProvider(pilot);
		touchSensor = new EV3TouchSensor(brick.getPort("S1"));
		ultraSensor = new NXTUltrasonicSensor(brick.getPort("S2"));
	}

	/**
	 * Creates a Move Pilot object to control the movement of the EV3
	 * @return new MovePilot object
	 */
	public MovePilot getPilot() {
		Wheel wheelL = WheeledChassis.modelWheel(Motor.A, 43.2).offset(68.4);
		Wheel wheelR = WheeledChassis.modelWheel(Motor.B, 43.2).offset(-68.4);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheelL, wheelR}, WheeledChassis.TYPE_DIFFERENTIAL);
		return pilotConfig(new MovePilot(chassis));
	}
	
	/**
	 * Configuration for the Move Pilot. Sets angular and linear speed and acceleration.
	 * @param pilot the Move Pilot to be configured
	 * @return The configured Move Pilot object
	 */
	public MovePilot pilotConfig(MovePilot pilot) {
		pilot.setLinearSpeed(100);
		pilot.setLinearAcceleration(80);
		pilot.setAngularSpeed(90);
		pilot.setAngularAcceleration(50);
		return pilot;
	}
	
}
