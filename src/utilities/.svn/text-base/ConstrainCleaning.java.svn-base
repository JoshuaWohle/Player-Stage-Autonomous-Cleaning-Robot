package utilities;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author Team Cyan
 * class responsible for storing constraints on session cleaning times
 */
public class ConstrainCleaning implements Serializable, ConstrainTime{

	//Class object to get both constrain times
	//new
	
	private Calendar start;
	private Calendar end;
	
	/**
	 * @param start the parameter to be set as the start constraint time
	 * @param end the parameter to be set as the end Constraint time
	 */
	public ConstrainCleaning(Calendar start,Calendar end){
		
		this.start = start;
		this.end = end;
		
		
	}
	
	/** 
	 * @return the start constraint time of this cleaning session
	 */
	public Calendar getStart(){
		return this.start;
	}
	/** 
	 * @return the end constraint time of this cleaning session
	 */
	public Calendar getEnd(){
		return this.end;
	}
	
}
