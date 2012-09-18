package consumerUI;

import images.ImgSource;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import map.Coordinate;
import map.MapTile;
import ui.ConnectionPreferences;
import ui.GradientPanel;
import ui.ImageButton;
import ui.JMenuItemExtended;
import ui.JPopupMenuExtended;
import ui.ServerAddressPanel;
import ui.UI;
import utilities.CleaningSession;
import utilities.ConstrainCleaning;
import utilities.Request;
import utilities.Response;
import utilities.Session;
import visualmap.VisualMap;
import visualmap.VisualMap2D;
import visualmap.VisualMap3D;

/**
 * This is the Main Consumer UI Class this is where the user 
 * has the most interaction with the Software
 * 
 * @author Team Cyan
 *
 */

public class ConsumerUI extends UI implements ActionListener, MouseListener,
		MouseMotionListener,ScheduleListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Server {
		COMMANDER, MAP_SERVER
	}
	
	private String theme = "mario";
	private RobotScreen rbtScrn;
	private SchedularScreen schdulScrn;
	private Color clrIN = new Color(00, 33, 48);
	private Color gradientColorLight = new Color(43, 86, 102);
	private Color gradientColorDark = new Color(21, 60, 75);

	private JFileChooser fc = new JFileChooser();
	private ObjectOutputStream output = null;
	private ObjectInputStream input = null;

	private JMenuItem connectCommanderMenuItem, connectMapServerMenuItem,setSpeedHigh,setSpeedMiddle,setSpeedLow,j2DView,j3DView,marioTheme,zeldaTheme;

	private static Point point = new Point();

	private ImageButton start, stop, schedule, settings, returnToMainMenu,
			close, minimize;

	private JPopupMenu settingsMenu;

	private JLabel emptyNorth, emptyWest, emptyEast,status;
	private JLabel frameTitle;
	private Container containerPrime, innerPrime, innerPrimeCenter;
	private JPanel southPrime, northPrime, northEastPrime, northWestPrime,innerPrimeSouth;

	private Socket clientCommander;
	private ObjectOutputStream outCommander = null;
	private ObjectInputStream inCommander = null;

	private Socket clientMapServer;
	private ObjectOutputStream outMapServer = null;
	private ObjectInputStream inMapServer = null;
	
	private VisualMap visualMap;

	private boolean viewIs2D,viewIs3D;
	private final String COMMANDER_PREFERENCE_FILENAME = "CommanderConnectionPreferencesConsumerUI";
	private final String MAPSERVER_PREFERENCE_FILENAME = "MapServerConnectionPreferencesConsumerUI";
	private boolean mapThreadRunning;
	private boolean was3DMap;
	private boolean commanderThreadRunning;
	private SoundThread themeSound = new SoundThread(ImgSource.themePath + "/theme.wav");
	
	

	/**
	 * Constructor Setting up the UI frame
	 * 
	 * Parameter values passed are default values to set up the connection between
	 * Client-Server
	 * 
	 * @param address
	 * @param portCommander
	 * @param portMap
	 */
	public ConsumerUI(String address,String portCommander,String portMap){
		super();
		
		
		
		mapThreadRunning = false;
		commanderThreadRunning = false;
		try {
			javax.swing.UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		visualMap = new VisualMap2D();
		visualMap.setClickable(false);
		
		
		viewIs2D = true;
		viewIs3D = false;
				
		schdulScrn = SchedularScreen.getInstance();
		schdulScrn.addScheduleListener(this);
		schdulScrn.passVisualMap(visualMap);
		
		rbtScrn = RobotScreen.getInstance();
		this.setUndecorated(true);

		frameTitle = new JLabel("Team Cyan Robot");
		frameTitle.setFont(font);
		frameTitle.setForeground(Color.CYAN);
		
		status = new JLabel("Status Update");
		status.setFont(font);
		status.setHorizontalAlignment(JLabel.CENTER);
		status.setForeground(Color.CYAN);
		
		JPopUpMenuSettings();

		close = new ImageButton("CLOSE", "/close.png", "/close.png");
		close.addActionListener(this);

		minimize = new ImageButton("MINIMIZE", "/minimize.png", "/minimize.png");
		minimize.addActionListener(this);

		start = new ImageButton("START", "/play-off.png", "/play-on.png");
		start.addActionListener(this);

		stop = new ImageButton("STOP", "/stop-off.png", "/stop-on.png");
		stop.addActionListener(this);

		schedule = new ImageButton("SCHEDULE", "/schedule-off.png",
				"/schedule-on.png");
		schedule.addActionListener(this);

		settings = new ImageButton("SETTINGS", "/settings-off.png",
				"/settings-on.png");
		settings.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {

				settingsMenu.show(thisFrame, 520, 540);
			}
		});

		returnToMainMenu = new ImageButton("RETURN", "/return-off.png",
				"/return-on.png");
		returnToMainMenu.addActionListener(this);

		innerPrimeCenter = new JPanel(new CardLayout());
		innerPrimeCenter.add(rbtScrn, "RETURN");
		innerPrimeCenter.add(schdulScrn, "SCHEDULE");

		emptyNorth = new JLabel(" ");
		emptyWest = new JLabel(" ");
		emptyEast = new JLabel(" ");

		northEastPrime = new GradientPanel(1000, 70, gradientColorLight,
				gradientColorDark);
		northEastPrime.setLayout(new FlowLayout());
		northEastPrime.add(minimize);
		northEastPrime.add(close);

		northWestPrime = new GradientPanel(1000, 70, gradientColorLight,
				gradientColorDark);
		northWestPrime.setLayout(new GridLayout(2, 1));
		northWestPrime.add(time);
		northWestPrime.add(date);

		northPrime = new GradientPanel(1000, 70, gradientColorLight,
				gradientColorDark);
		northPrime.setLayout(new GridLayout(1, 3, 215, 215));
		northPrime.add(northWestPrime);
		northPrime.add(frameTitle);
		northPrime.add(northEastPrime);

	
	    innerPrimeSouth = new JPanel();	
	    innerPrimeSouth.setLayout(new FlowLayout());
	    innerPrimeSouth.setBackground(clrIN);
	    innerPrimeSouth.add(status);
		
		southPrime = new GradientPanel(1000, 70, gradientColorLight,
				gradientColorDark);
		southPrime.setLayout(new FlowLayout());
		southPrime.add(start);
		southPrime.add(stop);
		southPrime.add(schedule);
		southPrime.add(settings);
		southPrime.add(returnToMainMenu);

		innerPrime = new JPanel();
		innerPrime.setBackground(clrIN);
		innerPrime.setLayout(new BorderLayout());

		innerPrime.add(innerPrimeCenter, BorderLayout.CENTER);
		innerPrime.add(emptyNorth, BorderLayout.NORTH);
		innerPrime.add(emptyWest, BorderLayout.WEST);
		innerPrime.add(innerPrimeSouth, BorderLayout.SOUTH);
		innerPrime.add(emptyEast, BorderLayout.EAST);

		containerPrime = new Container();
		containerPrime.setLayout(new BorderLayout());
		containerPrime.add(northPrime, BorderLayout.NORTH);
		containerPrime.add(innerPrime, BorderLayout.CENTER);
		containerPrime.add(southPrime, BorderLayout.SOUTH);
		
		rbtScrn.setMapScreen(visualMap);
		
