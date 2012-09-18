package robot;

/**
 * A listener interface for receiving robot sonar events from the robot. Any
 * object interested in listening for sensor reading from the robot must
 * implement this interface and all its methods. Sonar events include the new
 * sensor readings and the robot's current orientation. The implementing object
 * must then attach itself with the {@link Robot} using the
 * {@link Robot.addSonarListener} method.
 * 
 * @author mayank
 * 
 */
public interface SonarListener {

	/**
	 * called when sensor readings in {@link Robot} have been refreshed
	 * @param newSonar
	 * @param orientation
	 */
	public void updateSonar(double[] newSonar, int orientation);

}
