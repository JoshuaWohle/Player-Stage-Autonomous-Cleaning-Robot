package map;

import java.io.Serializable;
import java.util.Date;

import algorithms.Metric;
import robot.Orientation;

public class MapTile implements Comparable<MapTile>, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2242891493332829492L;


	private Coordinate coordinates = null;
	private MapTileType type = MapTileType.NOT_SET; 
	private boolean clean = true;
	private Metric metric = null;
	private Date dateCleaned = null;
	
	public void resetMetric(){
		metric = new Metric();
	}

	public void setCoordinates(int x, int y){
		coordinates = new Coordinate(x, y);
	}

	public void setCoordinates(Coordinate coordinates){
		this.coordinates = coordinates;
	}
	
	public Coordinate getCoordinates(){
		return coordinates;
	}
	
	public int getCoordinateX(){
		return coordinates.getX();
	}
	
	public int getCoordinateY(){
		return coordinates.getY();
	}
	

	public MapTileType getType() {
		return type;
	}

	public void setType(MapTileType type) {
		this.type = type;
	}
	
	public synchronized boolean isCleaned(){
		if(dateCleaned == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public Metric getMetric(){
		return metric;
	}
	
	
	public synchronized void setCleaned(Date d){
		this.dateCleaned = d;
	}
	
	public synchronized Date whenCleaned(){
		return dateCleaned;
	}
	
	
	/**
	 * Creates an empty map tile (nothing set yet)
	 */
	public MapTile() {
		this(MapTileType.NOT_SET);
	}
	
	/**
	 * Creates a new map tile, setting it's type
	 * @param tileType
	 */
	public MapTile(MapTileType tileType) {
		this.setType(tileType);
		this.setToBeCleaned(true);
		this.resetMetric();
	}
	
	/**
	 * Creates a new map type, sets it's type and coordinates
	 * @param tileType
	 * @param x
	 * @param y
	 */
	public MapTile(MapTileType tileType, int x, int y) {
		this(tileType);
		this.setCoordinates(x, y);
	}
	
	/**
	 * Checks if the current tile is passable or not
	 * @return true if passable, false if not
	 */
	public boolean isPassable() {
		if(this.type == MapTileType.EMPTY)
			return true;
		else 
			return false;
	}
	
	public boolean isCleanable(){
		return this.isPassable()  && !this.isCleaned() && this.isToBeCleaned();
	}
	
	public String toString() {
		if(getType() == MapTileType.EMPTY && isCleaned()){
			//return "C";
			return Integer.toString(this.getType().ordinal());
		}
		else{
			return Integer.toString(this.getType().ordinal());
		}
	}

	public int compareDirectionTo(MapTile tile) {
		Coordinate currentCoordinates = getCoordinates();
		Coordinate targetCoordinates = tile.getCoordinates();
		
		if(targetCoordinates.getX() > currentCoordinates.getX()){
			return Orientation.EAST;
		}
		else if(targetCoordinates.getX() < currentCoordinates.getX()){
			return Orientation.WEST;
		}
		else{
			if(targetCoordinates.getY() > currentCoordinates.getY()){
				return Orientation.SOUTH;
			}
			else if(targetCoordinates.getY() < currentCoordinates.getY()){
				return Orientation.NORTH;
			}
			else{
				return Orientation.CURRENT;
			}
		}
	}

	
	public int compareTo(MapTile o) {
		return this.metric.compareTo(o.getMetric());
	}

	public synchronized boolean isToBeCleaned() {
		return clean;
	}

	public synchronized void setToBeCleaned(boolean clean) {
		this.clean = clean;
	}
	
	
	public MapTile clone() {
		MapTile temp = new MapTile();
		temp.setType(this.getType());
		temp.setCleaned( this.whenCleaned());
		temp.setToBeCleaned(this.isToBeCleaned());
		temp.setCoordinates(this.getCoordinates());
		
		return temp;
	}
}