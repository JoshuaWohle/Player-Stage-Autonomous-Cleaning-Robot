package visualmap;


import images.ImgSource;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utilities.ActionCommandHelper;
import map.Coordinate;
import map.MapTile;
import map.MapTileType;


/**
 * @author Team Cyan
 *
 */
public class VisualMap2D extends VisualMap implements MouseListener, Runnable{
	
	private Image EMPTY_TILE_IMAGE = new ImageIcon(ImgSource.themePath + "/emptyTile.png").getImage();
	private Image WALL_TILE_IMAGE = new ImageIcon(ImgSource.themePath + "/wallTile.png").getImage();
	private Image NOT_SET_TILE_IMAGE = new ImageIcon(ImgSource.themePath + "/trans.png").getImage();
	private Image MOVING_TILE_IMAGE = new ImageIcon(ImgSource.themePath + "/movingTile.png").getImage();
	private Image TRANSPARENT_IMAGE = new ImageIcon(ImgSource.themePath + "/trans.png").getImage();
	private Image ROBOT_IMAGE = new ImageIcon(ImgSource.themePath + "/View2D.png").getImage();
	
	private VisualTile[][] visualMap = null;
	private BufferedImage bkgImage;
	private Coordinate lastRobotPosition = null;
	private boolean saved;
	ArrayList<Coordinate> selectedCoordinates = new ArrayList<Coordinate>();
	private Coordinate lastSelectedCoordinate = null;
	private boolean clickable = true;
	private boolean running;
	private Thread updateThread;
	
	/** 	 Draws background image
	 */
	public void paintComponent(Graphics graphics){
		super.paintComponent(graphics);
		((Graphics2D)graphics).drawImage(bkgImage, 0, 0, this);
	}
	
	
	/**
	 * Resets the selection on tiles
	 */
	
	public void resetSelection(){
		for(int i = 0; i < visualMap.length; i++){
			for(int j = 0; j < visualMap[i].length; j++){
				if(visualMap[i][j].isSelected()){
					visualMap[i][j].select();
				}
			}
		}
	}
	
	/**
	 * @param saved sets the value of saved in accordance to this boolean
	 */
	public void setSaved(boolean saved){
		this.saved = saved;
	}
	
	/**
	 * @return the boolean true
	 */
	public boolean isSaved(){
		return saved;
	}
	
	public Coordinate getRobotPosition() {
		return this.lastRobotPosition;
	}
	
