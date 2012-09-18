package algorithms;

import java.util.EmptyStackException;
import java.util.Stack;

import map.Coordinate;
import map.Map;
import map.MapTile;
import map.NeighbourTilePosition;
import robot.Orientation;


/**
 * A default implementation of the Coverage algorithm
 * 
 * @author mayank
 *
 */
public class Coverage implements CoverageAlgorithm{
	
	private final Stack<MapTile>uncleanedTiles = new Stack<MapTile>();
	
	private static MapTile[] refactorTiles(MapTile[] neighbouringTiles, int robotOrientation) {
		
		MapTile[] refactorted = new MapTile[neighbouringTiles.length];
		
		switch(robotOrientation){
		case Orientation.EAST:
			refactorted = neighbouringTiles.clone();
			break;
			
		case Orientation.WEST:
			
			refactorted[NeighbourTilePosition.EAST.ordinal()] = neighbouringTiles[NeighbourTilePosition.WEST.ordinal()];
			refactorted[NeighbourTilePosition.WEST.ordinal()] = neighbouringTiles[NeighbourTilePosition.EAST.ordinal()];
			refactorted[NeighbourTilePosition.NORTH.ordinal()] = neighbouringTiles[NeighbourTilePosition.NORTH.ordinal()];
			refactorted[NeighbourTilePosition.SOUTH.ordinal()] = neighbouringTiles[NeighbourTilePosition.SOUTH.ordinal()];
			break;
			
		case Orientation.NORTH:
			refactorted[NeighbourTilePosition.EAST.ordinal()] = neighbouringTiles[NeighbourTilePosition.NORTH.ordinal()];
			refactorted[NeighbourTilePosition.WEST.ordinal()] = neighbouringTiles[NeighbourTilePosition.WEST.ordinal()];
			refactorted[NeighbourTilePosition.NORTH.ordinal()] = neighbouringTiles[NeighbourTilePosition.EAST.ordinal()];
			refactorted[NeighbourTilePosition.SOUTH.ordinal()] = neighbouringTiles[NeighbourTilePosition.SOUTH.ordinal()];
			break;
			
		case Orientation.SOUTH:
			refactorted[NeighbourTilePosition.EAST.ordinal()] = neighbouringTiles[NeighbourTilePosition.SOUTH.ordinal()];
			refactorted[NeighbourTilePosition.WEST.ordinal()] = neighbouringTiles[NeighbourTilePosition.WEST.ordinal()];
			refactorted[NeighbourTilePosition.NORTH.ordinal()] = neighbouringTiles[NeighbourTilePosition.NORTH.ordinal()];
			refactorted[NeighbourTilePosition.SOUTH.ordinal()] = neighbouringTiles[NeighbourTilePosition.EAST.ordinal()];
			break;
		}
		return refactorted;
	}
	
	
	public MapTile getNextTileToCover(Map map, int robotOrientation){
		
		Coordinate robotCoordinates = map.getRobotPosition();
		
		MapTile[] neighboursTemp = Coverage.refactorTiles(map.getNeighbouringTiles(map.getTile(robotCoordinates)), robotOrientation);
		
		for(int i = neighboursTemp.length -1; i >= 0; i--){
			if(neighboursTemp[i].isCleanable()){
				uncleanedTiles.push(neighboursTemp[i]);
			}
		}
		
		try{
			while(!uncleanedTiles.peek().isCleanable()){
				uncleanedTiles.pop();
			}
		}
		catch(EmptyStackException e){
			e.printStackTrace();
			return null;
		}
		return uncleanedTiles.pop();
	}

	
	public void resetAlgorithm() {
		this.uncleanedTiles.removeAllElements();
		
	}
}