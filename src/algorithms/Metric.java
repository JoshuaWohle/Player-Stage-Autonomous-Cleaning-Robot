package algorithms;

import java.io.Serializable;

import map.MapTile;

/**
 * A class that encapsulates details used in path finding from one area to
 * another. The details it encapsulates include the best neighbouring tile with
 * the smallest path and the path to that tile. An object of this class is
 * merely used to aid effective path finding from one tile to another.
 * 
 * @author mayank
 * 
 */
public class Metric implements Comparable<Metric>, Serializable{
	
	private static final long serialVersionUID = -3409573145434387326L;
	private MapTile bestNeighbor;
	private int pathSize;
	private boolean used;
	
	/**
	 * Constructs an object of this class with no best neighbour and path size to be {@literal Integer.MAX_VALUE}
	 */
	public Metric(){
		bestNeighbor = null;
		pathSize = Integer.MAX_VALUE;
		used = false;
	}
	/**
	 * A method that sets the best neighbour of this object.
	 * @param bestNeighbor the {@link MapTile} as the best neighbouring tile to this
	 */
	public void setBestNeighbour(MapTile bestNeighbor){
		this.bestNeighbor = bestNeighbor;
		used = true;
	}

	/**
	 * A method to return the best neighbour.
	 * @return the best neighbour
	 */
	public MapTile getBestNeighbour(){
		return this.bestNeighbor;
	}
	
	/**
	 * A method to set the path size
	 * @param s path size
	 */
	public void setPathSize(int s){
		pathSize = s;
		used = true;
	}
	
	/**
	 * A method to get the path size
	 * @return the path size
	 */
	public int getPathSize(){
		return pathSize;
	}
	
	/**
	 * A method to determine whether an object of this class has been used or
	 * not. An object is used whenever a best neighbour has been set or a new
	 * path size has been set
	 * 
	 * @return {@literal true} when this object has been used and
	 *         {@literal false} otherwise
	 */
	public boolean isUsed(){
		return used;
	}

	
	public int compareTo(Metric metric){
		if(pathSize < metric.getPathSize())
			return -1;
		else if(pathSize > metric.getPathSize())
			return 1;
		else
			return 0;
	}

}