	public VisualMap2D(){
		super();
		saved = false;
		try {
			bkgImage = ImageIO.read(new File(ImgSource.imgPath + "/mapPanelBackground.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		selectedCoordinates = new ArrayList<Coordinate>();
		drawMap();
		running = true;
		updateThread = new Thread(this);
		updateThread.start();
	}
	
	/**
	 * @param newMap - a new MapTile Array to be used that will be represented by the visual map
	 * Sets the map being used to newMap then draws the map
	 */
	
	public void redraw() {
		this.updateRobotPosition(this.getRobotPosition());
	}
	
	public synchronized void updateMap(MapTile[][] newMap){
		super.updateMap(newMap);
		this.setSaved(false);
		this.drawMap();

	}

	
	/**
	 * Removes all components from current frame
	 * finds number of tiles vertically and horizontally in array of map tiles
	 * Creates grid which will be same dimensions as array
	 * Creates appropriate number of VisualTiles to be used
	 * Iterates through MapTile array and changes VisualTiles using createTile Method
	 * Adds Mouse listener to all components
	 * Adds grid of Components to JFrame
	 */
	private synchronized void drawMap(){
		
		this.removeAll();
		int width = map.length;
		int height = map[0].length;
		
		JPanel mapPanel = new innerPanel(new GridLayout(height, width));
		visualMap = new VisualTile[width][height];
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
					visualMap[j][i] = createTile(map[j][i]);
					visualMap[j][i].setActionCommand(ActionCommandHelper.encodeActionCommand(new String[]{Integer.toString(j), Integer.toString(i)}));
					visualMap[j][i].addMouseListener(this);
					mapPanel.add(visualMap[j][i]);
				}
			}
		this.add(mapPanel);
	}
	
	/**
	 * @param tile - tile being checked, depending on attributes it will be set a corresponding image
	 * image is based on the tile type
	 * if tile is empty image is dependent on when tile was last cleaned
	 */
	
	public synchronized void updateTile(MapTile tile){
		this.setSaved(false);
		
		map[tile.getCoordinateX()][tile.getCoordinateY()] = tile;
		VisualTile visualTile = visualMap[tile.getCoordinateX()][tile.getCoordinateY()];
		
		if(tile.isCleaned()) {
			if(lastRobotPosition != null && lastRobotPosition.getX() == tile.getCoordinateX() && lastRobotPosition.getY() == tile.getCoordinateY())
				return;
			visualTile.setImage(getCleanedVisualTile(tile).getImage());
			return;
		}
		
		switch(tile.getType()) {
		case EMPTY:
			visualTile.setImage(EMPTY_TILE_IMAGE);
			break;
		case STATIONARY:
			visualTile.setImage(WALL_TILE_IMAGE);
			break;
		case MOVING:
			visualTile.setImage(MOVING_TILE_IMAGE);
			break;
		case NOT_SET:
			visualTile.setImage(NOT_SET_TILE_IMAGE);
			break;
		default:
			visualTile.setImage(TRANSPARENT_IMAGE);
			break;
		}
		
	}
	
	/**
	 * @param loc - location of robot
	 * updates the location of the robot on the Visual Map
	 * the image of the tile before the robot was on it was stored
	 * when the robot moves it returns to its previous image
	 */
	
	public synchronized void updateRobotPosition(Coordinate loc){
		this.setSaved(false);
		
		try{
			Coordinate tempPos = lastRobotPosition;
			lastRobotPosition = loc;
			if (tempPos != null)
				updateTile(map[tempPos.getX()][tempPos.getY()]);
			
			visualMap[loc.getX()][loc.getY()].setImage(ROBOT_IMAGE);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @param tile - a MapTile being checked to find attributes that will determine what image to place on it 
	 * @return a Tile that has been changed and can be used
	 */
	private VisualTile createTile(MapTile tile) {
		
		VisualTile visualTile = null;

		switch(tile.getType()) {
		case EMPTY:
			if(tile.isCleaned())
				visualTile = getCleanedVisualTile(tile);
			else
				visualTile = new VisualTile(ImgSource.themePath + "/emptyTile.png");
			break;
		case STATIONARY:
			visualTile = new VisualTile(ImgSource.themePath + "/wallTile.png");
			break;
		case MOVING:
			visualTile = new VisualTile(ImgSource.themePath + "/movingTile.png");
			break;
		case NOT_SET:
			visualTile = new VisualTile(ImgSource.themePath + "/trans.png");
			break;
		default:
			visualTile = new VisualTile(ImgSource.themePath + "/trans.png");
			break;
		}
		
		return visualTile;
	}
	
	/**
	 * Selects all the tiles in between two coordinates (rectangle)
	 * @param c1 - the first Coordinate
	 * @param c2 - the second Coordinate
	 */
	/**
	 * @param c1 top left tile of tiles the user wishes to clean
	 * @param c2 bottom right tile of the tiles the user wishes to clean
	 * finds all tiles between these two points and adds it to an array list
	 */
	public void selectTiles(Coordinate c1, Coordinate c2) { 
		Coordinate topLeft = null;
		Coordinate bottomRight = null;
		
		if((c1.getX() <= c2.getX())
			&& c1.getY() <= c2.getY()) {
			topLeft = c1;
			bottomRight = c2;
		} else if((c1.getX() >= c2.getX())
				&& c1.getY() <= c2.getY()) {
			topLeft = new Coordinate(c2.getX(), c1.getY());
			bottomRight = new Coordinate(c1.getX(), c2.getY());
		} else if ((c1.getX() >= c2.getX())
					&& c1.getY() >= c1.getY()) {
			topLeft = c2;
			bottomRight = c1;
		} else {
			topLeft = c1;
			bottomRight = c2;
		}
		
		for(int i = topLeft.getX(); i <= bottomRight.getX(); i++) {
			for(int j = topLeft.getY(); j <= bottomRight.getY(); j++) {
				System.out.println("Selected tile : " + i + "-" + j);
				visualMap[i][j].select();
				if(visualMap[i][j].isSelected()){
					selectedCoordinates.add(map[i][j].getCoordinates());
				}
				else{
					selectedCoordinates.remove(map[i][j].getCoordinates());
				}
				
			}
		}
		System.out.println("total selected : " + selectedCoordinates.size());
	}
	
	/**
	 * @param tile the tile being checked, it is given an image depending on when it was last cleaned
	 * @returns the tile after the image has been changed
	 */
	public VisualTile getCleanedVisualTile(MapTile tile) {

		Date temp = tile.whenCleaned();
		Calendar lastCleaned = Calendar.getInstance();
		lastCleaned.setTime(temp);

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.MINUTE, -60);

		if (lastCleaned.before(cal)) {
			return new VisualTile(ImgSource.imgPath + "/over60.png");
		}

		cal.add(Calendar.MINUTE, 15);
		if (lastCleaned.before(cal)) {
			return new VisualTile(ImgSource.imgPath + "/between45-60.png");
		}

		cal.add(Calendar.MINUTE, 15);
		if (lastCleaned.before(cal)) {
			return new VisualTile(ImgSource.imgPath + "/between45-30.png");
		}

		cal.add(Calendar.MINUTE, 15);
		if (lastCleaned.before(cal)) {
			return new VisualTile(ImgSource.imgPath + "/between30-15.png");
		}

		return new VisualTile(ImgSource.imgPath + "/under15.png");

	}
	
	/**
	 * @param x x co-ordinate of tile to use
	 * @param y y co-ordinate of tile to use
	 * finds nearest 25 tiles that can be cleaned to this location and adds to arraylist
	 */
	public void surroundingTiles(int x, int y) {
		// ArrayList<String> clean = new ArrayList<String>();

		x = x - 2;
		y = y + 2;
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (x >= 0 && y >= 0) {
					if (map[x][y] != null && map[x][y].toString().equals("0")) {
						System.out.print(" " + x + "," + y);
						// String found = (x+","+y);direction
						// clean.add(found);
						// toClean.add(new Coordinate(x,y));
						selectedCoordinates.add(map[x][y].getCoordinates());

						// METHOD WILL BE HERE SO RCS WILL KNOW TO CLEAN THESE
						// TILES
					}
				}
				x++;
			}
			System.out.println("");
			y--;
			x = x - 5;
		}
	}
	
