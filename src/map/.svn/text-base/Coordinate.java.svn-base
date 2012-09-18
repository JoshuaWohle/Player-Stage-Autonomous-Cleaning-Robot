package map;

import java.io.Serializable;

public class Coordinate implements Comparable<Coordinate>, Serializable, Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6875940087196461124L;
	/**
	 * 
	 */
	
	private int x = 0;
	private int y = 0;
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coordinate(Coordinate coordinates) {
		this(coordinates.getX(), coordinates.getY());
	}
	
	public String toString(){
		return "(" + x + ", " + y + ")";
	}

	
	public int compareTo(Coordinate o) {
		if(this.x == o.getX() && this.y == o.getY()){
			return 0;
		}
		else if(this.x > o.getX()|| this.y > o.getY()){
			return 1;
		}
		else {
			return -1;
		}
	}
	
	
	public Object clone(){
		return new Coordinate(this.x, this.y);	
	}

}