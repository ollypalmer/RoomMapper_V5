package mapping;

import java.util.ArrayList;

import lejos.robotics.navigation.Pose;

/**
 * Mapping class is responsible for holding the map data. This is a singleton class as the map
 * is required by many other classes, yet only requires one instance to exist at any one time.
 * Contains methods for recording location and landmark data.
 * @author Oliver Palmer
 *
 */
public class Mapping {

	private static Mapping instance = null;
	private ArrayList<Observation> mapData;
	
	/**
	 * Private constructor is a requirement for a singleton class
	 */
	private Mapping() {	
		mapData = new ArrayList<Observation>();
	}
	
	/**
	 * Synchronized method to return the instance of the Mapping class, if no instance
	 * exists one will be created.
	 * @return the single instance of the Mapping class
	 */
	public static synchronized Mapping getInstance() {
		if (instance == null) {
			instance = new Mapping();
		}
		return instance;
	}
	
	/**
	 * Getter for the map data list
	 * @return
	 */
	public ArrayList<Observation> getMap(){
		return this.mapData;
	}
	
	/**
	 * Adds positional data to the map log
	 * @param pose - position object from the Odometry Pose Provider
	 */
	public void logPosition(Pose pose) {
		mapData.add(new Observation(pose.getX(), pose.getY(), pose.getHeading(), 0));
	}
	
	/**
	 * Adds landmark data to the map log. Calculates the landmark position using the EV3's recorded location
	 * and the ultrasonic sensors distance reading.
	 * @param pose - position object from the Odometry Pose Provider
	 * @param distance - distance recorded by the ultrasonic sensor
	 */
	public void logLandmark(Pose pose, float distance) {
		float x = pose.getX();
		float y = pose.getY();
		float heading = pose.getHeading();
		
		x += (float)Math.cos(Math.toRadians(heading)) * distance;
		y += (float)Math.sin(Math.toRadians(heading)) * distance;
		mapData.add(new Observation(x, y, pose.getHeading(), 1));
	}
}
