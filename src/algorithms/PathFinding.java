package algorithms;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.PriorityQueue;



import map.Map;
import map.MapTile;

/**
 * A default implementation of the path finding algorithm
 * @author mayank
 *
 */
public class PathFinding implements PathFindingAlgorithm{
	
	/**
	 * A method to determine a diagonal distance between two tiles 
	 * @param start the start {@link MapTile}
	 * @param end the end {@link MapTile}
	 * @return the distance between the two tiles
	 */
	public static double getDistance(MapTile start, MapTile end) {
		double differenceY = end.getCoordinateY() - start.getCoordinateY();
		double differenceX = end.getCoordinateX() - start.getCoordinateX();
		return Math.sqrt((differenceX * differenceX)
				+ (differenceY * differenceY));
	}
	
	private static void resetMetric(LinkedList<MapTile> tilesUsed) {
		while (!tilesUsed.isEmpty()) {
			tilesUsed.remove().resetMetric();
		}
	}	
	
	
	public LinkedList<Integer> getPath(Map map, MapTile start, MapTile end) throws IndexOutOfBoundsException {
		
		try{
			if(!start.isPassable() || !end.isPassable()){
				return new LinkedList<Integer>();
			}
		}
		catch(NullPointerException ex){
			return new LinkedList<Integer>();
		}
		
		ArrayList<MapTile> visited = new ArrayList<MapTile>();
		PriorityQueue<MapTile> unvisited = new PriorityQueue<MapTile>();
		LinkedList<Integer> shortestPath = new LinkedList<Integer>();
		LinkedList<MapTile> tilesUsed = new LinkedList<MapTile>();
		MapTile current = null;
		boolean pathFound = false;
		start.getMetric().setPathSize(0);
		start.getMetric().setBestNeighbour(start);

		unvisited.add(start);
		tilesUsed.add(start);

		while (!unvisited.isEmpty() && !pathFound) {
			current = unvisited.remove();
			if (current == end) {
				MapTile bestNeighbour = null;
				while (current != start) {
					bestNeighbour = current.getMetric().getBestNeighbour();
					shortestPath.add(0,	new Integer(bestNeighbour.compareDirectionTo(current)));
					current = bestNeighbour;
				}
				pathFound = true;
			} else {
				MapTile[] neighbours = map.getNeighbouringTiles(current);
				for (int i = 0; i < neighbours.length; i++) {
					try{
						if (neighbours[i].isPassable()&& !visited.contains(neighbours[i])) {
							
							if (neighbours[i].getMetric().compareTo(current.getMetric()) > 0) {
								neighbours[i].getMetric().setBestNeighbour(current);
								neighbours[i].getMetric().setPathSize(current.getMetric().getPathSize() + 1);
								tilesUsed.add(neighbours[i]);
							}
							if (!unvisited.contains(neighbours[i]) && neighbours[i].isPassable()) {
								unvisited.add(neighbours[i]);
							}
						}
					}
					catch(NullPointerException e){
						e.printStackTrace();}
					}
				visited.add(current);
			}
		}
		resetMetric(tilesUsed);
		return shortestPath;
	}
}