package consumerUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import visualmap.VisualMap;

import ui.ImageButton;
import utilities.CleaningSession;

import utilities.Session;
import map.Coordinate;


/**
 * This class is the interface used to schedule cleaning sessions
 *  
 * @author Team Cyan
 *
 */

public class SchedularScreen extends JPanel implements ActionListener,ListSelectionListener {

	private static SchedularScreen scheduleScreen;
	private ImageButton save,removeSchedule;
	private JLabel scheduleTimeTitle;
	private JLabel emptyNorth,emptyEast,emptySouth,emptyWest;

	private JSpinner setTime;
	private SpinnerDateModel timeModel;
	private Container container;
	private JPanel north,south,center,east,eastNorth,eastCenter,eastCenterSouth;
	private JSpinner.DateEditor timeFormat;
	private Font font;
	private DefaultListModel list;
	private JList listOfSchedulesToClean;
	private JScrollPane listOfSchedulesToCleanScroll,listOfSessionsInfo;
	private Color clrSchedularScreen = new Color(0,51,75);
	private Color clrSchedularScreenIN = new Color(00,33,48);
	private CleaningSession cleaningSession;
	private ScheduleListener sl;
	private Session selectedValues;
	private ArrayList<Coordinate>coordinatesToClean;
	private ArrayList<Coordinate>coordinatesToProhibit;
	private JTextArea sessionInfo;
	
	private VisualMap map;
	
	
	/**
	 * Constructor used to Create the Schedule screen
	 * @param map - passing the map to visually represent what areas to clean
	 */
	private SchedularScreen(){
		
		super();
		
		font = new Font("sansserif",Font.BOLD,15);
		
		sessionInfo = new JTextArea(5,20);
		sessionInfo.setFont(font);
		sessionInfo.setForeground(Color.CYAN);
		sessionInfo.setBackground(clrSchedularScreen);
		sessionInfo.setText("Session INFO: ");
		sessionInfo.setEditable(false);
		
		listOfSessionsInfo = new JScrollPane(sessionInfo);
		
		emptyNorth = new JLabel(" ");
		emptyWest = new JLabel(" ");
		emptyEast = new JLabel(" ");
		emptySouth = new JLabel(" ");
		
		
		scheduleTimeTitle = new JLabel("Schedule Date & Time For Cleaning: ");
		scheduleTimeTitle.setFont(font);
		scheduleTimeTitle.setForeground(Color.CYAN);
		
		
		list = new DefaultListModel();
		listOfSchedulesToClean = new JList(list);
		listOfSchedulesToClean.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listOfSchedulesToClean.addListSelectionListener(this);
		listOfSchedulesToClean.setVisibleRowCount(10);
		listOfSchedulesToClean.setBackground(clrSchedularScreen);
		listOfSchedulesToClean.setForeground(Color.CYAN);
		listOfSchedulesToClean.setFont(font);
		listOfSchedulesToCleanScroll = new JScrollPane(listOfSchedulesToClean);
		listOfSchedulesToCleanScroll.setPreferredSize(new Dimension(350,170));
        
		save = new ImageButton("SAVE","/save-on.png","/save-on.png");
		save.setActionCommand("Save Schedule");
		save.setToolTipText("Save Schedule Cleaning");
		save.addActionListener(this);
		
		removeSchedule = new ImageButton("REMOVE","/save-off.png","/save-off.png");
		removeSchedule.setActionCommand("Remove Schedule");
		removeSchedule.setToolTipText("Delete Schedule");
		removeSchedule.addActionListener(this);
		
		timeModel = new SpinnerDateModel();
		setTime = new JSpinner(timeModel);
		timeFormat = new JSpinner.DateEditor(setTime,"H:mm dd:MM:yyyy");
		setTime.setEditor(timeFormat);
		
		        
        north = new JPanel();
        north.setLayout(new FlowLayout());
        north.setBackground(clrSchedularScreen);
        north.add(scheduleTimeTitle);
 
        north.add(setTime);
      
        center = new JPanel();
        center.setBackground(clrSchedularScreen);
        center.setLayout(new BorderLayout());
        center.setBackground(clrSchedularScreen);
        
        eastNorth = new JPanel();
        eastNorth.setBackground(clrSchedularScreen);
        eastNorth.setLayout(new FlowLayout());
        eastNorth.add(listOfSchedulesToCleanScroll);
        
        eastCenterSouth = new JPanel(new FlowLayout());
        eastCenterSouth.setBackground(clrSchedularScreen);
        eastCenterSouth.setLayout(new FlowLayout());
        eastCenterSouth.add(listOfSessionsInfo);
        
        eastCenter = new JPanel();
        eastCenter.setBackground(clrSchedularScreen);
        eastCenter.setLayout(new BorderLayout());
        eastCenter.add(removeSchedule,BorderLayout.CENTER);
        eastCenter.add(eastCenterSouth,BorderLayout.SOUTH);
         
        east = new JPanel();
        east.setLayout(new BorderLayout());
        east.add(eastNorth,BorderLayout.NORTH);
        east.add(eastCenter,BorderLayout.CENTER);
        east.setForeground(clrSchedularScreen);
       
        south = new JPanel();
        south.setLayout(new FlowLayout());
        south.setBackground(clrSchedularScreen);
        south.add(save);
        
		container = new Container();
		container.setLayout(new BorderLayout());
		container.setBackground(clrSchedularScreen);
		container.add(north,BorderLayout.NORTH);
 		container.add(center,BorderLayout.CENTER);
 		container.add(east,BorderLayout.EAST);
		container.add(south,BorderLayout.SOUTH);
		
		this.setLayout(new BorderLayout(20,20));
		this.add(emptyNorth,BorderLayout.NORTH);
		this.add(emptySouth,BorderLayout.SOUTH);
		this.add(emptyEast,BorderLayout.EAST);
		this.add(emptyWest,BorderLayout.WEST);
		this.add(container,BorderLayout.CENTER);
		this.setBackground(clrSchedularScreenIN);
		this.setSize(800, 600);
		
	}
	
	
	/**
	 * updates the Schedules created by the user
	 * @param currentSessions
	 */
	public void updateSessionsList(List currentSessions){
		Object[] sessions = currentSessions.toArray();
		list.removeAllElements();
		for(int i = 0; i < sessions.length; i++){
			list.addElement(sessions[i]);
		}
	}
	
