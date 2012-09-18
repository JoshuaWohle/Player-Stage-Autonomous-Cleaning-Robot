package statisticsUI;

import ui.ConnectionPreferences;
import ui.GradientPanel;
import ui.ImageButton;
import ui.JMenuItemExtended;
import ui.JPopupMenuExtended;
import ui.ServerAddressPanel;
import ui.UI;
import utilities.Request;
import utilities.Response;
import visualmap.VisualMap2D;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
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

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;

import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;



import map.Coordinate;
import map.MapTile;

/**
 * This is the Class is the interface between the user and the Robot's Statistics
 * Here they can choose to save screen shots and check statistics based on the Robot's 
 * Cleaning effectiveness
 * 
 * @author Team Cyan
 *
 */
public class StatisticsUI extends UI implements ActionListener,MouseListener,MouseMotionListener{

	
	public enum Server{
		STATS_SERVER, MAP_SERVER
	}
	
	private boolean pathReady;
	private String screenshotPath = "";
	
	private Socket clientStatsServer;
	private ObjectOutputStream outStatsServer = null;
	private ObjectInputStream inStatsServer = null;

	private Socket clientMapServer;
	private ObjectOutputStream outMapServer = null;
	private ObjectInputStream inMapServer = null;
	
	private Color clrIN = new Color(00,33,48);
	
	private TimeSeries cleaningOverTimeData;
	private TimeSeries collisionsOverTimeData;
	private TimeSeries turnsOverTimeData;
	private TimeSeries movesOverTimeData;
	
	private ImageButton close,minimize,settingsEUI;
	
	private JMenuItemExtended connectStatsServerMenuItem, connectMapServerMenuItem,startScreenshots;
	
	private JPopupMenu settingsMenuEUI;
	
	private GradientPanel northPrime,northPrimeWest,northPrimeEast,southPrime;
	
	private LineChart cleaningOverTime,collisionsOverTime,turnsOverTime,movesOverTime; 
	
	private JPanel statsGraphPanel,centerPrime,innerCenterPrime;
	
	private JLabel progressBarTitle;
	
	private JLabel euiTitle, emptyNorth, emptyWest, emptySouth, emptyEast;;
	
	private static Point point = new Point();
	
	private Container container;
	
	private VisualMap2D visualMap;
	
	private JProgressBar progressBar = new JProgressBar(0,100);;
	
	private JPanel progressBarContainer;
	
	private Font font;
	
	private JFileChooser selectDirectoryForScreenShots;
	private String selectDirectoryForScreenShotsTitle;
	
	private Color gradientColorLightEUI = new Color(43, 86, 102);
	private Color gradientColorDarkEUI = new Color(21, 60, 75);
	
	private final String STATSSERVER_PREFERENCE_FILENAME = "StatServerConnectionPreferencesEUI";
	private final String MAPSERVER_PREFERENCE_FILENAME = "MapServerConnectionPreferencesEUI";
	private boolean statsServerThreadRunning;
	private boolean mapServerThreadRunning;
	
	
	
	public static void main(String [] args){
		
		StatisticsUI test = new StatisticsUI("localhost", "9000", "9001");
		
	}
	
