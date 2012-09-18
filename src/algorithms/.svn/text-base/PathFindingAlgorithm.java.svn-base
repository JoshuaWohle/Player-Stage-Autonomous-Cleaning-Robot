package algorithms;

import java.util.LinkedList;

import map.Map;
import map.MapTile;

/**
 * An interface that provides a method to determine the a path from one tile to
 * another within a map. An object implementing this must then attach itself to the
 * {@link Controller} using the {@link Controller.addPathFindingAlgorithm} method
 * 
 * @author mayank
 */
public interface PathFindingAlgorithm {
	/**
	 * A method to that finds a path from one tile to another within a map.
	 * 
	 * @param map map containing the two tiles
	 * @param start the start {@link MapTile}
	 * @param end the end {@link MapTile}
	 * @return a list of Integers determining the turns the robot should take in
	 *         order to reach from start to end.
	 */
	public LinkedList<Integer> getPath(Map map, MapTile start, MapTile end);

}
