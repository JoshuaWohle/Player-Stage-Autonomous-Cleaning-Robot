package utilities;

import java.util.Calendar;

/**
 * @author Team Cyan
 * Used to update time when a Tick occurs
 *
 */
public interface TickListener {
 
	/**
	 * @param t is set to Calendar when tick occurs
	 */
	public void tick(Calendar t);


	
}
