package algorithms;

import java.util.LinkedList;

import map.Coordinate;
import map.Map;
import map.MapTile;
import map.MapTileType;

/**
 * A default implementation of the ExploringAlgorithm
 * @author mayank
 *
 */
public class Exploring implements ExploringAlgorithm{
	
	
	public LinkedList<Coordinate> getTilesToExplore(Map map){
		
		LinkedList<MapTile> decidedTiles = new LinkedList<MapTile>();
		LinkedList<MapTile> undecidedTiles = new LinkedList<MapTile>();
		LinkedList<Coordinate> tilesToExplore = new LinkedList<Coordinate>();
		MapTile current = null;
		undecidedTiles.add(map.getTile(map.getRobotPosition()));
		
		while(!undecidedTiles.isEmpty()){
			current = undecidedTiles.remove();
			if(current.getType() == MapTileType.NOT_SET){
				tilesToExplore.add(current.getCoordinates());
			}
			
			MapTile[] neighbours = map.getNeighbouringTiles(current);
			for(int i = 0; i < neighbours.length; i++){
				try{
					if(neighbours[i].getType() != MapTileType.STATIONARY && !decidedTiles.contains(neighbours[i])){
						if(!undecidedTiles.contains(neighbours[i])){
								undecidedTiles.add(neighbours[i]);
						}
					}
				}
				catch(NullPointerException e){
				}
			}
			decidedTiles.add(current);
		}
	
		return tilesToExplore;	
	}	
}






















//package algorithms;
//
//import java.util.LinkedList;
//
//import collisionDetection.CollisionDetector;
//import collisionDetection.CollisionListener;
//import map.*;
//import robot.*;
//
///**
// * A class to handle robot exploration
// * 
// * @author Mayank Sharma
// */
//public class Exploring extends PathFinding implements CollisionListener{
//	
//	protected Robot robot = null;
//	private boolean collided = false;
//
//	/**
//	 * To construct an Explorer when the map is known
//	 * @param map the known map
//	 * @param robot the exploring robot
//	 */
//	protected Exploring(Map map, Robot robot){
//		super(map);
//		this.robot = robot;
//	}
//	
//	public void returnToStart(){
//		
//	}
//	
//	public boolean exploreToTile(MapTile tile){
//		LinkedList<Integer> path;
//		try{
//			 path = getPath(floorplan.getRobotPosition(), tile);
//		}
//		catch(IndexOutOfBoundsException e){
//			return false;
//		}
//		
//		while(!path.isEmpty()){
//			if(collided){
//				//deal with collision
//			}
//			else{
//				robot.moveToDirection(path.remove(0).intValue(), 1);
//			}
//		}
//		return tile.compareTo(floorplan.getRobotPosition()) == 0;
//	}
//
//	
//	public void obstacleInDirection(int direction, int distance) {
//		
//		
//	}
//
//	
//	public void collided() {
//		collided = true;
//		
//	}
//	
//	
//	
//	private static boolean wonderControl = true;
//	
//	public static void stopWondering(){
//		wonderControl = false;
//		
//	}
//	
//	public static void wonder(Robot r, Map m){
//		
//		while(wonderControl){
//			
//			
//			
//		}
//		
//	}
//	
//	
//	
//	
//}