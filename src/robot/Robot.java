package robot;

import java.util.ArrayList;

import utilities.Log;
import javaclient3.PlayerClient;
import javaclient3.Position2DInterface;
import javaclient3.RangerInterface;
import javaclient3.structures.PlayerConstants;

/**
 * This class is the robot abstraction layer of the server and deals with connection to the player/stage environment.
 * An object of this class can be treated as the robot hardware. This encapsulates details such as sonar readings, sensor reading
 * off setting and operations to move the robot from one tile to another and turn the robot.
 * 
 * @author mayank
 *
 */
public class Robot extends PlayerClient{
	/**
	 * error 0.08
	 */
	public static final double ERROR0d8 = 0.08;
	/**
	 * error 0.1
	 */
	public static final double ERROR1 = 0.1;
	/**
	 * error 0.18
	 */
	public static final double ERROR1d8 = 0.18;
	/**
	 * error 0.2
	 */
	public static final double ERROR2 = 0.2;
	/**
	 * error 0.28
	 */
	public static final double ERROR2d8 = 0.28;
	/**
	 * error 0.3
	 */
	public static final double ERROR3 = 0.3;
	/**
	 * error 0.4
	 */
	public static final double ERROR4 = 0.4;
	/**
	 * error 0.5
	 */
	public static final double ERROR5 = 0.5;
	/**
	 * Max sensor reading of Fine rear right minus error
	 */
	public static final double MAX_FINE_REAR_RIGHT = 1.5 - ERROR0d8; 
	/**
	 * Max sensor reading of  minus error
	 */
	public static final double MAX_FINE_REAR_CENTER = 1.5 - ERROR0d8;
	/**
	 * Max sensor reading of {@literal Fine rear left} minus error
	 */
	public static final double MAX_FINE_REAR_LEFT = 1.5 - ERROR0d8; 
	public static final double MAX_FINE_FRONT_RIGHT = 1.5 - ERROR5;
	public static final double MAX_FINE_FRONT_LEFT = 1.5 - ERROR5;
	public static final double MAX_FINE_LEFT_BACK = 1.5 - ERROR1d8;
	public static final double MAX_FINE_LEFT_FRONT = 1.5 - ERROR1d8;
	public static final double MAX_FINE_RIGHT_BACK = 1.5 - ERROR1d8; 
	public static final double MAX_FINE_RIGHT_FRONT = 1.5 - ERROR1d8;
	public static final double MAX_OBSTACLE_CENTER = 4.0 - ERROR1;
	public static final double MAX_OBSTACLE_LEFT  = 4.0 - ERROR2;
	public static final double MAX_OBSTACLE_RIGHT = 4.0 - ERROR2;
	public static final double MAX_EXPLORER_FRONT = 10.0 - ERROR3; 
	public static final double MAX_EXPLORER_LEFT = 10.0 - ERROR2d8;
	public static final double MAX_EXPLORER_RIGHT = 10.0 - ERROR2d8;
	
	
	public double SPEED = 0.2;
	public static final double YAW_MULTIPLIER = 10.0;
	public static final double DEGREES_MULTIPLIER = 180.0 / (Math.PI * YAW_MULTIPLIER);
	public static final double TILE_SIZE = 0.4;
	public static final double MOVE_PRECISION = 0.02;
	public static final int COLLISION_THREAD_SLEEP_TIME = 10; // Miiliseconds

	private final int DEGREE_OFF = 1;
	private final double DEFAULT_TURN_SPEED = 0.5;

	private int initialOrientation = 0;
	private Position2DInterface pos2D = null;
	private RangerInterface sonar = null;
	private double[] sonarValues = null;
	private ArrayList<MoveListener> moveListeners = null;
	private ArrayList<RobotStatsListener> actuatorListeners = null;
	private ArrayList<SonarListener> sonarListeners = null;
	private CollisionListener collisionListeners = null;
	private RobotMode mode = null;
	private int latestMove = 0;
	
	// Remembers the latest move error and uses it to correct the next one
	private double lastMoveErrorForward = 0.0;
	private double lastMoveErrorBackward = 0.0;
	
	
	// Used to pause a turn in case we collide
	private boolean pauseTurning;
	private int storedCurrentDegrees;
	private int storedTargetDegrees;
	
	public void setDefaultRobotSpeed(double speed) {
		this.SPEED = speed;
	}
	
