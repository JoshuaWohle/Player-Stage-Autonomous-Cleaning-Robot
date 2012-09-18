package server;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import map.Coordinate;
import map.Map;
import map.MapTile;
import map.MapTileType;

import controller.Controller;


import robot.Robot;


import utilities.Clock;
import utilities.ConstrainTime;

import utilities.CleaningSession;
import utilities.Response;
import utilities.Session;
import utilities.Request;
import utilities.TickListener;

/**
 * {@link Commander} is a multi-threaded and multi-client server to which
 * clients can connect in order schedule cleaning sessions, obtain the Map along
 * with all the cleaning sessions for that map to save to disk or to load a Map
 * and all its sessions. Any clients of this server that wish to communicate
 * with it must construct a {@link Request} and write to its ObjectOutputStream.
 * The following instructions are only interpreted by this server:-
 * @
 * 
 * 
 * @author mayank
 * 
 */
public class Commander implements Runnable, TickListener {
	
	/**
	 * Default port this server operates on
	 */
	public static final int PORT = 9002;

	private Controller controller;
	private ServerSocket serverSocket = null;
	private Thread commanderThread;
	private Queue<Session> cleaningSessions;
	private Session currentlyRunningSession = null;
	private Map map;
	private Robot robot;
	private ArrayList<ObjectOutputStream> outputStreams;
	private ArrayList<SessionStatsListener> statsListener;
	private boolean running = false;
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;

	/**
	 * Default constructor for this server that sets up relevant threads,
	 * linking the map and the robot and setting up the {@link ServerSocket}
	 * 
	 * @param port
	 * @param controller
	 * @param map
	 * @param robot
	 */
	public Commander(int port, Controller controller, Map map, Robot robot) {

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Commander is running");
		} catch (IOException e) {
			System.out.println("Port already used by another program");
			
			e.printStackTrace();
			System.exit(1);
		}
		commanderThread = new Thread(this);
		this.map = map;
		this.robot =  robot;
		this.controller = controller;
		cleaningSessions = new PriorityQueue<Session>();
		Clock.getClock().addTickListener(this);
		outputStreams = new ArrayList<ObjectOutputStream>();
		statsListener = new ArrayList<SessionStatsListener>();
		
