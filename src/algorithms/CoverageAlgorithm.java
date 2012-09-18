package algorithms;

import map.Map;
import map.MapTile;

/**
 * The CoverageAlgorithm interface provides a method to determine the next best
 * tile to cover in a map relative to robot's orientation The implementer of
 * this interface may wish to privately maintain a {@link List} of tiles that
 * have been visited and no longer need to be visited or tiles that still need
 * to be visited. An object implementing this must then attach itself to the
 * {@link Controller} using the {@link Controller.addCoverageAlgorithm} method
 * 
 * @see MapTile
 * 
 * @author mayank
 * 
 */
public interface CoverageAlgorithm {

	/**
	 * A method that implements some coverage algorithm that is able to cover
	 * all coverable tiles on the map. This method need not instruct the robot
	 * to move but only return the next best tile for the robot to cover.
	 * 
	 * @param map
	 *            the map to cover
	 * @param robotOrientation
	 *            the current orientation of the {@link robot.Orientation}
	 * @return the next tile the robot should cover next
	 */
	public MapTile getNextTileToCover(Map map, int robotOrientation);

	/**
	 * This resets the internal {@link List} of tiles that are either visited or unvisited
	 */
	public void resetAlgorithm();
}
