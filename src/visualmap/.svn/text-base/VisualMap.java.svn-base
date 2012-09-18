package visualmap;

import images.ImgSource;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import map.Coordinate;
import map.MapTile;
import map.MapTileType;

public abstract class VisualMap extends JPanel {
	
	protected MapTile[][] map;
	protected String themePath = "/mario";
	
	public VisualMap(){
		super(new GridLayout(1,1));
		map = new MapTile[10][10];
		
		for(int i = 0; i < map.length ; i++) {
			for(int j = 0; j < map[i].length; j++) {
				map[i][j] = new MapTile(MapTileType.NOT_SET, i, j);
			}
		}
	}
	
	public void updateMap(MapTile[][] newMap){
		this.map = newMap;
	}
	
	public void setTheme(String theme){
		
		if(theme.equals("mario")){
			themePath = "/mario";
		}else if(theme.equals("zelda")){
			themePath = "/zelda";
		}
		ImgSource.themePath = ImgSource.imgPath + "/themes" + themePath;
	}

	
	public String getTheme(){
		return themePath;
	}
	
	/**
	 * Takes screenshot depending on the current array of MapTiles
	 */
	public void takeScreenShot(String path){
		new ScreenShot(map, path);
	}

	
	/**
	 * @return the Array of MapTiles being used
	 */
	public MapTile[][] getMap(){
		return map;
	}
	
	public abstract void updateRobotPosition(Coordinate robotCoordinate);
	
	public abstract Coordinate getRobotPosition();
	
	public abstract void updateTile(MapTile tile);
	
	public abstract ArrayList<Coordinate> getSelectedTiles();
	
	public abstract void resetSelection();

	public abstract void clearSelection();

	public abstract void setClickable(boolean b);
}