	private MapTile oneEmptyTile(){
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				if(map[i][j].isPassable())
					return map[i][j];
			}
		}
		return null;
	}
	
	private MapTile[] getNeighbouringTiles(MapTile tile){
		MapTile[] neighbours = new MapTile[4];
		try{
			neighbours[0] = map[tile.getCoordinateX() + 1][tile.getCoordinateY()];
		}
		catch(IndexOutOfBoundsException e){
			neighbours[0] = null;
		}
		try{
			neighbours[1] = map[tile.getCoordinateX()][tile.getCoordinateY() - 1];
		}
		catch (IndexOutOfBoundsException e) {
			neighbours[1] = null;
		}
		
		try{
			neighbours[2] = map[tile.getCoordinateX() - 1][tile.getCoordinateY()];
		}
		catch(IndexOutOfBoundsException e){
			neighbours[2] = null;
		}
		try{
			neighbours[3] = map[tile.getCoordinateX()][tile.getCoordinateY() + 1];
		}
		catch(IndexOutOfBoundsException e){
			neighbours[3] = null;
		}
		return neighbours;
	}
	
	public boolean isFullyDiscovered(){
		LinkedList<MapTile> decidedTiles = new LinkedList<MapTile>();
		LinkedList<MapTile> undecidedTiles = new LinkedList<MapTile>();
		MapTile current = null;
		undecidedTiles.add(oneEmptyTile());
		
		while(!undecidedTiles.isEmpty()){
			current = undecidedTiles.remove();
			if(current.getType() == MapTileType.NOT_SET){
				return false;
			}
			
			MapTile[] neighbours = getNeighbouringTiles(current);
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
		return true;
	}
	
	/**
	 * @author Team Cyan
	 * responsible for dealing with interaction
	 */
	private class innerPanel extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public innerPanel(LayoutManager l){
			super(l);
		}

		public void paintComponent(Graphics graphics){
			super.paintComponent(graphics);
			((Graphics2D)graphics).drawImage(bkgImage, 0, 0, this);
		  
		}
	}
	
	/**
	 * @param b sets whether this map will be clickable or not
	 */
	
	public void setClickable(boolean b){
		this.clickable = b;
	}

	/** 
	 * if Clickable is set to false nothing happens
	 * Otherwise finds the co-ordinates of the tile that was clicked
	 */
	
	public void mouseClicked(MouseEvent e) {

		if(!clickable){
			return;
		} else if(!isFullyDiscovered()){
			JOptionPane.showConfirmDialog(this, "Map not yet fully discovered. \n Clickability will not be enabled until a full cleaning session has been run", "Undiscovered map", JOptionPane.ERROR_MESSAGE ,JOptionPane.OK_CANCEL_OPTION, null);
			return;
		}
		
		if(e.getSource().getClass() == VisualTile.class) {
			HashMap<String, String> options = ActionCommandHelper.decodeActionCommand(((VisualTile)e.getSource()).getActionCommand());
			
			Coordinate c = new Coordinate(Integer.parseInt(options.get("0")), Integer.parseInt(options.get("1")));
			visualMap[c.getX()][c.getY()].select();
			
			if(lastSelectedCoordinate == null)
				lastSelectedCoordinate = new Coordinate(Integer.parseInt(options.get("0")), Integer.parseInt(options.get("1")));
			else {
				selectTiles(lastSelectedCoordinate, new Coordinate(Integer.parseInt(options.get("0")), Integer.parseInt(options.get("1"))));
				lastSelectedCoordinate = null;
			}
			System.out.println("Selected Coordinate : " + new Coordinate(Integer.parseInt(options.get("0")), Integer.parseInt(options.get("1"))));
		}

	}
	
	
	private void updateMapTiles(){
		System.out.println("w");
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				try{
					VisualTile t = getCleanedVisualTile(map[i][j]);
					visualMap[i][j].setImage(t.getImage());
				}
				catch(Exception e){}
			}
		}
	}
	
	
	public void run() {
		while(running){
			updateMapTiles();
			try {
				Thread.sleep(900000);
			} catch (InterruptedException e) {}
		}	
	}
	
	public void killUpdateThread(){
		try{
			running = false;
			updateThread.interrupt();
			while(updateThread.isAlive());
		}
		catch(Exception e){}
		
	}
	
	
	
	/**
	 * @return all co-ordinates selected for cleaning
	 */
	public ArrayList<Coordinate> getSelectedTiles() {
		return selectedCoordinates;

	}

	/**
	 * clears the array containing co-ordinates to clean
	 */
	
	public void clearSelection() {

		selectedCoordinates.clear();

	}
		
	
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @author Team Cyan
	 *
	 */
	private class VisualTile extends Component {
		
		private static final long serialVersionUID = 1L;
		
		private Image image;
		private final Image selecteLayer = (new ImageIcon(ImgSource.imgPath + "/selectedLayer.png")).getImage();
		private String actionCommand = "";
		private boolean selected = false;
			
	    	/**
	    	 * @param newImage sets image of this component to the parameter
	    	 */
	    	public void setImage(Image newImage){
	    		this.image = newImage;
	    		this.paint(this.getGraphics());
	    	}
	    
	    	public void clearAndSetImage(Image newImage){
	    		this.getGraphics().clearRect(0, 0, 10, 10);
	    	}
	    	
	    	/**
	    	 * @return the image this component is using
	    	 */
	    	public Image getImage(){
	    		return image;
	    	}
	    	
	    	/**
	    	 * @param commandToSet sets the command of this object to this
	    	 */
	    	public void setActionCommand(String commandToSet) {
	    		this.actionCommand = commandToSet;
	    	}
	    	
	    	/**
	    	 * @return gets the Action Command of this component
	    	 */
	    	public String getActionCommand() {
	    		return this.actionCommand;
	    	}
		
		    /**
		     * @param imgPath constructs object with passed String as file path
		     */
		    public VisualTile(String imgPath) {
		    	image = (new ImageIcon(imgPath)).getImage();
		    }
		    
		    /** 
		     * @see draws image with given graphics
		     */
		    
		    public void paint(Graphics g){
		    	super.paint(g);
		    	if(!selected){
		    		g.drawImage(image,1, 1, null);
		    	}
		    	else{
		    		g.drawImage(selecteLayer,1, 1, null);
		    	}
		    }
		    
		    /**
		     * changes the variable stating if the tile is selected or not
		     */
		    public void select(){
		    	this.selected = !selected;
		    	this.paint(this.getGraphics());
		    }
		    
		    /**
		     * Gets whether the tile is selected.
		     * @return {@literal TRUE} if selected and {@literal FALSE} otherwise
		     */
		    public boolean isSelected(){
		    	return this.selected;
		    }
	}
}