package controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import map.Coordinate;
import map.Map;
import map.MapTile;
import map.MapTileType;
import map.NeighbourTilePosition;
import robot.CollisionListener;
import robot.Orientation;
import robot.Robot;
import robot.SonarType;
import utilities.Clock;
import utilities.Log;
import algorithms.Coverage;
import algorithms.CoverageAlgorithm;
import algorithms.Exploring;
import algorithms.ExploringAlgorithm;
import algorithms.PathFinding;
import algorithms.PathFindingAlgorithm;

/**
 * An object of this class acts as a medium through which instructions are sent
 * to the {@link Robot}. This encapsulates the robot and makes decisions as to
 * whether to move the robot, turn, or deal with a collision. Details of any
 * cleaning sessions, path traversing and collision avoidance are also
 * encapsulated within this class.
 * 
 * @author mayank
 * 
 */
public class Controller implements CollisionListener{
	
	private Map map;
	private Robot robot;
	private ControllerMode mode = ControllerMode.IDLE;
	private PathFindingAlgorithm pathFinding = null;
	private CoverageAlgorithm coverage = null;
	private ExploringAlgorithm exploring = null;
	private static final double COLLISION_MOVE_DIST = 0.05;
	private static final double COLLISION_TURN_SPEED = 0.3;
	private static final int COLLISION_ANGLE = 5;
	private static final double COLLISION_MOVE_SPEED = 0.08;
	private static final long COLLISION_AVOIDANCE_SLEEP_TIME = 1000;
	private boolean interrupted;
	private boolean collided;
	private boolean hadCollided;
	
	/**
	 * A default constructor of this class which sets the robot and the map
	 * @param map the {@link Map}
	 * @param robot the robot {@link Robot}
	 */
	public Controller(Map map, Robot robot){
		this.setMap(map);
		this.setRobot(robot);
		interrupted = false;
		collided = false;
	}
	
	/**
	 * A method to start a full cleaning session
	 */
	public void startCleaning(){
		interrupted = false;
		cleanTile(map.getTile(map.getRobotPosition()));
		MapTile next = new MapTile();
		setMode(ControllerMode.COVERING);
		MapTile temp = null;
		while(next != null){
			while(robot.isCollided())try{Thread.sleep(100);}catch(InterruptedException e){}
			LinkedList<Integer> path = new LinkedList<Integer>();
			try{
				if(hadCollided){
					if(next.isCleanable()){
						if(temp == next){
							next = coverage.getNextTileToCover(map, robot.getOrientation());
							path = pathFinding.getPath(map, map.getTile(map.getRobotPosition()), next);
						}
						else {
							path = pathFinding.getPath(map, map.getTile(map.getRobotPosition()), next);
						}
						temp = next;
					}
					else{
						next = coverage.getNextTileToCover(map, robot.getOrientation());
						path = pathFinding.getPath(map, map.getTile(map.getRobotPosition()), next);
					}
					hadCollided = false;
				}
				else if(getMode() != ControllerMode.OBSTACLE_AVOIDANCE){
					setMode(ControllerMode.COVERING);
					next = coverage.getNextTileToCover(map, robot.getOrientation());
					setMode(ControllerMode.PATH_FINDING);
					path = pathFinding.getPath(map, map.getTile(map.getRobotPosition()), next);
				}
			}
			catch(Exception e){
				
				e.printStackTrace();
			}
			
			if(getMode() == ControllerMode.INTERRUPTED || interrupted){
				setMode(ControllerMode.IDLE);
				returnToStart();
				return;
			}
			else if(traversePath(path)){
				cleanTile(next);
			}
		}
		
		if(!interrupted){
			LinkedList<Coordinate> tilesToExplore = exploring.getTilesToExplore(map);
			exploreTiles(tilesToExplore);
			this.cleanTiles(tilesToExplore);
		}
		resetMode();
		interrupted = false;
		coverage.resetAlgorithm();
		returnToStart();
	}
	
	public void returnToStart(){
		LinkedList<Integer> path = pathFinding.getPath(map, map.getTile(map.getRobotPosition()), map.getTile(map.getChargingStation()));
		while(!traversePath(path)){
			path = pathFinding.getPath(map, map.getTile(map.getRobotPosition()), map.getTile(map.getChargingStation()));
		}
	}
	