    /**
     * The constructor to create the Statistics UI 
     */
    public StatisticsUI(String address,String portStats,String portMap){
    	
		super();
		mapServerThreadRunning = false;
		statsServerThreadRunning = false;
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
    	this.setUndecorated(true);
    	
    	visualMap = new VisualMap2D();
    	visualMap.setClickable(false);
    	
    	font = new Font("sansserif",Font.BOLD,15);
    	
        emptyNorth = new JLabel(" ");
        emptyWest = new JLabel(" ");
        emptySouth = new JLabel(" ");
        emptyEast = new JLabel(" ");
    	
        euiTitle = new JLabel("Evaluation Software");
		euiTitle.setFont(font);
		euiTitle.setForeground(Color.CYAN);
		
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		progressBarTitle = new JLabel("Progress: ");
		progressBarTitle.setFont(font);
		progressBarTitle.setForeground(Color.CYAN);
		
		progressBarContainer = new JPanel();
		progressBarContainer.setBackground(clrIN);
		progressBarContainer.setLayout(new FlowLayout());
		progressBarContainer.add(progressBarTitle);
		progressBarContainer.add(progressBar);
		
   	
    	close = new ImageButton("CLOSE", "/close.png", "/close.png");
    	close.addActionListener(this);
    	minimize = new ImageButton("MINIMIZE","/minimize.png","/minimize.png");
    	minimize.addActionListener(this);
    	
    	northPrimeWest = new GradientPanel(1000,70,gradientColorLightEUI,gradientColorDarkEUI);
    	northPrimeWest.setLayout(new GridLayout(2,1));
    	northPrimeWest.add(time);
    	northPrimeWest.add(date);
    	
    	northPrimeEast = new GradientPanel(1000, 70, gradientColorLightEUI, gradientColorDarkEUI);
    	northPrimeEast.setLayout(new FlowLayout());
    	northPrimeEast.add(minimize);
    	northPrimeEast.add(close);
    	
    	northPrime = new GradientPanel(1000,70,gradientColorLightEUI,gradientColorDarkEUI);
    	northPrime.setLayout(new GridLayout(1,3,400,400));
    	northPrime.add(northPrimeWest);
    	northPrime.add(euiTitle);
    	northPrime.add(northPrimeEast);
    	
    	popUpMenuEUI();
    	
    	settingsEUI = new ImageButton("SETTINGS", "/settings-off.png", "/settings-on.png");
		settingsEUI.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {

				settingsMenuEUI.show(thisFrame,580,570);
			}
		});
    	
    	createSeries();
    	graphStats();
    	
    	statsGraphPanel = new JPanel();
    	statsGraphPanel.setBackground(clrIN);
    	statsGraphPanel.setLayout(new GridLayout(2,2,15,15));
   
    	statsGraphPanel.add(cleaningOverTime);
    
    	statsGraphPanel.add(collisionsOverTime);
    	
    	statsGraphPanel.add(movesOverTime);

    	statsGraphPanel.add(turnsOverTime);
    	
    	
    	
    	innerCenterPrime = new JPanel();
    	innerCenterPrime.setBackground(clrIN);
    	innerCenterPrime.setLayout(new BorderLayout());
    	innerCenterPrime.add(progressBarContainer,BorderLayout.NORTH);
    	innerCenterPrime.add(statsGraphPanel,BorderLayout.WEST);
    	innerCenterPrime.add(visualMap,BorderLayout.CENTER);
    	
    	centerPrime = new JPanel();
    	centerPrime.setBackground(clrIN);
    	centerPrime.setLayout(new BorderLayout());
    	centerPrime.add(innerCenterPrime,BorderLayout.CENTER); 	
    	centerPrime.add(emptyNorth,BorderLayout.NORTH);
    	centerPrime.add(emptyWest,BorderLayout.WEST);
    	centerPrime.add(emptyEast,BorderLayout.EAST);
    	centerPrime.add(emptySouth,BorderLayout.SOUTH);

    	southPrime = new GradientPanel(1000, 70, gradientColorLightEUI, gradientColorDarkEUI);
        southPrime.setLayout(new FlowLayout());
        southPrime.add(settingsEUI);
        
    	container = new Container();
    	container.setLayout(new BorderLayout());
    	container.add(northPrime,BorderLayout.NORTH);
    	container.add(centerPrime,BorderLayout.CENTER);
    	container.add(southPrime,BorderLayout.SOUTH);
    	
    	
    	
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
    	this.setContentPane(container);
		this.setSize(1300,700);
		this.setLocationRelativeTo(null);
		ConnectionPreferences statsP = null;
		ConnectionPreferences mapServerP = null;
		
		try{
			statsP = this.loadConnectionPreferences(STATSSERVER_PREFERENCE_FILENAME);
			System.out.println(statsP);
			this.connectToStatsServer(statsP.getAddress(), statsP.getPort());
		}
		catch(NullPointerException e){
			if(this.connectToStatsServer(address, portStats)){
				this.saveConnectionPreference(STATSSERVER_PREFERENCE_FILENAME, new ConnectionPreferences(address, portStats));
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
     * The constructor used to create a pop up menu for settings
     */
     public void popUpMenuEUI(){
    	
		settingsMenuEUI = new JPopupMenuExtended();


		connectStatsServerMenuItem = new JMenuItemExtended("Connect");
		connectStatsServerMenuItem.addActionListener(this);
		connectStatsServerMenuItem.setForeground(Color.CYAN);

		connectMapServerMenuItem = new JMenuItemExtended("Connect");
		connectMapServerMenuItem.addActionListener(this);
		connectMapServerMenuItem.setForeground(Color.CYAN);
		
		startScreenshots = new JMenuItemExtended("Start Screenshots");
		startScreenshots.addActionListener(this);
		startScreenshots.setForeground(Color.CYAN);


		settingsMenuEUI.addSeparator();
		JMenu subSettingsConnectToStatsServerMenu = new JMenu("Stats Server");
		subSettingsConnectToStatsServerMenu.setForeground(Color.CYAN);
		subSettingsConnectToStatsServerMenu.add(connectStatsServerMenuItem);
		settingsMenuEUI.add(subSettingsConnectToStatsServerMenu);

		settingsMenuEUI.addSeparator();
		JMenu subSettingsConnectToMapServersettingsMenuItem = new JMenu("Map Server");
		subSettingsConnectToMapServersettingsMenuItem.setForeground(Color.CYAN);
		subSettingsConnectToMapServersettingsMenuItem.add(connectMapServerMenuItem);
		settingsMenuEUI.add(subSettingsConnectToMapServersettingsMenuItem);
		
		settingsMenuEUI.addSeparator();
		JMenu subSettingsScreenShotsMenuItem = new JMenu("Screenshots");
		subSettingsScreenShotsMenuItem.setForeground(Color.CYAN);
		subSettingsScreenShotsMenuItem.add(startScreenshots);
		settingsMenuEUI.add(subSettingsScreenShotsMenuItem);
    	
		settingsMenuEUI.addSeparator();
		JMenuItemExtended selectDirectoryToSaveScreenShots = new JMenuItemExtended("Select Screenshots Directory");
		selectDirectoryToSaveScreenShots.addActionListener(this);
		
    	
    }
    
     
     /**
      * 
      * Used to specify the location for screen shots to save and whether the directory for screen shots was valid
      * @return boolean
      */
    private boolean selectDirectoryForScreenShots(){
    	  	
    	selectDirectoryForScreenShots = new JFileChooser();
    	selectDirectoryForScreenShots.setCurrentDirectory(new java.io.File("."));
    	selectDirectoryForScreenShots.setDialogTitle(selectDirectoryForScreenShotsTitle);
    	selectDirectoryForScreenShots.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	
    	
    		
    		int retrnOp = selectDirectoryForScreenShots.showSaveDialog(null);
    		if (retrnOp == JFileChooser.APPROVE_OPTION) {

    			File file = selectDirectoryForScreenShots.getSelectedFile();
                screenshotPath = file.getAbsolutePath();
                this.pathReady = true;
    		
    		return true;
    	
    	
    	}
		return false;	
    }
     
    /**
     * Used to connect to the Commander server
     * 
     * @param commanderAddress - passing the I.P. address 
     * @param commanderPort - passing the port number 
     * @throws UnknownHostException
     * @throws IOException
     */
    private boolean connectToStatsServer(String statAddress, String statPort){
    	
    	
		try {
			clientStatsServer = new Socket(statAddress,	Integer.parseInt(statPort));
			System.out.println("Connected to Commander: "+ clientStatsServer.getPort());
			inStatsServer = new ObjectInputStream(clientStatsServer.getInputStream());
			outStatsServer = new ObjectOutputStream(clientStatsServer.getOutputStream());
			runStatsServerThread();
			connectStatsServerMenuItem.setText("Disconnect");
			return true;
		} catch (NumberFormatException e) {
		    //e.printStackTrace();
		JOptionPane.showMessageDialog(this, "NumberFormatException: Please make sure you have the correct Port Stat input");
		} catch (UnknownHostException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this, "UnknownHostException: Please make sure you have the correct Host Stat input");
		} catch (IOException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this, "IOException Stat Server: Please make sure you are connected to the Server");
		    
		}
		return false;
	}

    /**
     * Used to connect to the Map Server 
     * 
     * @param mapServerAddress - connect to specified I.P. address
     * @param mapServerPort - connect to specified port
     * @throws UnknownHostException
     * @throws IOException
     */
	private boolean connectToMapServer(String mapServerAddress, String mapServerPort){
		try {
			clientMapServer = new Socket(mapServerAddress,Integer.parseInt(mapServerPort));
			System.out.println("Connected to MapServer: "+ clientMapServer.getPort());
			outMapServer = new ObjectOutputStream(clientMapServer.getOutputStream());
			inMapServer = new ObjectInputStream(clientMapServer.getInputStream());
			runMapThread();
			connectMapServerMenuItem.setText("Disconnect");
			return true;
		} catch (NumberFormatException e) {
		    //e.printStackTrace();
		   JOptionPane.showMessageDialog(this, "NumberFormatException: Please make sure you have the correct Map Host input");
		} catch (UnknownHostException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this, "UnknownHostException: Please make sure you have the correct Map Port input");
		} catch (IOException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this, "IOException Map Server: Please make sure you are connected to the Server");
		} 
		
		return false;
	}
	
	
	
	
	private void send(ObjectOutput outstream, Request object, Server server) {

		if (object != null) {
			try {
				outstream.writeObject(object);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, "Data not transmitted");
				//e.printStackTrace();
				
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(this, "server "+ Server.STATS_SERVER.toString() + " is not available");

				//e.printStackTrace();

			}
		}

	}

	
	/**
	 * Thread used to listen for any updates from the StatsServer 
	 */
	private void runStatsServerThread() {
		if(statsServerThreadRunning){
			return;
		}
		Thread threadStatsServer = new Thread(new Runnable() {
			boolean connected = clientStatsServer.isConnected();
			public void run() {
				statsServerThreadRunning = true;
				while (connected) {
					try {
						Response response = null;

						try {
							response = (Response) inStatsServer.readObject();
						} catch (Exception e) {
							connected = false;
							disconnectServer(Server.STATS_SERVER);
						}

						switch (response.getInstruction()) {
						case DIRECTION_CHANGED:
							turnsOverTime.addToSeries(turnsOverTimeData,new FixedMillisecond(System.currentTimeMillis()),((Integer) response.getOperand()).intValue());
							break;

						case ROBOT_MOVED:
							movesOverTime.addToSeries(movesOverTimeData,new FixedMillisecond(System.currentTimeMillis()),((Integer) response.getOperand()).intValue());
							break;

						case COLLISIONS_DETECTED:
							collisionsOverTime.addToSeries(collisionsOverTimeData,new FixedMillisecond(System.currentTimeMillis()),((Integer) response.getOperand()).intValue());
							break;

						case UPDATE_TILE:
							double p = ((Double) response.getOperand()).doubleValue();
							progressBar.setValue((int) p);
							cleaningOverTime.addToSeries(cleaningOverTimeData,new FixedMillisecond(System.currentTimeMillis()), p);
							break;

						case NEW_SESSION:
							cleaningOverTime.removeSeries(cleaningOverTimeData);
							collisionsOverTime
									.removeSeries(collisionsOverTimeData);
							movesOverTime.removeSeries(movesOverTimeData);
							turnsOverTime.removeSeries(turnsOverTimeData);
							createSeries();
							cleaningOverTime.addSeries(cleaningOverTimeData,
									Color.CYAN);
							collisionsOverTime.addSeries(
									collisionsOverTimeData, Color.CYAN);
							movesOverTime.addSeries(movesOverTimeData,
									Color.CYAN);
							turnsOverTime.addSeries(turnsOverTimeData,
									Color.CYAN);
							break;
						}
					} catch (ClassCastException e) {
						connected = false;
						e.printStackTrace();
					} catch (NullPointerException e) {
						connected = false;
						e.printStackTrace();
					} catch (Exception e) {
						connected = false;
						e.printStackTrace();

					}
				}
				statsServerThreadRunning = false;
			}
		});
		threadStatsServer.start();
	}
    
	/**
	 * Thread used to update the map the robot produces
	 */
	private void runMapThread() {
		if(mapServerThreadRunning){
			return;
		}
		Thread threadVisualMap = new Thread(new Runnable() {
			boolean connected = clientMapServer.isConnected();
			
			public void run() {
				mapServerThreadRunning = true;
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
							
						} else if (response.getInstruction() == Response.Instructions.UPDATE_TILE) {
							Object o = response.getOperand();
							ByteArrayInputStream in = new ByteArrayInputStream((byte[])o);
							ObjectInputStream inO = new ObjectInputStream(in);
							MapTile receivedTile = (MapTile) inO.readObject();
							visualMap.updateTile(receivedTile);
							
						} else if (response.getInstruction() == Response.Instructions.DIRECTION_CHANGED) {
							Object o = response.getOperand();
							ByteArrayInputStream in = new ByteArrayInputStream((byte[])o);
							ObjectInputStream inO = new ObjectInputStream(in);
							Coordinate coordinate = (Coordinate) inO.readObject();
							visualMap.updateRobotPosition(coordinate);
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
				}
				if(pathReady){
					visualMap.takeScreenShot(screenshotPath);
				}
				mapServerThreadRunning = false;
			}
		});
		threadVisualMap.start();
	}
	
	
	/**
	 * Initialising the graph series 
	 */
    public void graphStats(){
    	
    	cleaningOverTime = new LineChart("Percentage Cleaned Over Time","Time","Percentage");
    	cleaningOverTime.addSeries(cleaningOverTimeData, Color.CYAN);
    	   	
    	collisionsOverTime = new LineChart("Collisions Over Time","Time","Collision");
    	collisionsOverTime.addSeries(collisionsOverTimeData, Color.CYAN);
    	
    	turnsOverTime = new LineChart("Turns Over Time","Time","Turns");
    	turnsOverTime.addSeries(turnsOverTimeData, Color.CYAN);
    		
    	movesOverTime = new LineChart("Moves Over Time","Time","Moves");
    	movesOverTime.addSeries(movesOverTimeData, Color.CYAN);
    }
    
    private void createSeries(){
    	cleaningOverTimeData = new TimeSeries("PCOT");
    	collisionsOverTimeData = new TimeSeries("COT");
    	turnsOverTimeData = new TimeSeries("TOT");
    	movesOverTimeData = new TimeSeries("MOT");	
    }
    

    /**
     * Method used to listen for any action events the user may activate
     */
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String actionText = (String)e.getActionCommand();
		
		Request req;
		
		if(actionText.equals("CLOSE")){
			
			System.exit(0);
			
		}else if(actionText.equals("MINIMIZE")){
			
			this.setExtendedState(this.ICONIFIED);
			
		}else if (e.getSource().equals(connectStatsServerMenuItem)&& connectStatsServerMenuItem.getText().equals("Connect")) {
			if (clientStatsServer == null || outStatsServer == null|| inStatsServer == null) {
				
				ConnectionPreferences pref = this.loadConnectionPreferences(MAPSERVER_PREFERENCE_FILENAME);
				
				String address;
				String port;
				
				try{
					address = pref.getAddress();
					port = pref.getPort();
				}catch(NullPointerException ex){
					address = "localhost";
					port = "9000";
				}

					ServerAddressPanel serverAddressPanel = new ServerAddressPanel(address,port);
									
					if(JOptionPane.showConfirmDialog(this, serverAddressPanel,"Please Enter Server Address & Port Number",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
					       {
						
					        	if(!serverAddressPanel.checkInput()){
					    			JOptionPane.showMessageDialog(this,	"Please input Address & Port");
					        	}else{
					  
					        		
						     connectToStatsServer(serverAddressPanel.getAddress(),serverAddressPanel.getPort());
						    
						     if(serverAddressPanel.getMakeDefault()){
						    	 
						    	 this.saveConnectionPreference(STATSSERVER_PREFERENCE_FILENAME, new ConnectionPreferences(serverAddressPanel.getAddress(), serverAddressPanel.getPort()));
						     }
					        	
					      }
					   }
			}    			
									
		} else if (e.getSource().equals(connectStatsServerMenuItem) && connectStatsServerMenuItem.getText().equals("Disconnect")) {

			if (JOptionPane
					.showConfirmDialog(this,
							"You are disconnecting from Stas Server \n do you wish to continue?") == JOptionPane.YES_OPTION) {
				req = new Request(Request.Instructions.DISCONNECT, null);
				this.send(outStatsServer, req, Server.STATS_SERVER);
				try {
					this.disconnectServer(Server.STATS_SERVER);
					
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
		} else if (e.getSource().equals(connectMapServerMenuItem)&& connectMapServerMenuItem.getText().equals("Connect")) {
			
			if (clientMapServer == null || outMapServer == null
					|| inMapServer == null){
			
			ConnectionPreferences pref = this.loadConnectionPreferences(MAPSERVER_PREFERENCE_FILENAME);
			
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
					connectToMapServer(serverAddressPanel1.getAddress(),
							serverAddressPanel1.getPort());
					if(serverAddressPanel1.getMakeDefault()){
						this.saveConnectionPreference(MAPSERVER_PREFERENCE_FILENAME, 
								new ConnectionPreferences(serverAddressPanel1.getAddress(), serverAddressPanel1.getPort()));
					}
					
				}
			}
		}

		} else if (e.getSource().equals(connectMapServerMenuItem) && connectMapServerMenuItem.getText().equals("Disconnect")) {
			
			if (JOptionPane
					.showConfirmDialog(this,
							"You are disconnecting from Map Server \n do you wish to continue?") == JOptionPane.YES_OPTION) {
				req = new Request(Request.Instructions.DISCONNECT, null);
				this.send(outMapServer, req, Server.MAP_SERVER);
				try {
					this.disconnectServer(Server.MAP_SERVER);
				} catch (IOException ex) {
					ex.printStackTrace();
					
				}
			}
		} else if(actionText.equals("Start Screenshots")){
		
			selectDirectoryForScreenShots();
			
		}
		
		
		
	}

	
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		Point p = this.getLocation();
		this.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
		
	}

	
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		point.x = e.getX();
		point.y = e.getY();
		
	}

	
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method is used to save the connection preference the user has specified
	 * @param file
	 * @param pref
	 */
	private void saveConnectionPreference(String file, ConnectionPreferences pref){
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(new File(file)));
			out.writeObject(pref);
			out.close();
		} catch (FileNotFoundException e) {} 
		catch (IOException e) {}
	}
	
	/**
	 * loads the connection preference the user has specified
	 * @param file
	 * @return
	 */
	private ConnectionPreferences loadConnectionPreferences(String file){
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(file)));
			ConnectionPreferences p = (ConnectionPreferences) in.readObject();
			in.close();
			return p;
		} 
		catch (FileNotFoundException e) {} 
		catch (IOException e) {} 
		catch (ClassNotFoundException e) {}
		return null;
	}
	
	
	/**
	 * Closes all connections properly
	 * @param commander
	 * @throws IOException
	 */
	private void disconnectServer(Server commander) throws IOException {
		switch (commander) {
		case STATS_SERVER:
			outStatsServer.close();
			inStatsServer.close();
			inStatsServer.close();
			outStatsServer = null;
			inStatsServer = null;
			clientStatsServer = null;
			connectStatsServerMenuItem.setText("Connect");
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
	
}