	/**
	 * Gives any information based on the Session
	 * @param info
	 */
	public void setSessionInfoText(String info){
		sessionInfo.append("\n" + info);
		
	}
	
	/**
	 * Sets the Map screen for Schedule Screen
	 * @param map
	 */
	public void setMapScreen(JPanel map){
		
		center.add(map,BorderLayout.CENTER);
		
	}
	
	/**
	 * Removes the map screen from robot
	 */
	public void removeMapScreen(){
		
		center.removeAll();
	}
	
	/**
	 * returns an instance of the Schedule Screen
	 * @param map
	 * @return
	 */
	public static SchedularScreen getInstance(){
		
		if(scheduleScreen == null){
			scheduleScreen = new SchedularScreen();
		}
		
		return scheduleScreen;
		
	}


	/**
	 * This Method is used to pass the Session to the list
	 * @param session 
	 *
	 */
	private void dispatchSchedulerListenerEvent(Session session){
		try{
		sl.newSession(session);
		}catch(NullPointerException e){
			
			e.printStackTrace();
			
		}
	}
	
	/**
	 * 
	 * This Method is used to remove the session from the list
	 * @param sessionRemoved
	 */
	private void dispatchRemovedSchedulerListenerEvent(Session sessionRemoved){
		
		try{
		sl.removeSession((Session)sessionRemoved);
		}catch(NullPointerException e){
			
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Handles the action events from the user
	 * 
	 */
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String actionText = (String) e.getActionCommand();
		
		
		if(actionText.equals("Save Schedule")){
						
			String titleSession = JOptionPane.showInputDialog("Please Select a Title for the Session");
			
			Calendar cal = Calendar.getInstance();
		    cal.setTime(timeModel.getDate());
				
			cleaningSession = new CleaningSession(titleSession,cal, map.getSelectedTiles());
			dispatchSchedulerListenerEvent(cleaningSession);
			map.resetSelection();
			
		}else if(actionText.equals("Remove Schedule")){
			
			    selectedValues = (Session)listOfSchedulesToClean.getSelectedValue();
			    dispatchRemovedSchedulerListenerEvent(selectedValues);
			
		}
		
	}


	
	public void valueChanged(ListSelectionEvent l) {
		// TODO Auto-generated method stub
		
	}
	

	/**
	 * Adds the listener to the Schedule for new updates
	 * @param sl
	 */
	public void addScheduleListener(ScheduleListener sl){
		
		this.sl = sl;
		
	}


	public void passVisualMap(VisualMap visualMap) {
		this.map = visualMap;
		
	}
	

}