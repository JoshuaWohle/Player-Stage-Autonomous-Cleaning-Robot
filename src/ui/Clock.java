package ui;

import java.util.ArrayList;
import java.util.Calendar;
import utilities.TickListener;

/**
 * 
 * @author Team Cyan
 * This Class will provide methods to handle time for the User Interface 
 *
 */

public class Clock implements Runnable{

	private ArrayList<TickListener> tickListeners;
	private static Clock clock;
	private Thread threadTime;
	private Calendar cal;
	
	private Clock(){
		tickListeners = new ArrayList<TickListener>();
		cal = Calendar.getInstance();
		threadTime = new Thread(this);
	}
	
	
	/**
	 * Checks whether clock has a time if not then it is given one, then executing
	 * the .startTime() method to start the clock
	 * 
	 * @return clock
	 */
	public static Clock getClock(){
		if(clock == null){
			clock = new Clock();
			clock.startTime();
		}
		return clock;
	}

	/**
	 * Add the appropriate TickListener Objects to the tickListeners <Array>
	 * @param tick
	 */
	
	public void addTickListener(TickListener tick){
		tickListeners.add(tick);
	}
	
	/**
	 * Updates the clock with the current time sleeping every second 
	 */
	
	public void run() {
		while(true){
			cal = Calendar.getInstance();
			for(int i = 0; i < tickListeners.size(); i++){
				tickListeners.get(i).tick(cal);
			}
			try {Thread.sleep(999);} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Initialising the start time for the clock
	 */
	private void startTime(){
		threadTime.start();
	}
	
	/**
	 * Returns an instance of Calendar Object
	 * 
	 * @return Calendar cal
	 */
	public Calendar getCalendar(){
		return cal;
	}
	
	
	/**
	 * Compares two Calendar TIME values and checks whether the time is set correctly 
	 * with the end time after the start time & the start time before the end time.
	 * Comparing hour & minute
	 * 
	 * @param start
	 * @param end
	 * @return Integer value
	 */
	public static int compareTime(Calendar start, Calendar end){
			Calendar cal = start;
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int min = cal.get(Calendar.MINUTE);
	
			Calendar otherDateTime = end;
			int ohour = otherDateTime.get(Calendar.HOUR_OF_DAY);
			int omin = otherDateTime.get(Calendar.MINUTE);
		if (ohour > hour) {
			return -1;
		} else if (ohour < hour) {
			return 1;
		} else {
			if (omin > min) {
				return -1;
			} else if (omin < min) {
				return 1;
			} else {
				return 0;
			}
		}

	}
	
	
	/**
	 * Compares the two Calendar values DATES, checking any differences visa vi 
	 * Calendar o: 15/01/2001 compare Calendar cal: 10/01/2001 will return -1 
	 * Calendar o: 20/01/2001 compare Calendar cal: 21/01/2001 will return +1
	 * 
	 * Checking DAY,MONTH,YEAR
	 * 
	 * @param o
	 * @return Integer value
	 */
	public static int compareDateTime(Calendar o){
		Calendar cal = Clock.getClock().getCalendar();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		Calendar otherDateTime = o;
		int oday = otherDateTime.get(Calendar.DAY_OF_MONTH);
		int omonth = otherDateTime.get(Calendar.MONTH) + 1;
		int oyear = otherDateTime.get(Calendar.YEAR);
		int ohour = otherDateTime.get(Calendar.HOUR_OF_DAY);
		int omin = otherDateTime.get(Calendar.MINUTE);
		
		if(oyear > year){
			return -1;
		}
		else if(oyear < year){
			return 1;
		}
		else{
			if(omonth > month){
				return -1;
			}
			else if(omonth < month){
				return 1;
			}
			else{
				if(oday > day){
					return -1;
				}
				else if(oday < day){
					return 1;
				}
				else{
					if(ohour > hour){
						return -1;
					}
					else if(ohour < hour){
						return 1;
					}
					else{
						if(omin > min){
							return -1;
						}
						else if(omin < min){
							return 1;
						}
						else{
							return 0;
						}
					}
				}
			}
		}
	}
}
