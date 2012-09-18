package algorithms;

import java.util.LinkedList;

import map.Coordinate;
import map.Map;

/**
 * The ExploringAlgorithm interface provides a method to determine a collection
 * of tiles that can be explored further to determine new areas of in the map.
 * An object implementing this must then attach itself to the {@link Controller}
 * using the {@link Controller.addExploringAlgorithm} method
 * 
 * @author mayank
 * 
 */
public interface ExploringAlgorithm {
	
	/**
	 * A method to provide the tiles that can be explored further to determine new areas of the map
	 * @param map the map to use
	 * @return a list of tiles that can be explored further.
	 */
	public LinkedList<Coordinate> getTilesToExplore(Map map);
}
