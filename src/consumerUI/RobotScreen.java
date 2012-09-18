package consumerUI;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import visualmap.VisualMap;


/**
 * This is the screen used for the main Screen interface, this screen will show
 * the initial map and develop over time as the robot cleans
 * @author Team Cyan
 *
 */

public class RobotScreen extends JPanel {
	
	VisualMap visualMap;
	
	
	/**
	 * @param args
	 */
	
	private static RobotScreen robotScreen;
	private Color clrMainScreen = new Color(00,33,48);
;
	
	public RobotScreen(){
		
		super();		
			
		this.setBackground(clrMainScreen);
		this.setLayout(new BorderLayout());
		this.setSize(800,600);
	}

	
	/**
	 * Sets the map to the robot screen
	 * @param map
	 */
	public void setMapScreen(VisualMap map){
		if(visualMap != null)
			this.remove(visualMap);
		
		visualMap = map;
		this.add(visualMap,BorderLayout.CENTER);
	}
	
	/**
	 * Removes the component that holds map
	 */
	public void removeMapScreen(){
		
		this.removeAll();
	}
	
	/**
	 * Returns an instance of the robot screen with the visual map loaded
	 * 
	 * @param map
	 * @return robotScreen
	 */
	
     public static RobotScreen getInstance(){
    	 if(robotScreen == null){
    		 robotScreen = new RobotScreen();
    	 }
    	 return robotScreen;
    	 
     }
		
	}

