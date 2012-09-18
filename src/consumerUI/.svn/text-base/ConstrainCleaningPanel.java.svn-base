package consumerUI;

import java.awt.FlowLayout;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import ui.Clock;

/**
 *  This class is the configured JOptionPanel used to Constrain Cleaning Times
 * @author Team Cyan
 *
 */

public class ConstrainCleaningPanel extends JPanel {

	JSpinner timeStart, timeEnd;
	JLabel timeStartTitle, timeEndTitle;
	SpinnerDateModel timeStartModel, timeEndModel;
	JSpinner.DateEditor timeStartFormat, timeEndFormat;
	Calendar cal;
	
	/**
	 * Constructor setting up the Panel
	 */
	public ConstrainCleaningPanel() {

		super(new FlowLayout());

		timeStartTitle = new JLabel("Start: ");
		timeEndTitle = new JLabel("End: ");

		timeStartModel = new SpinnerDateModel();
		timeStart = new JSpinner(timeStartModel);
		timeStartFormat = new JSpinner.DateEditor(timeStart, "H:mm");
		timeStart.setEditor(timeStartFormat);

		timeEndModel = new SpinnerDateModel();
		timeEnd = new JSpinner(timeEndModel);
		timeEndFormat = new JSpinner.DateEditor(timeEnd, "H:mm");
		timeEnd.setEditor(timeEndFormat);

		this.add(timeStartTitle);
		this.add(timeStart);
		this.add(timeEndTitle);
		this.add(timeEnd);

	}

	
	/**
	 * Returns the Start Time for Constraining Cleaning
	 * 
	 * @return cal
	 */
	public Calendar getStart() {

		cal = Calendar.getInstance();
		cal.setTime(timeStartModel.getDate());
		
		return cal;

	}

	/**
	 * Returns the End time for Constrain Cleaning
	 * 
	 * @return cal
	 */
	
	public Calendar getEnd() {

		cal = Calendar.getInstance();
		cal.setTime(timeEndModel.getDate());
		
		return cal;
	}
	
	/**
	 * Checks whether the Constrain times have been set appropriately 
	 * 
	 * @return boolean
	 */

	public int checkConstrainTime() {
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.setTime(timeStartModel.getDate());
		calEnd.setTime(timeEndModel.getDate());
		if (Clock.compareTime(calStart, calEnd) < 0) {
			return -1;
		} else if(Clock.compareTime(calStart, calEnd) == 0){
			return 0;
		} else {
			return 1;
		}
	}

}