	private void exploreTiles(LinkedList<Coordinate> tilesToExplore) {
		while(!tilesToExplore.isEmpty()){
			Coordinate c = tilesToExplore.remove();
			MapTile tile = map.getTile(c);
			MapTileType oldType = tile.getType();
			tile.setType(MapTileType.EMPTY);
			LinkedList<Integer> path = pathFinding.getPath(map, map.getTile(map.getRobotPosition()), tile);
			tile.setType(oldType);
			if(interrupted){
				return;
			}
			else if(traversePath(path)){
				this.cleanTile(map.getTile(c));
			}
			
		}
	}

	/**
	 * Starts a partial cleaning session cleaning only the tiles specified in the formal parameters
	 * @param tiles A collection of {@link Coordinate} objects representing the tiles to clean only.
	 */
	public void cleanTiles(List<Coordinate> tiles){
		setMode(ControllerMode.CLEANING);
		int i = 0;
		while(i < tiles.size()){
			LinkedList<Integer> path = pathFinding.getPath(map, map.getTile(map.getRobotPosition()), map.getTile(tiles.get(i)));
			while(collided)try{Thread.sleep(100);}catch(InterruptedException e){}
			if(getMode() == ControllerMode.INTERRUPTED || interrupted){
				resetMode();
				returnToStart();
				return;
			}
			else if(traversePath(path)){
				cleanTile(map.getTile(tiles.get(i)));
				setMode(ControllerMode.COVERING);
				i++;
			}
		}
		resetMode();
		interrupted = false;
		coverage.resetAlgorithm();
	}
	
	private void cleanTile(MapTile tile){
		setMode(ControllerMode.CLEANING);
		Coordinate robotPosition = map.getRobotPosition();
		map.cleanTile(robotPosition, tileCleanedAtDate());
		resetMode();
		

	}
	
	private boolean traversePath(LinkedList<Integer> path){
		setMode(ControllerMode.PATH_TRAVERSING);
		while(!path.isEmpty()){
			int targetOrientation = path.remove(0).intValue();
			if(getMode() == ControllerMode.OBSTACLE_AVOIDANCE || collided){
				return false;
			}
			else if(getMode() == ControllerMode.INTERRUPTED){
				return false;
			}
			else{
				robot.moveToDirection(targetOrientation, 1);
				map.printMap();
			}
		}
		
		if(getMode() == ControllerMode.OBSTACLE_AVOIDANCE || collided){
			return false;
		}
		resetMode();
		return true;
	}
	
	private Date tileCleanedAtDate(){
		return (Clock.getClock()).getCalendar().getTime();
	}
	
	/**
	 * Terminates a cleaning session
	 */
	public synchronized void stopCleaning(){
		setMode(ControllerMode.INTERRUPTED);
		interrupted = true;
		coverage.resetAlgorithm();
	}
	
	private void resetMode(){
		mode = ControllerMode.IDLE;
	}
	
	private void setMode(ControllerMode newMode){
		mode = newMode;
	}
	
	private ControllerMode getMode(){
		return mode;
	}
	
	/**
	 * Sets a {@link CoverageAlgorithm} to an object of this class
	 * @param c an implementation of {@link CoverageAlgorithm}
	 */
	public void setCoverageAlgorithm(CoverageAlgorithm c){
		this.coverage = c;
	}

	/**
	 * Sets a {@link EcploringAlgorithm} to an object of this class
	 * @param c an implementation of {@link ExploringAlgorithm}
	 */
	public void setExploringAlgorithm(ExploringAlgorithm e){
		this.exploring = e;
	}
	
	/**
	 * Sets a {@link PathFindingAlgorithm} to an object of this class
	 * @param c an implementation of {@link CoverageAlgorithm}
	 */
	public void setPathFindingAlgorithm(PathFindingAlgorithm pf){
		this.pathFinding = pf;
	}

	/**
	 * Gets the {@link Map} that the robot is operating in
	 * @return the map
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Sets the {@link Map} that the robot is operating in
	 * @param floorplan an object of {@link Map}
	 */
	public void setMap(Map floorplan) {
		this.map = floorplan;
	}

	/**
	 * Gets the robot that is being used
	 * @return the robot 
	 */
	public Robot getRobot() {
		return robot;
	}

	/**
	 * Sets the robot to be used
	 * @param robot an object of {@link Robot}
	 */
	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	
	
