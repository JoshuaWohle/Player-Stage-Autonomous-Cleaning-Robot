package robot;

/**
 * A listener interface for receiving statistical data from the robot. Any
 * object interested in listening for such statistical data must implement this
 * interface and all its methods. Statistical data from the robot includes new
 * direction the robot moves / turns and whether the robot has stalled
 * 
 * @author mayank
 * 
 */
public interface RobotStatsListener {

	/**
	 * Called when the robot has moved its direction (orientation)
	 * @param newOrientation the new direction the robot is facing
	 */
	public void directionChanged(int newOrientation);

	/**
	 * Called when the robot motors have stalled
	 */
	public void stalled();
	
	public void moved();

}