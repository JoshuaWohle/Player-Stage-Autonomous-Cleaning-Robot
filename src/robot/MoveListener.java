package robot;

import map.Coordinate;

/**
 * A listener interface for receiving move events from the robot. Any object
 * interested in listening for move events must implement this interface and all
 * its methods. Move events include the direction the robot has moved and new
 * sensor readings after the move The implementing object must then attach
 * itself with the {@link Robot} using the {@link Robot.addMoveListener} method.
 * 
 * @author mayank
 * @see Map
 */
public interface MoveListener {
	
	public void directionMoved(int direction);
	
	public void addSensorReading(double explorerFront, double fineFrontLeft, double fineFrontRight, 
			double explorerRight, double fineRightBottom, double fineRightTop, 
			double explorerLeft, double fineLeftBottom, double fineLeftTop, int orientation);
}