		startHour = 25;
		startMinute = 61;
		endHour = -1;
		endMinute = -1;
		
	}

	
	public void run() {
		while (true) {
			try {
				final Socket clientSocket = serverSocket.accept();
				Thread th = new Thread(new Runnable() {
					
					public void run() {
						System.out.println("New Client: " + clientSocket);
						try {
							 ObjectOutputStream objectOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
							 ObjectInputStream objectInStream = new ObjectInputStream(clientSocket.getInputStream());
							 outputStreams.add(objectOutStream);
							
							while(true){
								Request request = null;
								Object object = null;
								
								try {
									object = objectInStream.readObject();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									outputStreams.remove(objectOutStream);
									return;
								}
								
								if(object.getClass() == Request.class) {
									request = (Request) object;
									System.out.println(request.getInstruction());
									try{
										switch(request.getInstruction()){
											case ADD_SESSION:
												cleaningSessions.add((Session) request.getOperand());
												objectOutStream.writeObject(new Response(Response.Instructions.SESSIONS_LIST, getIteratedSessions()));
												break;
												
											case REMOVE_SESSION:
												cleaningSessions.remove(request.getOperand());
												if(currentlyRunningSession != null){
													controller.stopCleaning();
												}
												objectOutStream.writeObject(new Response(Response.Instructions.SESSIONS_LIST, getIteratedSessions()));
												break;
												
											case STOP:
												controller.stopCleaning();
												break;
											
											case LOAD_MAP:
												controller.stopCleaning();
												byte[] mapBytesToLoad = (byte[]) request.getOperand();
												ByteArrayInputStream bytesIn = new ByteArrayInputStream(mapBytesToLoad);
												ObjectInput objectIn = new ObjectInputStream(bytesIn);
												try{
													Map newMap = (Map)objectIn.readObject();
													map.setId(newMap.getId());
													map.setName(newMap.getName());
													map.setRobotPosition(newMap.getRobotPosition());
													map.setChargingStation(newMap.getChargingStation());
													map.setMap(newMap.getMap());
													map.getTile(map.getChargingStation()).setType(MapTileType.EMPTY);
													map.getTile(map.getRobotPosition()).setType(MapTileType.EMPTY);
													Queue<Session> newCleaningSessions = new PriorityQueue<Session>();
													while(objectIn.available() > 0){
														Session s = (Session) objectIn.readObject();
														ArrayList<Coordinate> pC = s.getPermittedTiles();
														for(int i = 0; i < pC.size(); i++){
															MapTile t = map.getTile(pC.get(i));
															pC.set(i, t.getCoordinates());
														}
														newCleaningSessions.add(s);
													}
													cleaningSessions = newCleaningSessions;
													bytesIn.close();
													objectIn.close();
												}
												catch(IOException ex){
													String errorStr = "Could not read file, please try again later";
													Response errorResponse = new Response(Response.Instructions.ERROR, errorStr);
													objectOutStream.writeObject(errorResponse);
												}
												break;
												
											case SAVE_MAP:
												try{
												ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
												ObjectOutput objectOut = new ObjectOutputStream(bytesOut);   
												objectOut.writeObject(map.clone());	
												Iterator<Session> it = cleaningSessions.iterator();
												while (it.hasNext()){
													objectOut.writeObject(it.next().clone());
												}
												objectOut.flush();
												
												byte[] mapBytesToSave = bytesOut.toByteArray();
												bytesOut.close();
												objectOut.close();
												Response response = new Response(Response.Instructions.SAVE_MAP, mapBytesToSave.clone());
												objectOutStream.writeObject(response);
												}
												catch(IOException ex){
													String errorStr = "Could not save map, please try again later";
													Response errorResponse = new Response(Response.Instructions.ERROR, errorStr);
													objectOutStream.writeObject(errorResponse);
												}
												break;
												
											case DISCONNECT:
												outputStreams.remove(objectOutStream);
												objectInStream.close();
												objectOutStream.close();
												objectInStream = null;
												objectOutStream = null;
												break;
												
											case CONSTRAIN_ClEANING_TIME:
												ConstrainTime constrainTimes = (ConstrainTime)request.getOperand();
												setStartTime(constrainTimes.getStart());
												setEndTime(constrainTimes.getEnd());
												break;
											case SET_SPEED:
												robot.setDefaultRobotSpeed(((Double)request.getOperand()).doubleValue());
												break;
												
										}
										objectOutStream.flush();
									}
									catch(Exception ex){
										
										ex.printStackTrace();
										
									}
								} else if(object.getClass() == String.class) { // Treats commands through Strings
									String stringRequest = (String) object;
									if(stringRequest.equals("START"))
										cleaningSessions.add(new CleaningSession("Full Cleaning", Calendar.getInstance(), null));
									else if(stringRequest.equals("STOP"))
										controller.stopCleaning();
								}
							}
						} 
						catch (IOException e) {
							
							e.printStackTrace();
							System.out.println("Command not received");
						}
						catch(NullPointerException e){
							
							e.printStackTrace();
							System.out.println("Disconnected");
						}
					}
				});
				th.start();
			} 
			catch (IOException e) {
				
				e.printStackTrace();
				System.out.println("Client unable to connect to clients, please restart Commander");
			}
		}
	}
	
	private synchronized void setStartTime(Calendar startDate) {
		startHour = startDate.get(Calendar.HOUR_OF_DAY);
		startMinute = startDate.get(Calendar.MINUTE);
	}
	
	private synchronized void setEndTime(Calendar endDate) {
		endHour = endDate.get(Calendar.HOUR_OF_DAY);
		endMinute = endDate.get(Calendar.MINUTE);
	}
	
	public void runServer() {
		if(!running){
			running = true;
			commanderThread.start();
		}
	}