	public void collided(int angle) {
		System.out.println("Collided");
		setMode(ControllerMode.OBSTACLE_AVOIDANCE);
		collided = true;
		do{
			robot.refreshSonarValues();
			double front = robot.getSonarValueAt(SonarType.EXPLORER_FRONT);
			double frontFine = Math.min(robot.getSonarValueAt(SonarType.FINE_FRONT_LEFT), robot.getSonarValueAt(SonarType.FINE_FRONT_RIGHT));
			double frontFineRight = robot.getSonarValueAt(SonarType.FINE_FRONT_RIGHT);
			double frontFineLeft = robot.getSonarValueAt(SonarType.FINE_FRONT_LEFT);
			double frontAverage = (frontFineLeft + frontFineRight) / 2;
			double left = robot.getSonarValueAt(SonarType.EXPLORER_LEFT);
			double leftFine = Math.min(robot.getSonarValueAt(SonarType.FINE_LEFT_BACK), robot.getSonarValueAt(SonarType.FINE_LEFT_FRONT));
			double right = robot.getSonarValueAt(SonarType.EXPLORER_RIGHT);
			double rightFine = Math.min(robot.getSonarValueAt(SonarType.FINE_RIGHT_BACK), robot.getSonarValueAt(SonarType.FINE_RIGHT_FRONT));
			double rear = robot.getSonarValueAt(SonarType.FINE_REAR_CENTER);
			double rearFine = Math.min(robot.getSonarValueAt(SonarType.FINE_REAR_LEFT), robot.getSonarValueAt(SonarType.FINE_REAR_RIGHT));
			double rearMax = Math.max(robot.getSonarValueAt(SonarType.FINE_REAR_LEFT), robot.getSonarValueAt(SonarType.FINE_REAR_RIGHT));

			if(angle % 90 == 0){
				if(front <= 0){
					robot.moveDistance(-COLLISION_MOVE_DIST);
				} else if(rear <= 0 || rearFine <= 0){
					if(frontFine <= 0 && rearFine <= 0){
						robot.moveDistance(COLLISION_MOVE_DIST);
						if(robot.isCollided())
							robot.moveDistance(-COLLISION_MOVE_DIST);
					} else
						robot.moveDistance(COLLISION_MOVE_DIST);
				} else if(left <=0){
					System.out.println("Collided left");
//					robot.turnDegrees(COLLISION_ANGLE);
//					robot.moveDistance(-COLLISION_MOVE_DIST);
//					robot.turnDegrees(-COLLISION_ANGLE);
					avoidLeft(robot);
					recover(robot);
					
				} else if(right <= 0){
					System.out.println("Collided right  rear " + rear);
//					robot.turnDegrees(360 - COLLISION_ANGLE);
//					robot.moveDistance(-COLLISION_MOVE_DIST);
//					robot.turnDegrees(COLLISION_ANGLE);
					avoidRight(robot);
					recover(robot);
					
				} else if (frontFine <= 0) {
					robot.moveDistance(-COLLISION_MOVE_DIST);
				}
			} else {
				if (rearFine <= 0){
					Log.add(this, "Collided at the back while turning, with angle : " + angle);
					robot.moveDistance(COLLISION_MOVE_DIST);
				} else {
					Log.add(this, "Collided at the front while turning, with angle : " + angle);
					robot.moveDistance(-COLLISION_MOVE_DIST);
				}
			}
		}while(robot.isCollided());
		resetMode();
		collided = false;
		hadCollided = true;

	}
	
	public static void main(String[] args){
		Robot r = new Robot("192.168.0.15", 6665);
		r.runThreaded(-1, -1);
		Map m = new Map();
		r.addMoveListener(m);
		Controller c = new Controller(m, r);
		c.setCoverageAlgorithm(new Coverage());
		c.setExploringAlgorithm(new Exploring());
		c.setPathFindingAlgorithm(new PathFinding());
		r.addCollisionListener(c);
		c.startCleaning();
//		while(!r.isCollided()){
//			r.turnDegrees(270);
//		}
		
		System.out.println("terminated");
		
		
	}
	
	private static void avoidLeft(Robot r){
		r.setSpeed(COLLISION_MOVE_SPEED, COLLISION_TURN_SPEED);
		try {
			Thread.sleep(COLLISION_AVOIDANCE_SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.setSpeed(0.0, 0.0);
	}
	
	private static void avoidRight(Robot r){
		r.setSpeed(COLLISION_MOVE_SPEED, -COLLISION_TURN_SPEED);
		try {
			Thread.sleep(COLLISION_AVOIDANCE_SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.setSpeed(0.0, 0.0);
	}
	
	private static void recover(Robot r){
		r.moveDistance(-COLLISION_MOVE_DIST * 1.5);
		r.getOrientation();
	}
	
	
	
	
	
}