//		try {
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//		} catch (ClassNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (InstantiationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		UIManager.put("OptionPane.messageForeground", Color.CYAN);
//		UIManager.put("Panel.background", clrIN);
//		UIManager.put("OptionPane.background", clrIN);
//		UIManager.put("OptionPane.font", font);
//		UIManager.put("OptionPane.buttonFont",font);
//		
		
		
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setContentPane(containerPrime);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 700);
		this.setLocationRelativeTo(null);
		
		ConnectionPreferences commanderP = null;
		ConnectionPreferences mapServerP = null;
		
		try{
			commanderP = this.loadConnectionPreferences(COMMANDER_PREFERENCE_FILENAME);
			System.out.println(commanderP);
			this.connectToCommander(commanderP.getAddress(), commanderP.getPort());
		}
		catch(NullPointerException e){
			if(this.connectToCommander(address, portCommander)){
				this.saveConnectionPreference(COMMANDER_PREFERENCE_FILENAME, new ConnectionPreferences(address, portCommander));
			}
		}
		try{
			mapServerP = this.loadConnectionPreferences(MAPSERVER_PREFERENCE_FILENAME);
			this.connectToMapServer(mapServerP.getAddress(), mapServerP.getPort());
		}
		catch(NullPointerException e){
			if(this.connectToMapServer(address, portMap)){
				this.saveConnectionPreference(MAPSERVER_PREFERENCE_FILENAME, new ConnectionPreferences(address, portMap));
			}
		}
	}
	
	/**
	 * Runs the Thread for Commander Listening for any events dealt with the robot
	 */
	public void runCommanderThread() {
		if(commanderThreadRunning){
			return;
		}
		Thread threadCommander = new Thread(new Runnable() {
			boolean connected = clientCommander.isConnected();
			public void run() {
				commanderThreadRunning = true;
				while (connected) {
					try {
						Response response = null;
						try{
							response = (Response) (inCommander.readObject());
						}
						catch(Exception e){
							connected = false;
							disconnectServer(Server.COMMANDER);
						}
						
					switch(response.getInstruction()){
							case SAVE_MAP:
								byte[] mapFile = (byte[]) response.getOperand();
								saveMap(mapFile);
								break;

							case SESSIONS_LIST:
								schdulScrn.updateSessionsList((List<Session>)response.getOperand());
							break;
							case DROPPED_SESSION:
								schdulScrn.setSessionInfoText(response.getOperand().toString());
							break;	
							case EXECUTING_SESSION:
								schdulScrn.setSessionInfoText(response.getOperand().toString());
							break;	
							case SESSION_TERMINATED:
								schdulScrn.setSessionInfoText(response.getOperand().toString());
								JOptionPane.showMessageDialog(thisFrame, "Session Has Finished");
							break;
							case ERROR:
								JOptionPane.showMessageDialog(thisFrame, response.getOperand());
							break;	
								
					}
					} catch (ClassCastException e) {
						connected = false;
						e.printStackTrace();
					} catch (NullPointerException e) {
						connected = false;
						//e.printStackTrace();
					} catch (Exception e){
						connected = false;
						e.printStackTrace();
					}
				}
				commanderThreadRunning = false;
			}
		});
		threadCommander.start();

	}
	
	/**
	 * Runs the Thread Map Listening for any events dealt with Map 
	 * 
	 */
	public void runMapThread() {
		if(mapThreadRunning){
			return;
		}
		Thread threadVisualMap = new Thread(new Runnable() {
			boolean connected = clientMapServer.isConnected();
			
			public void run() {
				mapThreadRunning = true;
				while (connected) {
					try {
						Response response = null;
						try{
							response = (Response) (inMapServer.readObject());
						}
						catch(Exception e){
							connected = false;
							disconnectServer(Server.MAP_SERVER);
						}
						if (response.getInstruction() == Response.Instructions.NEW_MAP) {
							Object o = response.getOperand();
							ByteArrayInputStream in = new ByteArrayInputStream((byte[])o);
							ObjectInputStream inO = new ObjectInputStream(in);
							MapTile[][] tilesArray = (MapTile[][]) inO.readObject();
							visualMap.updateMap(tilesArray);
							//pos.updateMap(tilesArray);
							
						} else if (response.getInstruction() == Response.Instructions.UPDATE_TILE) {
							Object o = response.getOperand();
							ByteArrayInputStream in = new ByteArrayInputStream((byte[])o);
							ObjectInputStream inO = new ObjectInputStream(in);
							MapTile receivedTile = (MapTile) inO.readObject();
							visualMap.updateTile(receivedTile);
							//pos.changeTile(receivedTile);
							
						} else if (response.getInstruction() == Response.Instructions.DIRECTION_CHANGED) {
							Object o = response.getOperand();
							ByteArrayInputStream in = new ByteArrayInputStream((byte[])o);
							ObjectInputStream inO = new ObjectInputStream(in);
							Coordinate coordinate = (Coordinate) inO.readObject();
							if(coordinate != null)
								visualMap.updateRobotPosition(coordinate);
							//pos.updatePosition(coordinate);
						
						}
						
					} catch (IOException e) {
						
						connected = false;
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						
						connected = false;
						e.printStackTrace();
					} catch (ClassCastException e) {
						
						connected = false;
						e.printStackTrace();
					} catch (NullPointerException e) {
						
						connected = false;
						e.printStackTrace();
					} catch (Exception e){
						
						connected = false;
						e.printStackTrace();
					}
					mapThreadRunning = false;
				}
			}
		});
		threadVisualMap.start();
	}

	
	/**
	 * Connects to the Commander Server
	 * Applying connections to Input Streams(To Receive) & Output Streams(To Send)
	 * 
	 * @param commanderAddress
	 * @param commanderPort
	 */
	private boolean connectToCommander(String commanderAddress,	String commanderPort) {

		try {
			clientCommander = new Socket(commanderAddress,	Integer.parseInt(commanderPort));
			System.out.println("Connected to Commander: "+ clientCommander.getPort());
			inCommander = new ObjectInputStream(clientCommander.getInputStream());
			outCommander = new ObjectOutputStream(clientCommander.getOutputStream());
			runCommanderThread();
			status.setText("Connection Successful");
			connectCommanderMenuItem.setText("Disconnect");
			return true;
		} catch (NumberFormatException e) {
			
			status.setText("Connection Attempt Failed");
		    //e.printStackTrace();
		JOptionPane.showMessageDialog(this, "NumberFormatException: Please make sure you have the correct Port Commander input");
		} catch (UnknownHostException e) {
			
			status.setText("Connection Attempt Failed");
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this, "UnknownHostException: Please make sure you have the correct Host Commander input");
		} catch (IOException e) {
			
			status.setText("Connection Attempt Failed");
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this, "IOException Commander Server: Please make sure you are connected to the Server");
		    
		}
		return false;
	}

	/**
	 * 
	 * Connects to the Map Server
	 * Applying connections to Input Streams(To Receive) & Output Streams(To Send)
	 * 
	 * @param mapServerAddress
	 * @param mapServerPort
	 */
	private boolean connectToMapServer(String mapServerAddress,
			String mapServerPort) {
		try {
			clientMapServer = new Socket(mapServerAddress,Integer.parseInt(mapServerPort));
			System.out.println("Connected to MapServer: "+ clientMapServer.getPort());
			outMapServer = new ObjectOutputStream(clientMapServer.getOutputStream());
			inMapServer = new ObjectInputStream(clientMapServer.getInputStream());
			runMapThread();
			status.setText("Connection Successful");
			connectMapServerMenuItem.setText("Disconnect");
			return true;
		} catch (NumberFormatException e) {
			
			status.setText("Connection Attempt Failed");
		    //e.printStackTrace();
		   JOptionPane.showMessageDialog(this, "NumberFormatException: Please make sure you have the correct Map Host input");
		} catch (UnknownHostException e) {
			status.setText("Connection Attempt Failed");
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this, "UnknownHostException: Please make sure you have the correct Map Port input");
		} catch (IOException e) {
			status.setText("Connection Attempt Failed");
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this, "IOException Map Server: Please make sure you are connected to the Server");
		} 
		
		return false;
	}

	/**
	 * Constructs the Popup Menu for Settings
	 */
	public void JPopUpMenuSettings() {

		settingsMenu = new JPopupMenuExtended();

		JMenuItem saveMapMenuItem = new JMenuItem("Save Map");
		saveMapMenuItem.setActionCommand("SAVE_MAP");
		saveMapMenuItem.addActionListener(this);
		saveMapMenuItem.setForeground(Color.CYAN);
		settingsMenu.add(saveMapMenuItem);

		JMenuItem loadMapMenuItem = new JMenuItem("Load Map");
		loadMapMenuItem.setActionCommand("LOAD_MAP");
		loadMapMenuItem.addActionListener(this);
		loadMapMenuItem.setForeground(Color.CYAN);
		settingsMenu.add(loadMapMenuItem);

		settingsMenu.addSeparator();

		connectCommanderMenuItem = new JMenuItemExtended("Connect");
		connectCommanderMenuItem.addActionListener(this);
		connectCommanderMenuItem.setForeground(Color.CYAN);

		connectMapServerMenuItem = new JMenuItemExtended("Connect");
		connectMapServerMenuItem.addActionListener(this);
		connectMapServerMenuItem.setForeground(Color.CYAN);
		
		setSpeedHigh = new JMenuItemExtended("High");
		setSpeedHigh.addActionListener(this);
		setSpeedHigh.setForeground(Color.CYAN);
		
		setSpeedMiddle = new JMenuItemExtended("Normal");
		setSpeedMiddle.addActionListener(this);
		setSpeedMiddle.setForeground(Color.CYAN);
		
		setSpeedLow = new JMenuItemExtended("Low");
		setSpeedLow.addActionListener(this);
		setSpeedLow.setForeground(Color.CYAN);
		
		j2DView = new JMenuItemExtended("Switch to 2D View");
		j2DView.addActionListener(this);
		j2DView.setForeground(Color.CYAN);
		
		j3DView = new JMenuItemExtended("Switch to 3D View");
		j3DView.addActionListener(this);
		j3DView.setForeground(Color.CYAN);
		
		marioTheme = new JMenuItemExtended("Mario Theme");
		marioTheme.addActionListener(this);
		marioTheme.setForeground(Color.CYAN);
		
		zeldaTheme = new JMenuItemExtended("Zelda Theme");
		zeldaTheme.addActionListener(this);
		zeldaTheme.setForeground(Color.CYAN);
		
		JMenu subSettingsConnectToCommanderMenu = new JMenu("Commander");
		subSettingsConnectToCommanderMenu.setForeground(Color.CYAN);
		subSettingsConnectToCommanderMenu.add(connectCommanderMenuItem);
		settingsMenu.add(subSettingsConnectToCommanderMenu);

		JMenu subSettingsConnectToMapServersettingsMenuItem = new JMenu(
				"Map Server");
		subSettingsConnectToMapServersettingsMenuItem.setForeground(Color.CYAN);
		subSettingsConnectToMapServersettingsMenuItem
				.add(connectMapServerMenuItem);
		settingsMenu.add(subSettingsConnectToMapServersettingsMenuItem);
		
		JMenu subSettingsSetSpeed = new JMenu("Set Speed");
		subSettingsSetSpeed.setForeground(Color.CYAN);
		subSettingsSetSpeed.add(setSpeedHigh);
		subSettingsSetSpeed.add(setSpeedMiddle);
		subSettingsSetSpeed.add(setSpeedLow);
		settingsMenu.add(subSettingsSetSpeed);
		
		JMenu subSwitchMapView = new JMenu ("Map View");
		subSwitchMapView.setForeground(Color.CYAN);
		subSwitchMapView.add(j2DView);
		subSwitchMapView.add(j3DView);
		settingsMenu.add(subSwitchMapView);

		JMenu subThemeSwitcher = new JMenu("Switch Theme");
		subThemeSwitcher.setForeground(Color.CYAN);
		subThemeSwitcher.add(marioTheme);
		subThemeSwitcher.add(zeldaTheme);
		settingsMenu.add(subThemeSwitcher);
		
		settingsMenu.addSeparator();
		JMenuItem constraintRobotCleaningTime = new JMenuItem("Constrain Cleaning Time");
		constraintRobotCleaningTime.setForeground(Color.CYAN);
		constraintRobotCleaningTime.addActionListener(this);
		settingsMenu.add(constraintRobotCleaningTime);
		
	}

	/**
	 * Method for Saving the map file 
	 * @param mapFile
	 */
	public void saveMap(byte[] mapFile) {

		int retrnOp = fc.showSaveDialog(null);
		if (retrnOp == JFileChooser.APPROVE_OPTION) {

			File file = fc.getSelectedFile();
			try {
				output = new ObjectOutputStream(new FileOutputStream(file));
				output.writeObject(mapFile);
				output.close();

			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		}

	}

	/**
	 * Method to Load the map file 
	 * @return
	 */
	public byte[] loadMap() {

		int retrnOp = fc.showOpenDialog(null);
		byte[] loadedMap = null;
		if (retrnOp == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			loadedMap = new byte[(int) file.length()];
			System.out.println(file.length());

			try {
				input = new ObjectInputStream(new FileInputStream(file));
				loadedMap = (byte[]) input.readObject();
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();			
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			} 
		}

		return loadedMap;

	}

	/**
	 * This method is used to send objects to the server 
	 * Sending through to the appropriate Output Stream, The Req Object and to the Server who is to receive it
	 * 
	 * @param outstream
	 * @param object
	 * @param server
	 */
	private boolean send(ObjectOutput outstream, Request object, Server server) {

		if (object != null) {
			try {
				outstream.writeObject(object);
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, "Object not transmitted");
				//e.printStackTrace();
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(this, "server "+ Server.COMMANDER.toString() + " is not available");
				//e.printStackTrace();
			}
		}
		return false;
	}


	/**
	 * The actionPerformed method to handle any action dealt by the user through the UI
	 */
	
	public void actionPerformed(ActionEvent e) {
		String actionText = (String) e.getActionCommand();

		CardLayout cl = (CardLayout) (innerPrimeCenter.getLayout());
		cl.show(innerPrimeCenter, actionText);
		Request req;

		if (actionText.equals("START")) {
			Session newSession = new CleaningSession("Full Cleaning",
					getDateTime(), null);

			req = new Request(Request.Instructions.ADD_SESSION, newSession);
			if(this.send(outCommander, req, Server.COMMANDER)){
				
				themeSound.startSound();
				status.setText("Robot Has Started Cleaning");
			}

		} else if (actionText.equals("STOP")) {
			
			req = new Request(Request.Instructions.STOP, null);
			if(this.send(outCommander, req, Server.COMMANDER)){
			
				themeSound.stopSound();
	            status.setText("Robot Has Stopped Cleaning");
	            
			}
			
		} else if (actionText.equals("CLOSE")) {
				
			System.exit(0);
				
		} else if (actionText.equals("MINIMIZE")) {
			this.setExtendedState(this.ICONIFIED);

		} else if (actionText.equals("SAVE_MAP")) {
			req = new Request(Request.Instructions.SAVE_MAP, null);
			this.send(outCommander, req, Server.COMMANDER);

		} else if (actionText.equals("LOAD_MAP")) {
			byte[] loadedMap = loadMap().clone();
			if (loadedMap != null) {
				req = new Request(Request.Instructions.LOAD_MAP, loadedMap);
				this.send(outCommander, req, Server.COMMANDER);
			}

		} else if (e.getSource().equals(connectCommanderMenuItem)
				&& connectCommanderMenuItem.getText().equals("Connect")) {
			
			
			status.setText("Attempting to Connect to Commander");
			
			if (clientCommander == null || outCommander == null
					|| inCommander == null) {

				ConnectionPreferences pref = this.loadConnectionPreferences(COMMANDER_PREFERENCE_FILENAME);
				
				String address;
				String port;
				
				try{
					address = pref.getAddress();
					port = pref.getPort();
				}
				catch(NullPointerException ex){
					address = "localhost";
					port = "9002";
				}
				
				ServerAddressPanel serverAddressPanel = new ServerAddressPanel(address, port);
				if (JOptionPane.showConfirmDialog(this, serverAddressPanel,	"Please Enter Server Address & Port Number", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

					if (!serverAddressPanel.checkInput()) {
						JOptionPane.showMessageDialog(this,
								"Please input Address & Port");
					} else {
						connectToCommander(serverAddressPanel.getAddress(),
								serverAddressPanel.getPort());
						if(serverAddressPanel.getMakeDefault()){
							this.saveConnectionPreference(COMMANDER_PREFERENCE_FILENAME, 
									new ConnectionPreferences(serverAddressPanel.getAddress(), serverAddressPanel.getPort()));
						}

					}
				}
			} }else if (e.getSource().equals(connectCommanderMenuItem)
					&& connectCommanderMenuItem.getText().equals("Disconnect")) {
				if (JOptionPane
						.showConfirmDialog(this,
								"You are disconnecting from Commander \n do you wish to continue?") == JOptionPane.YES_OPTION) {
					req = new Request(Request.Instructions.DISCONNECT, null);
					this.send(outCommander, req, Server.COMMANDER);
					try {
						this.disconnectServer(Server.COMMANDER);
						status.setText("Attempting to Connect Commander");
						
					} catch (IOException ex) {
						
						ex.printStackTrace();
						status.setText("Disconnect Failed Commander");
					}
				}

			} else if (e.getSource().equals(connectMapServerMenuItem)
					&& connectMapServerMenuItem.getText().equals("Connect")) {
				
				status.setText("Attempting to Connect to Map Server");
				
				ConnectionPreferences pref = this.loadConnectionPreferences(COMMANDER_PREFERENCE_FILENAME);
				
				String address;
				String port;
				
				try{
					address = pref.getAddress();
					port = pref.getPort();
				}
				catch(NullPointerException ex){
					address = "localhost";
					port = "9001";
				}
				
				ServerAddressPanel serverAddressPanel1 = new ServerAddressPanel(address, port);
				if (JOptionPane.showConfirmDialog(this, serverAddressPanel1,
						"Please Enter Server Address & Port Number",
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

					if (!serverAddressPanel1.checkInput()) {
						JOptionPane.showMessageDialog(this,
								"Please input Address & Port");
					} else {
						connectToMapServer(serverAddressPanel1.getAddress(),serverAddressPanel1.getPort());
						if(serverAddressPanel1.getMakeDefault()){
							this.saveConnectionPreference(MAPSERVER_PREFERENCE_FILENAME, 
									new ConnectionPreferences(serverAddressPanel1.getAddress(), serverAddressPanel1.getPort()));
						}
						
					}
				}

			} else if (e.getSource().equals(connectMapServerMenuItem)
					&& connectMapServerMenuItem.getText().equals("Disconnect")) {
				
				if (JOptionPane
						.showConfirmDialog(this,
								"You are disconnecting from Map Server \n do you wish to continue?") == JOptionPane.YES_OPTION) {
					req = new Request(Request.Instructions.DISCONNECT, null);
					this.send(outMapServer, req, Server.MAP_SERVER);
					try {
						this.disconnectServer(Server.MAP_SERVER);
						status.setText("Attempting to Disconnect MAP");
					} catch (IOException ex) {
						
						status.setText("Disconnect Failed MAP");
						ex.printStackTrace();
						
					}
				}

			}else if (actionText.equals("Constrain Cleaning Time")) {

				ConstrainCleaningPanel constrainCleaningpanel = new ConstrainCleaningPanel();

				int option = JOptionPane.showConfirmDialog(this,
						constrainCleaningpanel,
						"Please Enter Constrained Cleaning Times",
						JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					if (constrainCleaningpanel.checkConstrainTime() == 1) {
						JOptionPane.showMessageDialog(this,
								"End Time cannot be Set before Start Time");
					}else if(constrainCleaningpanel.checkConstrainTime() == 0){
						JOptionPane.showMessageDialog(this,
								"Start Time cannot be equal to End Time");
					}else{
					ConstrainCleaning cc = new ConstrainCleaning(constrainCleaningpanel.getStart(), constrainCleaningpanel.getEnd());
					
					req = new Request(Request.Instructions.CONSTRAIN_ClEANING_TIME,cc);
					this.send(outCommander, req, Server.COMMANDER);
										
					}
					
				}
			 }else if(actionText.equals("SCHEDULE")){
				 rbtScrn.removeMapScreen();
				 if(visualMap instanceof VisualMap3D){
					 VisualMap tempMap = new VisualMap2D();
					 tempMap.updateMap(visualMap.getMap());
					 visualMap = tempMap;
					 was3DMap = true;
				 }
				 else{
					 was3DMap = false;
				 }
				 schdulScrn.setMapScreen(visualMap);
				 visualMap.setClickable(true);
				 
			  }else if(actionText.equals("RETURN")){
				  schdulScrn.removeMapScreen();
				  
				  if(was3DMap){
					  VisualMap tempMap = new VisualMap3D();
					  tempMap.updateMap(visualMap.getMap());
					  if(visualMap instanceof VisualMap2D){
						  ((VisualMap2D)visualMap).killUpdateThread();
					  }
					  visualMap = tempMap;
				  }
				  rbtScrn.setMapScreen(visualMap);
				  visualMap.setClickable(false);
				  visualMap.resetSelection();
				  visualMap.clearSelection();
				  
			  }else if(e.getSource().equals(setSpeedHigh)){
				  
				  req = new Request(Request.Instructions.SET_SPEED, new Double(0.45));
				  this.send(outCommander, req, Server.COMMANDER);
				  
			  }else if(e.getSource().equals(setSpeedMiddle)){
				  
				  req = new Request(Request.Instructions.SET_SPEED, new Double(0.30));
				  this.send(outCommander, req, Server.COMMANDER);
				  
			  }else if(e.getSource().equals(setSpeedLow)){
				  
				  req = new Request(Request.Instructions.SET_SPEED, new Double(0.15));
				  this.send(outCommander, req, Server.COMMANDER);
				  
			  }else if(e.getSource().equals(j2DView)){
				  if(viewIs3D){
					  viewIs2D = true;
					  viewIs3D = false;
					  
					  MapTile[][]tempArray = visualMap.getMap();
					  Coordinate position = visualMap.getRobotPosition();
					  visualMap = new VisualMap2D();
					  visualMap.updateMap(tempArray);
					  visualMap.updateRobotPosition(position);
					  rbtScrn.setMapScreen(visualMap);
				  
				  }else{
					 JOptionPane.showMessageDialog(this, "View is already set to 2D mode");
				  }
				  

			  }else if(e.getSource().equals(j3DView)){
				  
				  if(viewIs2D){
					  viewIs3D = true;
					  viewIs2D = false;
					  MapTile[][] tempArray = visualMap.getMap();
					  Coordinate position = visualMap.getRobotPosition();
					  if(visualMap instanceof VisualMap2D){
						  ((VisualMap2D)visualMap).killUpdateThread();
					  }
					 
					  visualMap = new VisualMap3D();
					  visualMap.updateMap(tempArray);
					  visualMap.updateRobotPosition(position);				  
					  rbtScrn.setMapScreen(visualMap);
				  }else{
					JOptionPane.showMessageDialog(this, "View is already set to 3D mode");  
				  }
			  }else if(e.getSource().equals(marioTheme)){
				  
				  visualMap.setTheme("mario");
				  if(themeSound.isRunning()) {
						themeSound.stopSound();
					  	themeSound = new SoundThread(ImgSource.themePath + "/start.wav");
					  	themeSound.startSound();
				  	  } else
				  		themeSound = new SoundThread(ImgSource.themePath + "/start.wav");

				  MapTile[][]tempArray = visualMap.getMap();
				  Coordinate position = visualMap.getRobotPosition();
				  
				  if(viewIs2D)
					  visualMap = new VisualMap2D();
				  else if(viewIs3D)
					  visualMap = new VisualMap3D();
				  
				  visualMap.updateMap(tempArray);
				  visualMap.updateRobotPosition(position);
				  rbtScrn.setMapScreen(visualMap);
//				  MapTile[][] tempArray = visualMap.getMap();
//				  Coordinate position = visualMap.getRobotPosition();
//				  visualMap = new VisualMap3D();
//				  visualMap.updateMap(tempArray);
//				  visualMap.updateRobotPosition(position);
//				  rbtScrn.setMapScreen(visualMap);
				  
				  
			  }else if(e.getSource().equals(zeldaTheme)){
				  
				  visualMap.setTheme("zelda");
				  if(themeSound.isRunning()) {
					themeSound.stopSound();
				  	themeSound = new SoundThread(ImgSource.themePath + "/start.wav");
				  	themeSound.startSound();
			  	  } else
			  		themeSound = new SoundThread(ImgSource.themePath + "/start.wav");
				  
				  MapTile[][]tempArray = visualMap.getMap();
				  Coordinate position = visualMap.getRobotPosition();
				  
				  if(viewIs2D)
					  visualMap = new VisualMap2D();
				  else if(viewIs3D)
					  visualMap = new VisualMap3D();
				  
				  visualMap.updateMap(tempArray);
				  visualMap.updateRobotPosition(position);
				  rbtScrn.setMapScreen(visualMap);
				  
//				  MapTile[][] tempArray = visualMap.getMap();
//				  Coordinate position = visualMap.getRobotPosition();
//				  visualMap = new VisualMap3D();
//				  visualMap.updateMap(tempArray);
//				  visualMap.updateRobotPosition(position);
//				  rbtScrn.setMapScreen(visualMap);
				  
			  }
		
		
			 }
	
	
	
	/**
	 * This method deals with disconnecting from the Server properly 
	 * Closing connections where need be
	 * @param commander
	 * @throws IOException
	 */
	private void disconnectServer(Server commander) throws IOException {
		switch (commander) {
		case COMMANDER:
			outCommander.close();
			inCommander.close();
			inCommander.close();
			outCommander = null;
			inCommander = null;
			clientCommander = null;
			connectCommanderMenuItem.setText("Connect");
			break;
		case MAP_SERVER:
			outMapServer.close();
			outMapServer.close();
			outMapServer.close();
			outMapServer = null;
			outMapServer = null;
			outMapServer = null;
			connectMapServerMenuItem.setText("Connect");
			break;
		}

	}

	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		point.x = e.getX();
		point.y = e.getY();

	}

	
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		Point p = this.getLocation();
		this.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);

	}

	
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Deals with sending new Sessions to the Servers
	 * @param cleaningSession
	 */
	
	public void newSession(Session cleaningSession) {
		// TODO Auto-generated method stub
	
		Request req = new Request(Request.Instructions.ADD_SESSION, cleaningSession.clone());
		System.out.println(req.getOperand());
		this.send(outCommander, req, Server.COMMANDER);
		
	}

	/**
	 * Sends the Session which is to be removed by the Server
	 * @param removeSession
	 */
	
	public void removeSession(Session removeSession) {
		// TODO Auto-generated method stub
		Request req = new Request(Request.Instructions.REMOVE_SESSION,removeSession.clone());
		System.out.println(req.getOperand());
		this.send(outCommander, req, Server.COMMANDER);
	}
	
	private void saveConnectionPreference(String file, ConnectionPreferences pref){
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(new File(file)));
			out.writeObject(pref);
			out.close();
		} catch (FileNotFoundException e) {} 
		catch (IOException e) {}
	}
	
	private ConnectionPreferences loadConnectionPreferences(String file){
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(file)));
			ConnectionPreferences p = (ConnectionPreferences)in.readObject();
			in.close();
			return p;
		} 
		catch (FileNotFoundException e) {} 
		catch (IOException e) {} 
		catch (ClassNotFoundException e) {}
		return null;
	}

}