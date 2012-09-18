package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


import map.MapStatsListener;

import robot.RobotStatsListener;

import utilities.Request;
import utilities.Response;
import utilities.Session;

public class StatsServer implements RobotStatsListener, MapStatsListener, SessionStatsListener, Runnable{

	public static final int PORT = 9000;
	private ServerSocket serverSocket = null;
	private ArrayList<ObjectOutputStream> outputStreams;
	private boolean running = false;
	private Thread statsServerThread = null;
	private int turns;
	private double emptyTiles;
	private double cleanedTiles;
	private int stalls;
	private int moves;

	public StatsServer(int port) {
		outputStreams = new ArrayList<ObjectOutputStream>();
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Stats Server is running ");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		statsServerThread = new Thread(this);
	}
	
	public void resetStats(){
		turns = 0;
		emptyTiles = 0;
		cleanedTiles = 0;
		stalls = 0;
		moves = 0;
		
	}
	
	public void run(){
		while (true) {
			try {
				final Socket clientSocket = serverSocket.accept();
				System.out.println("Client found: " + clientSocket);
				
				Thread th = new Thread(new Runnable(){
					
					public void run() {
						try {
							ObjectOutputStream objectOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
							ObjectInputStream objectInStream = new ObjectInputStream(clientSocket.getInputStream());
							outputStreams.add(objectOutStream);

							while (true) {

								while (objectInStream != null) {
									Request request = null;
									try {
										request = (Request) objectInStream
												.readObject();
										System.out.println(request
												.getInstruction());
									} catch (Exception ex) {
										outputStreams.remove(objectOutStream);
										return;
									}
									if (request.getInstruction() == Request.Instructions.DISCONNECT) {
										outputStreams.remove(objectOutStream);
										objectInStream.close();
										objectOutStream.close();
										objectInStream = null;
										objectOutStream = null;
									}
								}
							}

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				th.start();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
		
	}

	public void runServer() {		
		if (!running) {
			statsServerThread.start();
			running = true;
		}
	}

	
	public void directionChanged(int newOrientation) {
		turns++;
		Response response = new Response(Response.Instructions.DIRECTION_CHANGED, new Integer(turns));
		for(int i = 0; i < outputStreams.size(); i++){
			try {
				outputStreams.get(i).writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
	}

	
	public void tileCleaned(int totalCleaned, int totalEmpty) {
		cleanedTiles = totalCleaned;
		emptyTiles = totalEmpty;
		double percentage = 0.0;
		try{
			percentage = (cleanedTiles / emptyTiles) * 100.0;
		}
		catch(Exception ex){}
		Response response = new Response(Response.Instructions.UPDATE_TILE, new Double(percentage));
		for(int i = 0; i < outputStreams.size(); i++){
			try {
				outputStreams.get(i).writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		
	}

	
	public void stalled() {
		stalls++;
		Response response = new Response(Response.Instructions.COLLISIONS_DETECTED, new Integer(stalls));
		for(int i = 0; i < outputStreams.size(); i++){
			try {
				outputStreams.get(i).writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		
	}

	
	
	public void newMap() {
		Response response = new Response(Response.Instructions.NEW_MAP, null);
		for(int i = 0; i < outputStreams.size(); i++){
			try {
				outputStreams.get(i).writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
	}
	

	
	public void newSession(Session session) {
		resetStats();
		Response response = new Response(Response.Instructions.NEW_SESSION, null);
		for(int i = 0; i < outputStreams.size(); i++){
			try {
				outputStreams.get(i).writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
	}

	
	public void moved() {
		moves++;
		Response response = new Response(Response.Instructions.ROBOT_MOVED, new Integer(moves));
		for(int i = 0; i < outputStreams.size(); i++){
			try {
				outputStreams.get(i).writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		
	}
}