	/**
	 * Creates a new robot object
	 * @param server - the server the robot is on
	 * @param port - the port with which to communicate
	 */
	public Robot(String server, int port){
		super(server, port);
		this.setListeners();
		pos2D = this.requestInterfacePosition2D(0, PlayerConstants.PLAYER_OPEN_MODE);
		sonar = this.requestInterfaceRanger(0, PlayerConstants.PLAYER_OPEN_MODE);
		pauseTurning = false;
		
		Thread collisionThread = new Thread(new Runnable() {
			public void run(){
				while(true){
					boolean b = false;
					try{
						b = isCollided();
					}
					catch(NullPointerException ex){}
					if(b){
						//while(getMode() != RobotMode.IDLE);
						
						setSpeed(0.0, 0.0);
						System.out.println("--- Obstacle detected");
						for(int i = 0; i < actuatorListeners.size(); i++){
							actuatorListeners.get(i).stalled();
						}
						if(collisionListeners != null){
							collisionListeners.collided(getAngle());
							if(!isCollided() && pauseTurning){
								resumeTurning();
							}
						}
					}
					try{Thread.sleep(COLLISION_THREAD_SLEEP_TIME);}
					catch(InterruptedException ex){
						
						ex.printStackTrace();
					}
				}
			}
		});
		collisionThread.start();
		
		System.out.println("Connection successful and running");
		
	}
	private void setListeners() {
		sonarListeners = new ArrayList<SonarListener>();
		moveListeners = new ArrayList<MoveListener>();
		actuatorListeners = new ArrayList<RobotStatsListener>();
	}
	
	public int getLatestMove(){
		return latestMove;
	}
	
	public boolean isCollided(){
		return pos2D.getData().getStall() == 1;
	}
	
	public RobotMode getMode(){
		return mode;
	}
	
	private synchronized void setMode(RobotMode newMode){
		mode = newMode;
	}
	
	private synchronized void resetMode(){
		mode = RobotMode.IDLE;
	}

	private double[] refactorSonar(double[] sonars) {
		
		double[] refactoredSonar = new double[sonars.length];

		for (int i = 0; i < refactoredSonar.length; i++) {
			
			if(i <= 2)
				refactoredSonar[i] = sonars[i] - 0.08;
			else if(i == 3 || i == 4)
				refactoredSonar[i] = sonars[i] - 0.5;
			else if(i >= 5 && i <= 8)
				refactoredSonar[i] = sonars[i] - 0.18;
			else if(i == 9)
				refactoredSonar[i] = sonars[i] - 0.1;
			else if(i >= 10 && i <=11)
				refactoredSonar[i] = sonars[i] - 0.2;
			else if(i == 12)
				refactoredSonar[i] = sonars[i] - 0.3;
			else if(i >= 13)
				refactoredSonar[i] = sonars[i] - 0.28;
			
			if(refactoredSonar[i] < 0)
				refactoredSonar[i] = 0.0;
			
		}
		return refactoredSonar;
	}
	
	/**
	 * Gets the player/stage dependent 'x' coordinate
	 * @return
	 */
	public double getRobotX(){
		return pos2D.getX();
	}
	

	/**
	 * Gets the player/stage dependent 'y' coordinate
	 * @return
	 */
	public double getRobotY(){
		return pos2D.getY();
	}

