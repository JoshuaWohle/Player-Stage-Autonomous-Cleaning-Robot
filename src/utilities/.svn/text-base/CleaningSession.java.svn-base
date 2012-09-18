package utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import map.Coordinate;

/**
 * @author Team Cyan
 * Used to store details about a cleaning session
 */
public class CleaningSession implements Session, Serializable{

	private ArrayList<Coordinate> permittedTiles;
	private Calendar dateTime;
	private String name;
	private String status;
	
	/**
	 * @param name the name of the session
	 * @param dateTime time to clean
	 * @param permittedTiles tiles robot should clean
	 * @param prohibitTiles tiles robot will not clean
	 */
	public CleaningSession(String name, Calendar dateTime, ArrayList<Coordinate> permittedTiles){
		
		this.dateTime = dateTime;
		this.permittedTiles = permittedTiles;
		this.name = name;
		this.status = "";
		
	}
	 
	/** 
	 * @param status sets the status to the passed parameter
	 */
	public void setStatus(String status){
		this.status = status;
	}
	
	/** 
	 *@return gets the status of this session
	 */
	public String getStatus(){
		return this.status;
	}
	
	/** 
	 * @return the name of this session
	 */
	
	public String getName(){
		return this.name;
	}
	
	/** 
	 * @return the date of this session
	 */
	
	public Calendar getDateTime() {
		return this.dateTime;
	}
	
	/** 
	 * @return arrayList of permitted tiles
	 */
	
	public ArrayList<Coordinate> getPermittedTiles() {
		return this.permittedTiles;
	}
	/** 
	 * @return a string containing the time of this session and it's status
	 */
	
	public String toString(){
		int day = this.getDateTime().get(Calendar.DAY_OF_MONTH);
		int month = this.getDateTime().get(Calendar.MONTH) + 1;
		int year = this.getDateTime().get(Calendar.YEAR);
		int hours = this.getDateTime().get(Calendar.HOUR_OF_DAY);
		int mins = this.getDateTime().get(Calendar.MINUTE);
		return this.name + " [" + day + "/" + month + "/" + year + " - " + hours + ":" + mins + "]" + " " + this.getStatus();	
	}

	/** 
	 * @param o compared with current session
	 * @return an int -1,0,1 depending if this session was before, same time or after o respectively
	 */
	
	public int compareTo(Session o) {
		return this.getDateTime().compareTo(o.getDateTime());
	}
	
	/** 
	 * @return a cloned session object
	 */
	
	public Object clone(){
		Session temp = new CleaningSession(this.getName(), this.getDateTime(), this.getPermittedTiles());
		temp.setStatus(new String(this.getStatus()));
		return temp;
	}

}