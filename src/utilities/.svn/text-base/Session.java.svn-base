package utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import map.Coordinate;

/**
 * @author Team Cyan
 * Interface that all session objects will implement
 * Allows them to be Comparable, cloneable and serializable
 *
 */
public interface Session extends Comparable<Session>, Cloneable, Serializable{
	/**
	 * @return the name of the session
	 */
	public String getName();
	/**
	 * @return an ArrayList of Coordinates that will be permitted tiles
	 */
	public ArrayList<Coordinate> getPermittedTiles();

	/**
	 * @return a Calendar that will have the date and time of the cleaning session
	 */
	public Calendar getDateTime();
	/**
	 * @return the status of the cleaning session
	 */
	public String getStatus();
	/**
	 * @param status the String passed will become the new status of the cleaning session
	 */
	public void setStatus(String status);
	/**
	 * @return a clone of the cleaning session
	 */
	public Object clone();
}