//
	
	public void tick(final Calendar t) {
		try{
			int hour = t.get(Calendar.HOUR_OF_DAY);
			int min = t.get(Calendar.MINUTE);
			Calendar sessionDateTime = cleaningSessions.peek().getDateTime();
			int comparison = Clock.compareDateTime(sessionDateTime);
			
			int now = (hour * 100) + min;
			int start = (this.startHour * 100) + this.startMinute;
			int end = (this.endHour * 100) + this.endMinute;
			
			if(now >= start && now < end){
				
				if(currentlyRunningSession == null && comparison == 0){
					Session session = cleaningSessions.remove();
					session.setStatus("DROPPED");
					Response response = new Response(Response.Instructions.DROPPED_SESSION, session.clone());
					
					for(int i = 0; i < outputStreams.size(); i++){
						try {
							outputStreams.get(i).writeObject(response);
							outputStreams.get(i).writeObject(new Response(Response.Instructions.SESSIONS_LIST, getIteratedSessions()));
						} catch (IOException e) {
						
							e.printStackTrace();
						}
					}
				}
				if(currentlyRunningSession != null){
					controller.stopCleaning();
				}
			}
			else if(currentlyRunningSession == null && comparison == 0){
				Session session = cleaningSessions.remove();
				executeSession(session);
				session.setStatus("EXECUTING");
				Response response1 = new Response(Response.Instructions.EXECUTING_SESSION, session.clone());
				for(int i = 0; i < outputStreams.size(); i++){
					try {
						outputStreams.get(i).writeObject(response1);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
			else if(currentlyRunningSession != null && comparison == 0){
				Session session = cleaningSessions.remove();
				List<Coordinate> permitTiles = session.getPermittedTiles();
				for(int i = 0; i < permitTiles.size(); i++){
					map.getTile(permitTiles.get(i)).setToBeCleaned(true);
				}
				currentlyRunningSession.getPermittedTiles().addAll(session.getPermittedTiles());
				session.setStatus("EXECUTING");
				Response response = new Response(Response.Instructions.EXECUTING_SESSION, session.clone());
				for(int i = 0; i < outputStreams.size(); i++){
					try {
						outputStreams.get(i).writeObject(response);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
		}
		catch(NullPointerException e1){
			
			//e1.printStackTrace();
		}

	}
	
	private synchronized void executeSession(final Session session){
		currentlyRunningSession = session;
		Thread th = new Thread(new Runnable() {
			
			public void run() {
				List<Coordinate> permittedTiles = session.getPermittedTiles();
				for(int i = 0; permittedTiles != null && i < permittedTiles.size(); i++){
					map.getTile(permittedTiles.get(i)).setToBeCleaned(true);
				}	
				
				if((session.getPermittedTiles() != null && session.getPermittedTiles().isEmpty()) || session.getPermittedTiles() == null ){
					controller.startCleaning();
				}
				else{
					controller.cleanTiles(session.getPermittedTiles());
				}
				currentlyRunningSession.setStatus("FINISHED");
				Response response = new Response(Response.Instructions.SESSION_TERMINATED, currentlyRunningSession);
				for(int i = 0; i < outputStreams.size(); i++){
					try {
						outputStreams.get(i).writeObject(response);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
				currentlyRunningSession = null;
				
				
			}
		});
		th.start();
		Response response = new Response(Response.Instructions.SESSIONS_LIST, getIteratedSessions());
		for(int i = 0; i < outputStreams.size(); i++){
			try {
				outputStreams.get(i).writeObject(response);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		for(int i = 0; i < statsListener.size(); i++){
			this.statsListener.get(i).newSession(session);
		}
	}
	
	private LinkedList<Session> getIteratedSessions(){
		LinkedList<Session> temp = new LinkedList<Session>();
		Iterator<Session> it = cleaningSessions.iterator();
		while(it.hasNext()){
			temp.add((Session) it.next().clone());
		}
		return temp;
	}
	
	public static void saveSessions(Map map, Queue<Session> sessions, File workingDir){
		File mapDir = new File(workingDir.getAbsolutePath() + File.separator + map.getId());
		if(!mapDir.exists()){
			mapDir.mkdir();
		}
		
		while(!sessions.isEmpty()){	
			try {
				Session c = (Session) sessions.remove().clone();
				File f = new File(mapDir.getAbsolutePath() + File.separator + c.getName() + ".ser");
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
				out.writeObject(c);
				out.close();
			} 
			catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
			catch (IOException e) {
			
				e.printStackTrace();
			}
		}
	}
	
	public static Queue<Session> loadSessions(File workingDir, Map map){
		Queue<Session> cleaningSessions = new PriorityQueue<Session>();
		File sessionsDir = new File(workingDir + File.separator + map.getId());
		
		if(sessionsDir.exists()){
			File[] sessions = sessionsDir.listFiles();
			for(int i = 0; i < sessions.length; i++){
				try {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(sessions[i]));
					Session s = (Session) in.readObject();
					cleaningSessions.add(s);
					in.close();
				} 
				catch (FileNotFoundException e) {
					
				e.printStackTrace();} 
				catch (IOException e) {
					
				e.printStackTrace();} 
				catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				}
			}
		}
		return cleaningSessions;
	}

	public void addSessionStatsListener(StatsServer statsServer) {
		this.statsListener.add(statsServer);
	}
	
	public void removeSessionStatsListener(StatsServer statsServer) {
		this.statsListener.remove(statsServer);
	}
	
}