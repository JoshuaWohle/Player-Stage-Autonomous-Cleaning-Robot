package map;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import robot.MoveListener;
import robot.Robot;
import robot.Orientation;
import utilities.MapFileHandler;

/**
 * @author Joshua Wöhle
 * This class will provide us with some simple helper methods in relation to the map we are generating
 */
public class Map implements Serializable, MoveListener, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7622432914927444692L;
	//
	// Declare default variables
	private final int START_COLS = 10;
	private final int START_ROWS = 10;
	public static final int SQUARES_PER_SONAR = 25; // This means we will be dividing the main sonars in 25 to represent a square
	public static final int MAX_SONAR_VALUE = 10; // Maximum range of our sensors
	public static final Coordinate START_POSITION = new Coordinate(5, 5);
	private Coordinate chargingPosition = START_POSITION;
	public static final double PER_SQUARE_SIZE = MAX_SONAR_VALUE / (double)SQUARES_PER_SONAR;
	
	private long id = 0;
	private int numberCols = START_COLS; 
	private int numberRows = START_ROWS;
	private String name = "";
	private Coordinate robotPosition = START_POSITION;
	private ArrayList<MapStatsListener> mapStatsListeners = null;
	private ArrayList<MapListener> mapListeners = null;
	
	int totalCleaned = 0;
	int totalEmpty = 0;
	
	private MapTile[][] map = new MapTile[numberRows][numberCols]; 
	
	public int getNumberCols() {
		return numberCols;
	}

	private void setNumberCols(int numberCols) {
		this.numberCols = numberCols;
	}

	public int getNumberRows() {
		return numberRows;
	}

	private void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MapTile[][] getMap() {
		return this.map;
	}

	/**
	 * Takes an arraylist of maptiles and creates a new map out of it
	 * @param newMap - the array of MapTiles[][]
	 */
	public void setMap(MapTile[][] newMap) {
		this.map = newMap;
		this.setNumberCols(map.length);
		this.setNumberRows(map[0].length);
		
		totalEmpty = 0;
		totalCleaned = 0;
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				map[i][j].setCleaned(null);
				if(map[i][j].getType() == MapTileType.EMPTY){
					totalEmpty++;
				}
			}
		}
		
		
		
		for(int i = 0; i < mapListeners.size(); i++){
			mapListeners.get(i).updatedMap(this);
		}
		
		for(int i = 0; i < mapStatsListeners.size(); i++){
			mapStatsListeners.get(i).newMap();
		}
	}
	
	/**
	 * Creates an empty map object and initiates all squares to "NOT_SET"
	 */
	public Map() {
		mapStatsListeners = new ArrayList<MapStatsListener>();
		mapListeners = new ArrayList<MapListener>();
		initMap();
		id = System.currentTimeMillis();
	}
	
	/**
	 * Creates a new map from a text file of numbers (every number representing the type of a tile)
	 * @param mapToLoad - the path to the map to load (including the filename)
	 */
	public Map(String mapToLoad) {
		mapStatsListeners = new ArrayList<MapStatsListener>();
		mapListeners = new ArrayList<MapListener>();
		loadMap(mapToLoad);
		id = System.currentTimeMillis();
	}
	
	/**
	 * Gets a map tile from coordinate x and y
	 * @param x - the x coordinate of the tile you want
	 * @param y - the y coordinate of the tile you want
	 * @return MapTile - the tile you want
	 */
	public synchronized MapTile getTile(Coordinate coordinates){
		int x = coordinates.getX();
		int y = coordinates.getY();
		if(!this.isOutOfBounds(x, y))
				return map[x][y];
		else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	/**
	 * This method simply tells you if a certain area of the map is passable
	 * @param x - the x coordinate of the map tile
	 * @param y - the y coordinate of the map tile
	 * @return true if passable, false if not
	 */
	public synchronized boolean isPassable(Coordinate coordinates) {
		int x = coordinates.getX();
		int y = coordinates.getY();
		if(this.isOutOfBounds(x, y))
			throw new IndexOutOfBoundsException();
		else
			return this.getTile(coordinates).isPassable();
	}
	
	/**
	 * Returns the total number of passable squares on the map
	 * @return number of passable squares
	 */
	public int getNoPassableTiles() {
		int tiles = 0; 
		
		for(int i = 0; i < numberCols; i++) {
			for(int j = 0; j < numberCols; j++) {
				if(map[i][j].isPassable())
					tiles++;
			}
		}
		return tiles;
	}
	
	
	/* (non-Javadoc)
	 * @see robot.MoveListener#addSensorReading(double, double, double, double, double, double, double, double, double, int)
	 * This method takes in all of the current sensor readings
	 */
	public synchronized void addSensorReading(double explorerFront, double fineFrontLeft, double fineFrontRight, 
			double explorerRight, double fineRightBottom, double fineRightTop, 
			double explorerLeft, double fineLeftBottom, double fineLeftTop, int orientation) {
		
		int squaresFreeInFront = (int)Math.floor(explorerFront * SQUARES_PER_SONAR/MAX_SONAR_VALUE);
		int squaresFreeToRight = (int)Math.floor(explorerRight * SQUARES_PER_SONAR/MAX_SONAR_VALUE);
		int squaresFreeToLeft = (int)Math.floor(explorerLeft * SQUARES_PER_SONAR/MAX_SONAR_VALUE);
		
		// These three checks are to make sure that the tile we're setting to "empty" is big enough for the robot to pass through
		if(fineFrontLeft != fineFrontRight 
			|| fineFrontLeft < Robot.MAX_FINE_FRONT_LEFT) {
			squaresFreeInFront = (int)Math.floor(Math.min(fineFrontLeft, fineFrontRight) * SQUARES_PER_SONAR/MAX_SONAR_VALUE);
		}
		
		if(fineRightBottom != fineRightTop 
			|| fineRightBottom < Robot.MAX_FINE_RIGHT_BACK) {
			squaresFreeToRight = (int)Math.floor(Math.min(fineRightBottom, fineRightTop) * SQUARES_PER_SONAR/MAX_SONAR_VALUE);
		}
		
		if(fineLeftBottom != fineLeftTop 
			|| fineLeftBottom < Robot.MAX_FINE_LEFT_BACK) {
			squaresFreeToLeft = (int)Math.floor(Math.min(fineLeftBottom, fineLeftTop) * SQUARES_PER_SONAR/MAX_SONAR_VALUE);	
		}
		
		
		switch(orientation) {
			case Orientation.NORTH :
				updateMapTilesAsEmpty(squaresFreeInFront, Orientation.NORTH);
				updateMapTilesAsEmpty(squaresFreeToRight, Orientation.EAST);
				updateMapTilesAsEmpty(squaresFreeToLeft, Orientation.WEST);
				break;
			case Orientation.EAST :
				updateMapTilesAsEmpty(squaresFreeInFront, Orientation.EAST);
				updateMapTilesAsEmpty(squaresFreeToRight, Orientation.SOUTH);
				updateMapTilesAsEmpty(squaresFreeToLeft, Orientation.NORTH);
				break;
			case Orientation.SOUTH :
				updateMapTilesAsEmpty(squaresFreeInFront, Orientation.SOUTH);
				updateMapTilesAsEmpty(squaresFreeToRight, Orientation.WEST);
				updateMapTilesAsEmpty(squaresFreeToLeft, Orientation.EAST);
				break;
			case Orientation.WEST :
				updateMapTilesAsEmpty(squaresFreeInFront, Orientation.WEST);
				updateMapTilesAsEmpty(squaresFreeToRight, Orientation.NORTH);
				updateMapTilesAsEmpty(squaresFreeToLeft, Orientation.SOUTH);
				break;
			default : 
				throw new IndexOutOfBoundsException();
		}
		
		
		//saveMap("/home/joshua/last_map.txt");
	}
	
	/**
	 * Takes care of setting a specified number of tiles to "EMPTY" in bulk and to "STATIONARY" at the end of the sensor reading
	 * @param position
	 * @param numberOfTilesToUpdate
	 * @param direction
	 * @param wall - should a wall be drawn up at the end or not
	 */
	private void updateMapTilesAsEmpty(int numberOfTilesToUpdate, int direction) {
		// These two coordinates are used to set the "stationary object" at the back of the sensor reading
		int backX = 0;
		int backY = 0;
		boolean wall = false;
		
		if(numberOfTilesToUpdate < Robot.MAX_EXPLORER_FRONT*SQUARES_PER_SONAR/MAX_SONAR_VALUE)
			wall = true;
		
		switch(direction) {
			case Orientation.NORTH :
				for(int i = 1; i <= numberOfTilesToUpdate; i++) {
					int posX = robotPosition.getX();
					int posY = robotPosition.getY();
					int y = posY-i;
					
					this.updateMapTile(MapTileType.EMPTY, posX, y);
				}
				if(wall) {
					int posX = robotPosition.getX();
					int posY = robotPosition.getY();
					backX = posX;
					backY = posY-numberOfTilesToUpdate-1;
				}
				break;
			case Orientation.EAST :
				for(int i = 1; i <= numberOfTilesToUpdate; i++) {
					int posX = robotPosition.getX();
					int posY = robotPosition.getY();
					int x = posX+i;
					this.updateMapTile(MapTileType.EMPTY, x, posY);
				}
				if(wall) {
					int posX = robotPosition.getX();
					int posY = robotPosition.getY();
					backX = posX+numberOfTilesToUpdate+1;
					backY = posY;
				}
				break;
			case Orientation.SOUTH :
				for(int i = 1; i <= numberOfTilesToUpdate; i++) {
					int posX = robotPosition.getX();
					int posY = robotPosition.getY();
					int y = posY+i;
					this.updateMapTile(MapTileType.EMPTY, posX, y);
				}
				if(wall) {
					int posX = robotPosition.getX();
					int posY = robotPosition.getY();
					backX = posX;
					backY = posY+numberOfTilesToUpdate+1;
				}
				break;
			case Orientation.WEST : 
				for(int i = 1; i <= numberOfTilesToUpdate; i++) {
					int posX = robotPosition.getX();
					int posY = robotPosition.getY();
					int x = posX-i;
					this.updateMapTile(MapTileType.EMPTY, x, posY);
				}
				if(wall) {
					int posX = robotPosition.getX();
					int posY = robotPosition.getY();
					backX = posX-numberOfTilesToUpdate-1;
					backY = posY;
				}
				break;
			default :
				throw new IndexOutOfBoundsException();
		}
		if(wall) {
			this.updateMapTile(MapTileType.STATIONARY, backX, backY);
		}
	}
	
	/**
	 * Returns the last known position of the robot
	 * @return the MapTile of the robot's current position
	 */
	public Coordinate getRobotPosition(){
		return robotPosition;
	}
	
	/**
	 * Sets a certain tile to "cleaned"
	 * @param coordinates - the coordinates of the tile to be cleaned
	 * @param dateCleaned - the date at which it has been cleaned
	 */
	public void cleanTile(Coordinate coordinates, Date dateCleaned){
		MapTile t = getTile(coordinates);
		t.setCleaned(dateCleaned);
		if(t.getType() == MapTileType.EMPTY && t.isCleaned()){
			totalCleaned++;
		}
		for(int i = 0; i < mapListeners.size(); i++){
			mapListeners.get(i).updatedMap(this.getTile(coordinates));
		}
		
		for(int i = 0; i < mapStatsListeners.size(); i++){
			mapStatsListeners.get(i).tileCleaned(totalCleaned, totalEmpty);
		}
		
	}
	
	
	/**
	 * Returns the tiles next to the specified Tiles (EAST, NORTH, WEST, SOUTH)
	 * @param mapTile
	 * @return an array of MapTiles
	 */
	public MapTile[] getNeighbouringTiles(MapTile mapTile) throws NullPointerException{
		/*
		 * [0]=EAST, [1]=NORTH, [2]=WEST, [3]=SOUTH 
		 */
		MapTile[] neighbours = new MapTile[4];
		try{
			neighbours[NeighbourTilePosition.EAST.ordinal()] = getTile(new Coordinate(mapTile.getCoordinateX() + 1, mapTile.getCoordinateY()));
		}
		catch(IndexOutOfBoundsException e){
			neighbours[NeighbourTilePosition.EAST.ordinal()] = null;
			
		}
		
		try{
			neighbours[NeighbourTilePosition.NORTH.ordinal()] = getTile(new Coordinate(mapTile.getCoordinateX(), mapTile.getCoordinateY() -1));
		}
		catch (IndexOutOfBoundsException e) {
			neighbours[NeighbourTilePosition.NORTH.ordinal()] = null;
			
		}
		
		try{
			neighbours[NeighbourTilePosition.WEST.ordinal()] = getTile(new Coordinate(mapTile.getCoordinateX() - 1, mapTile.getCoordinateY()));
		}
		catch(IndexOutOfBoundsException e){
			neighbours[NeighbourTilePosition.WEST.ordinal()] = null;
			
		}
		try{
			neighbours[NeighbourTilePosition.SOUTH.ordinal()] = getTile(new Coordinate(mapTile.getCoordinateX(), mapTile.getCoordinateY() + 1));
		}
		catch(IndexOutOfBoundsException e){
			neighbours[NeighbourTilePosition.SOUTH.ordinal()] = null;
			
		}
		return neighbours;
	}
	
	/**
	 * Initiates the map for the very first tiles
	 */
	private void initMap() {
		for(int i = 0; i < this.getNumberCols(); i++) {
			for(int j = 0; j < this.getNumberRows(); j++) {
				this.map[i][j] = new MapTile(MapTileType.NOT_SET, i, j);
			}
		}
		this.setMapTile(new MapTile(MapTileType.EMPTY, robotPosition.getX(), robotPosition.getY()), robotPosition.getX(), robotPosition.getY());
		this.getTile(START_POSITION).setType(MapTileType.EMPTY);
	}
	
	/**
	 * Sets a new tile on the map at the right coordinates
	 * @param tile - the tile to set
	 * @param x - x coordinate of the tile
	 * @param y - y coordinate of the tile
	 */
	private void setMapTile(MapTile tile, int x, int y) {
		if(isOutOfBounds(x, y)) {
			doubleMapSize(x, y);
			if(x < 0)
				x = getNumberCols() / 2 - 1;
			if(y < 0)
				y = getNumberRows() / 2 - 1;
			
			setMapTile(tile, x, y);
		} else {
			tile.setCoordinates(x, y);
			this.map[x][y] = tile;
		}
		
		for(int i = 0; i < mapListeners.size(); i++){
			mapListeners.get(i).updatedMap(map[x][y]);
		}
	}
	
	/**
	 * Sets a new tile on the map at the right coordinates
	 * @param tile - the tile to set
	 * @param x - x coordinate of the tile
	 * @param y - y coordinate of the tile
	 */
	private void updateMapTile(MapTileType tileType, int x, int y) {
		if(isOutOfBounds(x, y)) {
			doubleMapSize(x, y);
			if(x < 0)
				x = getNumberCols() / 2 - 1;
			if(y < 0)
				y = getNumberRows() / 2 - 1;
			
			updateMapTile(tileType, x, y);
		} else {
			if(map[x][y] == null){
				setMapTile(new MapTile(tileType, x, y), x, y);
				if(tileType == MapTileType.EMPTY){
					this.totalEmpty++;
				}
			}
			else {
				// Setting Moving obstacles (if empty before and now NOT, then it means it has been moving !)
				MapTileType oldType = map[x][y].getType();
				if(map[x][y].getType() == MapTileType.EMPTY && tileType == MapTileType.STATIONARY)
					tileType = MapTileType.MOVING;
				
				if(map[x][y].getType() != tileType
					|| map[x][y].getCoordinateX() != x
					|| map[x][y].getCoordinateY() != y) {
					this.map[x][y].setType(tileType);
					this.map[x][y].setCoordinates(x, y);
					
					for(int i = 0; i < mapListeners.size(); i++){
						mapListeners.get(i).updatedMap(map[x][y]);
					}
					
					if(oldType == MapTileType.EMPTY && (map[x][y].getType() == MapTileType.STATIONARY || map[x][y].getType() == MapTileType.MOVING || map[x][y].getType() == MapTileType.NOT_SET)){
						if(totalEmpty > 0){
							totalEmpty--;
						}
					}
					else if((oldType == MapTileType.STATIONARY || oldType == MapTileType.MOVING || oldType == MapTileType.NOT_SET) && map[x][y].getType() == MapTileType.EMPTY){
						totalEmpty++;
					}
					
				}
			}
		}
		
	}
	
	/**
	 * Simply doubles the size of the current map array. It only
	 * doubles the size to the side it has to (x or y, not both)
	 */
	private void doubleMapSize(int x, int y) {
		
		
		int oldCols = this.getNumberCols();
		int oldRows = this.getNumberRows();
		int newCols = oldCols;
		int newRows = oldRows;
		if(x >= oldCols || x < 0)
			newCols = oldCols*2;
		if(y >= oldRows || y < 0)
			newRows = oldRows*2;
		
		MapTile[][] temp = new MapTile[newCols][newRows];
		
		for(int i = 0; i < newCols; i++) {
			for(int j = 0; j < newRows; j++) {
				temp[i][j] = new MapTile(MapTileType.NOT_SET, i, j);
			}
		}
		
		int startX;
		int endX;
		int startY;
		int endY;
		
		if(x < 0 && y >= 0) {
			startX = oldCols; 
			endX = newCols;
			startY = 0; 
			endY = oldRows;
		} else if (x >= 0 && y < 0) {
			startX = 0; 
			endX = oldCols;
			startY = oldRows; 
			endY = newRows;
		} else if (x < 0 && y < 0) {
			startX = oldCols; 
			endX = newCols;
			startY = oldRows; 
			endY = newRows;
		} else {
			startX = 0;
			endX = oldCols;
			startY = 0;
			endY = oldRows;
		}
		
		// Copy over the old map values, while maintaining them at their right positions
		for(int i = startX; i < endX; i++) {
			for(int j = startY; j < endY; j++) {
				temp[i][j] = this.map[i-startX][j-startY];
				temp[i][j].setCoordinates(i, j);
			}
		}
		
		this.map = temp;
		
		this.setNumberCols(newCols);
		this.setNumberRows(newRows);
		
		// set new position of the robot if the map coordinates changed
		int curPosX = robotPosition.getX();
		int curPosY = robotPosition.getY();
		int chargingPX = chargingPosition.getX();
		int chargingPY = chargingPosition.getY();
		
		
		if(x < 0) {
			curPosX = curPosX + getNumberCols()/2;
			chargingPX = chargingPX + getNumberCols()/2;
			
		}
		if(y < 0) {
			curPosY = curPosY + getNumberRows()/2;
			chargingPY = chargingPY + getNumberRows()/2;
			
		}
		setRobotPosition(new Coordinate(curPosX, curPosY));
		this.chargingPosition = new Coordinate(chargingPX, chargingPY);
		
		for(int i = 0; i < mapListeners.size(); i++){
			mapListeners.get(i).updatedMap(this);
		}
	}
	
	private boolean isOutOfBounds(int x, int y) {
		if(
			(x < 0 ||
			 x >= getNumberCols()) ||
			 y < 0 ||
			(y >= getNumberRows())
			)
			return true;
		else
			return false;
	}
	
	/**
	 * Sets a new position for the robot
	 * @param rpos - the position to set the robot to
	 */
	public void setRobotPosition(Coordinate rpos){
		this.robotPosition = rpos;
		for(int i = 0; i < mapListeners.size(); i++){
			mapListeners.get(i).directionMoved(this.getRobotPosition());
		}
	}
	
	/**
	 * Gets the position of the charging station within the map.
	 * It is assumed that the charging station is same as the robot position when an object of Map is first instantiated. 
	 * @return
	 */
	public Coordinate getChargingStation(){
		return chargingPosition;
	}
	
	public void setChargingStation(Coordinate c){
		chargingPosition = c;
	}
	
	/**
	 * This method will save the map to a text file
	 * 
	 */
	private void saveMap(String fileLocation) {
		try {
			MapFileHandler writer = new MapFileHandler(fileLocation, true);
			writer.writeMapFromArray(map, this.getNumberCols(), this.getNumberRows());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private void saveMapCoordinates(String fileLocation) {
		try {
			MapFileHandler writer = new MapFileHandler(fileLocation, true);
			writer.writeMapCoordinatesFromArray(map);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * This method takes the string pointing to the location of the file and then use
	 * that file to load it, if it contains a valid map
	 * @param String fileLocation - the location of the map you want to load 
	 */
	public void loadMap(String fileLocation) {
		try {
			MapFileHandler reader = new MapFileHandler(fileLocation, false);
			String[] row;
			try {
				int rowNumber = 0;
				while((row = reader.readLineChars()) != null) {
					for(int i = 0; i < row.length; i++) {
						this.setMapTile(new MapTile(MapTileType.values()[Integer.parseInt(row[i])], i, rowNumber), i, rowNumber);
					}
					rowNumber++;
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();

		} catch (IOException e) {
			
			e.printStackTrace();

		}
	}
	
	/**
	 * Sets the ID of the map
	 * @param id - the id to set
	 */
	public void setId(long id){
		this.id = id;
	}
	
	/**
	 * Gets the ID of the map
	 * @return the ID of the map
	 */
	public long getId(){
		return this.id;
	}
	
	/**
	 * This method is for debugging and development purposes at the moment, it simply prints out every single line of the map
	 */
	public void printMap() {	
		
		for(int i = 0; i < this.getNumberRows(); i++) {
			String row = " ";
			for(int j = 0; j < this.getNumberCols(); j++) {
				try {
					if(map[j][i].getCoordinates().compareTo(robotPosition) == 0){
						row = row + "X";
					}
					else if(map[j][i] != null)
						row = row + map[j][i].toString();
					else if(map[j][i] != null)
						row = row + "N";
					} 
				catch(NullPointerException e) {
					System.out.println(i + "-" + j + " has no coordinate");
				
					e.printStackTrace();
				}
					
			}
			System.out.println(row);
		}
		System.out.println("\n");
	}

	
	public void directionMoved(int direction) {

		if(direction == Orientation.EAST){
			robotPosition.setX(robotPosition.getX() + 1);
		}else if(direction == Orientation.WEST){
			robotPosition.setX(robotPosition.getX() - 1);
		}else if(direction == Orientation.NORTH){
			robotPosition.setY(robotPosition.getY() - 1);
		}else if(direction == Orientation.SOUTH){
			robotPosition.setY(robotPosition.getY() + 1);
		}
		
		for(int i = 0; i < mapListeners.size(); i++){
			mapListeners.get(i).directionMoved(robotPosition);
		}
	}
	
	
	public Map clone(){
		Map tempMap = new Map();
		MapTile[][] tempTiles = new MapTile[this.map.length][this.map[0].length];
		for(int i = 0; i < this.map.length; i++){
			for(int j = 0; j < this.map[0].length; j++){
				try{	
					tempTiles[i][j] = (MapTile)map[i][j].clone();
				}
				catch(NullPointerException e){
					tempTiles[i][j].setCoordinates(i, j);
					
					e.printStackTrace();
				}
			}
		}
		tempMap.setId(this.getId());
		tempMap.setMap(tempTiles);
		return tempMap;
	}
	
	public void addMapStatsListener(MapStatsListener ml){
		mapStatsListeners.add(ml);
	}
	
	public void removeMapStatsListener(MapStatsListener ml){
		mapStatsListeners.remove(ml);
	}
	
	public void addMapListener(MapListener ml){
		mapListeners.add(ml);
		ml.updatedMap(this);
		ml.directionMoved(robotPosition);
	}
	
	public void removeMapListener(MapListener ml){
		mapListeners.remove(ml);
	}
}