	/**
	 * A method to get the sensor value for a particular sensor
	 * 
	 * @param i
	 *            the index of the sensor in the sonar array
	 * @return the sensor value
	 */
	public void refreshSonarValues() {
		while (!sonar.isDataReady()){
			try {
				Thread.sleep(1,1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		sonarValues = refactorSonar(sonar.getData().getRanges());
	}
	
	public double getSonarValueAt(SonarType type) {
		return sonarValues[type.ordinal()];
	}
	
	/**
	 * Stores the current and target angle (degrees) and pauses a turn (in case we've collided for example)
	 * @param currentDegrees
	 * @param targetDegrees
	 */
	private synchronized void pauseTurning(int currentDegrees, int targetDegrees){
		if(!pauseTurning){
			System.out.println("Pausing turn" + " c: " + currentDegrees + " t: " + targetDegrees );
			storedCurrentDegrees = currentDegrees;
			storedTargetDegrees = targetDegrees;
			pauseTurning = true;
		}
	}
	
	public double getSpeed(){
		return SPEED;
	}
	
	/**
	 * Resumes turning exactly where it left off
	 */
	public synchronized void resumeTurning(){
		System.out.println("Resuming turn");
		if(pauseTurning){
			pauseTurning = false;
			int diff = storedTargetDegrees - storedCurrentDegrees;
			
			if(diff < 0){
				turnDegrees(Orientation.EAST + diff);
			}
			else if(diff > 0){
				turnDegrees(diff);
			}
		}
	}

	/**
	 * A method to turn the robot by a specified angle at a specified speed
	 * 
	 * @param degrees the angle to turn by
	 */
	public synchronized void turnDegrees(int degrees){
		boolean b = false;
		if(isCollided()){
			resetMode();
			b = true;
		}
		int currentDegrees = getAngle();
		int targetDegrees = (currentDegrees + degrees) % 360;
		int angleDiff = Math.abs(currentDegrees - targetDegrees);
		double minTurnRate = 0.05;
		double maxTurnRate = 1.3;
		double turnRateConst = 60.0;

		if (degrees > 180) {
			minTurnRate = -minTurnRate;
			maxTurnRate = -maxTurnRate;
			turnRateConst = -turnRateConst;
		}

		while (angleDiff > 0) {
			setMode(RobotMode.TURNING);
			if (angleDiff <= 30 || angleDiff >= 338) {
				if (angleDiff <= 6 || angleDiff >= 340)
					setSpeed(0.0, minTurnRate);
				else 
					setSpeed(0.0, angleDiff / turnRateConst);
			} else
				setSpeed(0.0, maxTurnRate);
			
			currentDegrees = getAngle();
			angleDiff = (int) Math.round(Math.abs(currentDegrees - targetDegrees));
			if(!b){
				if(isCollided()){
					pos2D.setSpeed(0.0, 0.0);
					pauseTurning(currentDegrees, targetDegrees);
					resetMode();
					return;
				}
			}
			try {Thread.sleep(1);} 
			catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		setSpeed(0.0, 0.0);
		resetMode();
	}

	/**
	 * A method to determine the current angle of the robot in degrees (conversion from radians)
	 * 
	 * @return the angle of the robot
	 */
	public synchronized int getAngle() {
		double actualDegrees = 0.0;
		double yaw = 0.0;
		boolean yawFound = false;
		
		while(!yawFound){
			try {
				yaw = pos2D.getYaw() * YAW_MULTIPLIER;
				yawFound = true;
			} catch (NullPointerException e) {
				System.out.println("Error occurred at player/stage side, not our fault!");
				e.printStackTrace();
				setSpeed(0.0, 0.0);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {}
			} 
		}
		
		double degreesHALF = yaw * DEGREES_MULTIPLIER;
		if (degreesHALF < 0)
			actualDegrees = 360 + degreesHALF;
		else
			actualDegrees = degreesHALF;
	
		return (int) Math.round(actualDegrees);	}

	/**
	 * A method to set the speed and turn rate of the robot
	 * 
	 * @param s
	 *            the speed
	 * @param t
	 *            the turn rate
	 */
	public synchronized void setSpeed(double s, double t) {
		if(s == 0.0 && t == 0.0)
			setMode(RobotMode.IDLE);
		else if(s != 0.0 && t == 0.0)
			setMode(RobotMode.MOVING);
		else if(s == 0.0 && t != 0.0)
			setMode(RobotMode.TURNING);
		
		pos2D.setSpeed(s, t);
	}

	/**
	 * Moves the robot forward a specified number of steps (tiles)
	 * @param steps - the steps to move forward
	 */
	public synchronized void goForward(int steps) {
		for (int i = 0; i < steps; i++) {
			if(isCollided()){
				resetMode();
				return;
			}
			moveToFrontTile();
			notifyMoved();
		}
	}
	
	/**
	 * This is a test method used to log the sensor readings of the robot
	 * @param s
	 */
	private void logSensorReadings(String s){
		this.refreshSonarValues();
	
	}

	/**
	 * Moves the robot backwards the specified number of steps (tiles)
	 * @param steps - the amount of tiles to move
	 */
	private synchronized void goBack(int steps) {
		for (int i = 0; i < steps; i++) {
			if(isCollided()){
				resetMode();
				return;
			}
			moveToRearTile();
			notifyMoved();
		}
	}

	/**
	 * Turns the robot to face a certain direction
	 * @param direction
	 */
	public synchronized void turnToDirection(int direction) {
		setSpeed(0.0, 0.0);
		int currentDirection = getOrientation();
		
		if(!(Math.abs(currentDirection - direction) <= DEGREE_OFF)){
			switch (currentDirection) {
			case Orientation.EAST:
				turnDegrees(direction % Orientation.EAST);
				break;
			/*default: // Why is this here ??
				return;*/
			case Orientation.WEST:
				switch (direction) {
				case Orientation.EAST:
				turnDegrees(Orientation.WEST);
					break;
				case Orientation.NORTH:
				turnDegrees(Orientation.SOUTH);
					break;
				case Orientation.SOUTH:
				turnDegrees(Orientation.NORTH);
					break;
				default:
					return;
				}
				break;
			case Orientation.NORTH:
				switch (direction) {
				case Orientation.EAST:
				turnDegrees(Orientation.SOUTH);
					break;
				case Orientation.WEST:
				turnDegrees(Orientation.NORTH);
					break;
				case Orientation.SOUTH:
				turnDegrees(Orientation.WEST);
					break;
				default:
					return;
				}
				break;
			case Orientation.SOUTH:
				switch (direction) {
				case Orientation.EAST:
				turnDegrees(Orientation.NORTH);
					break;
				case Orientation.WEST:
				turnDegrees(Orientation.SOUTH);
					break;
				case Orientation.NORTH:
				turnDegrees(Orientation.WEST);
					break;
				default:
					return;
				}
				break;
			}
			
			if(!isCollided()){
				int newOrientation = getOrientation();
				if (currentDirection != newOrientation) {
					notifyDirectionChanged(newOrientation);
					notifySonarListeners();
				}
			}
			
		}
	}
	
	private void notifySonarListeners() {
		this.refreshSonarValues();
		for (int i = 0; i < moveListeners.size(); i++) {
			moveListeners.get(i).addSensorReading(
					getSonarValueAt(SonarType.EXPLORER_FRONT),
					getSonarValueAt(SonarType.FINE_FRONT_LEFT),
					getSonarValueAt(SonarType.FINE_FRONT_RIGHT),
					
					getSonarValueAt(SonarType.EXPLORER_RIGHT),
					getSonarValueAt(SonarType.FINE_RIGHT_BACK),
					getSonarValueAt(SonarType.FINE_RIGHT_FRONT),
					
					getSonarValueAt(SonarType.EXPLORER_LEFT),
					getSonarValueAt(SonarType.FINE_LEFT_BACK),
					getSonarValueAt(SonarType.FINE_LEFT_FRONT),
					
					getOrientation());
		}
	}
	
	private void notifyDirectionChanged(int orientation) {
		for (int i = 0; i < actuatorListeners.size(); i++) {
			actuatorListeners.get(i).directionChanged(orientation);
		}
	}
	
	private void notifyDirectionMoved() {
		latestMove = getOrientation();
		for (int i = 0; i < moveListeners.size(); i++){
			moveListeners.get(i).directionMoved(latestMove);
		}
	}
	
	private void notifyMoved() {
		for (int i = 0; i < actuatorListeners.size(); i++){
			this.actuatorListeners.get(i).moved();
		}
	}
	
	public synchronized void moveDistance(double distance){
		
		if(distance > 0){
			long time = (long) ((distance / (SPEED / 10.0)) * 1000);
			System.out.println(time);
			setSpeed(SPEED / 10, 0.0);
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		else if(distance < 0) {
			distance = Math.abs(distance);
			long time = (long) ((distance / (SPEED / 10.0)) * 1000);
			System.out.println(time);
			setSpeed(-SPEED / 10, 0.0);
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		setSpeed(0.0, 0.0);
		notifyMoved();
	}

	/**
	 * Moves the robot into a certain direction
	 * @param direction - the direction in which to move
	 * @param steps - the number of steps to move in the specified direction
	 */
	public synchronized void moveToDirection(int direction, int steps) {
		int currentOrientation = getOrientation();
		
		if(direction == Orientation.CURRENT){
			goForward(steps);
		}
		else if (Math.abs(direction - currentOrientation) == 180) {
			goBack(steps);
		} 
		else {
			turnToDirection(direction);
			goForward(steps);
		}
	}

	/**
	 * gets the current orientation of the robot in degrees, relative to a direction (NORTH; EAST; SOUTH; WEST)
	 * @return Orientation of the current orientation
	 */
	public synchronized int getOrientation() {

		if (Math.abs(getAngle() - 0) <= DEGREE_OFF) {
			return Orientation.EAST;
		} else if (Math.abs(getAngle() - 90) <= DEGREE_OFF) {
			return Orientation.NORTH;
		} else if (Math.abs(getAngle() - 180) <= DEGREE_OFF) {
			return Orientation.WEST;
		} else if (Math.abs(getAngle() - 270) <= DEGREE_OFF) {
			return Orientation.SOUTH;
		} 
		
		// find the nearest angle to current angle
		int currentAngle = getAngle();
		int difference = currentAngle;
		int THEangle = 0;

		for (int i = 90; i <= 360; i += 90) {
			if (Math.abs(currentAngle - i) < difference) {
				THEangle = i;
			}
			difference = Math.abs(currentAngle - i);
		}
		
		int dif = THEangle - currentAngle;
		
		if(dif > 0){
			turnDegrees(dif);
		}
		else{
			turnDegrees(Orientation.EAST + dif);
		}
		
		return THEangle;
	}

	/**
	 * Resets the current angle to a "normalised" value
	 */
	private synchronized void resetAngle() {
		int currentAngle = getAngle();
		if (currentAngle != 0) {
			int angleToTurn = 360 - currentAngle;
			turnDegrees(angleToTurn);
		}
	}

	/**
	 * Moves the robot exactly 1 tile to the front
	 */
	public synchronized void moveToFrontTile() {
		if(isCollided()){
			resetMode();
			return;
		}
		refreshSonarValues();
		double tempSensorReading = getSonarValueAt(SonarType.EXPLORER_FRONT);
	
		// Fallback method if we cannot use sensor data to base our move on
		if(tempSensorReading == MAX_EXPLORER_FRONT) {
			moveFrontTime();
			return;
		}
		
		setSpeed(SPEED, 0.0);
		setMode(RobotMode.MOVING);

		while(tempSensorReading - getSonarValueAt(SonarType.EXPLORER_FRONT) + lastMoveErrorForward < TILE_SIZE && tempSensorReading - getSonarValueAt(SonarType.EXPLORER_FRONT) > -0.2) {
			refreshSonarValues();
			if(Math.abs(tempSensorReading - getSonarValueAt(SonarType.EXPLORER_FRONT) + lastMoveErrorForward - TILE_SIZE) < MOVE_PRECISION)
				break;
			if(isCollided()){
				resetMode();
				setSpeed(0.0, 0.0);
				return;
			}
		}
		
		setSpeed(0.0, 0.0);
		lastMoveErrorForward += tempSensorReading - getSonarValueAt(SonarType.EXPLORER_FRONT) - TILE_SIZE;
		
		notifyDirectionMoved();
		notifySonarListeners();
		
		resetMode();
		System.out.println("Move complete");
	}

	/**
	 * Move to robot one tile to the back
	 */
	public synchronized void moveToRearTile() {
		if(isCollided()){
			resetMode();
			return;
		}
		
		refreshSonarValues();
		double tempSensorReading = getSonarValueAt(SonarType.EXPLORER_FRONT);
		
		// Fallback method same as front
		if(tempSensorReading == MAX_EXPLORER_FRONT) {
			moveBackTime();
			return;
		}
		
		setSpeed(-SPEED, 0.0);
		setMode(RobotMode.MOVING);
		
		while(tempSensorReading - getSonarValueAt(SonarType.EXPLORER_FRONT) - lastMoveErrorBackward > TILE_SIZE && tempSensorReading - getSonarValueAt(SonarType.EXPLORER_FRONT) > -0.2) {
			refreshSonarValues();
			if(isCollided()){
				resetMode();
				setSpeed(0.0, 0.0);
				return;
			}
			if(Math.abs(tempSensorReading - getSonarValueAt(SonarType.EXPLORER_FRONT) - lastMoveErrorBackward - TILE_SIZE) < MOVE_PRECISION)
				break;
		}
		
		setSpeed(0.0, 0.0);
		lastMoveErrorBackward += tempSensorReading - getSonarValueAt(SonarType.EXPLORER_FRONT) - TILE_SIZE;

		boolean b = getOrientation() == Orientation.WEST;
		for (int i = 0; i < moveListeners.size(); i++){
			if (b)
				latestMove = Orientation.EAST;
			else{
				latestMove = (Math.abs(getOrientation() + 180) % 360); 
			}
			moveListeners.get(i).directionMoved(latestMove);
		}

		notifySonarListeners();
		resetMode();
	}

	private static String formatSonarValues(double[] sonarValues) {
		String str = "";
		for (int i = 0; i < sonarValues.length; i++) {
			str = str + String.format("%6.2f", sonarValues[i]);
		}
		return str;

	}


	/**
	 * Adds a move listener to the robot. This listener will be notified of all movements of the robot
	 * @param ml
	 */
	public void addMoveListener(MoveListener ml) {
		if(!this.moveListeners.contains(ml)){
			this.moveListeners.add(ml);
			this.refreshSonarValues();
			ml.addSensorReading(
					getSonarValueAt(SonarType.EXPLORER_FRONT),
					getSonarValueAt(SonarType.FINE_FRONT_LEFT),
					getSonarValueAt(SonarType.FINE_FRONT_RIGHT),
					
					getSonarValueAt(SonarType.EXPLORER_RIGHT),
					getSonarValueAt(SonarType.FINE_RIGHT_BACK),
					getSonarValueAt(SonarType.FINE_RIGHT_FRONT),
					
					getSonarValueAt(SonarType.EXPLORER_LEFT),
					getSonarValueAt(SonarType.FINE_LEFT_BACK),
					getSonarValueAt(SonarType.FINE_LEFT_FRONT),
					
					getOrientation());
		}
	}

	/**
	 * Removes a move listener from the list of listeners
	 * @param ml
	 */
	public void removeMoveListener(MoveListener ml) {
		this.moveListeners.remove(ml);
	}

	/**
	 * Adds a listener to the list of actuator listeners
	 * @param al
	 */
	public void addActuatorListener(RobotStatsListener al) {
		if(!this.actuatorListeners.contains(al))
			this.actuatorListeners.add(al);
	}

	/**
	 * Removes a listener from the list of actuator listeners
	 * @param al
	 */
	public void removeActuatorListener(RobotStatsListener al) {
		this.actuatorListeners.remove(al);
	}

	/**
	 * Adds a listener to the list of sonar listeners
	 * @param sl
	 */
	public void addSonarListener(SonarListener sl) {
		if(!sonarListeners.contains(sl))
			this.sonarListeners.add(sl);
	}

	/**
	 * Removes a listener from the list of sonar listeners
	 * @param sl
	 */
	public void removeSonarListener(SonarListener sl) {
		this.sonarListeners.remove(sl);
	}
	
	/**
	 * @param cl
	 */
	public void addCollisionListener(CollisionListener cl){
			this.collisionListeners = cl;
	}
	
	/**
	 * @param cl
	 */
	public void removeCollisionListener(CollisionListener cl){
		this.collisionListeners = null;
	}
	
	/**
	 * Returns the opposite direction of the current one
	 * @param direction
	 * @return
	 */
	public static synchronized int reverseDirection(int direction){
		switch(direction){
		case Orientation.EAST:
			return Orientation.WEST;
		case Orientation.WEST:
			return Orientation.EAST;
		case Orientation.NORTH:
			return Orientation.SOUTH;
		case Orientation.SOUTH:
			return Orientation.NORTH;
		}
		return Orientation.CURRENT;
	}
	
	public void moveFrontTime(){

		for(int i = 0; i < 4; i++){
			refreshSonarValues();
			setSpeed(1, 0.0);
			try {Thread.sleep(100);} catch (InterruptedException e) {}
			setSpeed(0, 0.0);
		}
		
	}
	
	public void moveBackTime(){

		for(int i = 0; i < 4; i++){
			refreshSonarValues();
			setSpeed(1, 0.0);
			try {Thread.sleep(100);} catch (InterruptedException e) {}
			setSpeed(0, 0.0);
		}
		
	}
	
	public static void main(String[] a){
		Robot r = new Robot("192.168.0.3", 6665);
		r.runThreaded(-1,-1);
		r.setSpeed(0.0, 5);
		while(true){
			System.out.println(r.pos2D.getYaw());
		}
	}
	
	
}