package server;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


import map.Coordinate;
import map.Map;
import map.MapListener;
import map.MapTile;

import utilities.Log;
import utilities.Request;
import utilities.Response;

public class MapServer implements MapListener, Runnable{

	public static final int PORT = 9001;
	private ServerSocket serverSocket = null;
	private ArrayList<ObjectOutputStream> outputStreams;
	private Map map = null;
	private boolean running = false;
	private Thread mapServerThread = null;

	public MapServer(int port, Map map) {
		this.map = map;
		outputStreams = new ArrayList<ObjectOutputStream>();
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Map Server is running ");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		mapServerThread = new Thread(this);
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
							
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							ObjectOutputStream outO = new ObjectOutputStream(out);
							outO.writeObject(map.getMap());
							Response response = new Response(Response.Instructions.NEW_MAP, out.toByteArray());
							objectOutStream.writeObject(response);
							
							Log.add(this, "Initial map written");
							
							ByteArrayOutputStream out2 = new ByteArrayOutputStream();
							ObjectOutputStream outO2 = new ObjectOutputStream(out2);
							outO2.writeObject(map.getRobotPosition());
							Response response2 = new Response(Response.Instructions.DIRECTION_CHANGED, out2.toByteArray());
							objectOutStream.writeObject(response2);
							
							Log.add(this, "Initial robot position set");
							
							while(objectInStream != null){
								Request request = null;
								try{
									request = (Request) objectInStream.readObject();
									Log.add(this, request.getInstruction().toString());
								}
								catch(Exception ex){
									outputStreams.remove(objectOutStream);
									return;
								}
								if(request.getInstruction() == Request.Instructions.DISCONNECT){
									outputStreams.remove(objectOutStream);
									objectInStream.close();
									objectOutStream.close();
									objectInStream = null;
									objectOutStream = null;
								
								}
							}
						} catch (IOException e) {
							
						}catch (NullPointerException e) {
							
						} catch (Exception e){
							
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
			mapServerThread.start();
			running = true;
		}
	}

	
	public void updatedMap(Map map) {
		ObjectOutputStream o = null;
		ByteArrayOutputStream b = null;
		try {
			b = new ByteArrayOutputStream();
			o = new ObjectOutputStream(b);
			o.writeObject(map.getMap());
			o.flush();
		} catch (IOException e1) {
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println("MAP SENT");
		//map.printMap();
		MapTile[][] newMap = ((Map)map.clone()).getMap();
		Response response = new Response(Response.Instructions.NEW_MAP, b.toByteArray());
		try {
			for (int i = 0; i < outputStreams.size(); i++) {
				outputStreams.get(i).writeObject(response);
			}
		} catch (IOException e) {
			
		}
	}

	
	public void updatedMap(MapTile mapTile) {
		ObjectOutputStream o = null;
		ByteArrayOutputStream b = null;
		try {
			b = new ByteArrayOutputStream();
			o = new ObjectOutputStream(b);
			o.writeObject(mapTile);
			o.flush();
		} catch (IOException e1) {

			
			e1.printStackTrace();
			
		}
		//System.out.println(mapTile.getCoordinates() + " SENT");
		//MapTile newTile = (MapTile) mapTile.clone();
		Response response = new Response(Response.Instructions.UPDATE_TILE,	b.toByteArray());
		try {
			for (int i = 0; i < outputStreams.size(); i++) {
				outputStreams.get(i).writeObject(response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void directionMoved(Coordinate newCoordinate) {
		ObjectOutputStream o = null;
		ByteArrayOutputStream b = null;
		try {
			b = new ByteArrayOutputStream();
			o = new ObjectOutputStream(b);
			o.writeObject(newCoordinate);
			o.flush();
		} catch (IOException e1) {

			
			
			e1.printStackTrace();
		}
		
		System.out.println(newCoordinate + " RECEIVED COORDINATE");
		
		Response response = new Response(Response.Instructions.DIRECTION_CHANGED,	b.toByteArray());
		try {
			for (int i = 0; i < outputStreams.size(); i++) {
				outputStreams.get(i).writeObject(response);
			}
		} catch (IOException e) {
			
		}
	}
}