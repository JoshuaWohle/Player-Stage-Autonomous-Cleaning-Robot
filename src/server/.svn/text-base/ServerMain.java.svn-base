package server;

import images.ImgSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


import algorithms.Coverage;
import algorithms.Exploring;
import algorithms.PathFinding;
import controller.Controller;

import map.Map;
import robot.Robot;
import utilities.CleaningSession;
import utilities.Log;
import utilities.Session;

public class ServerMain {
	
	public ServerMain(String address, int commanderPort, int mapServerPort, int statsServerPort, String playerStageAddress, int playerStagePort){
		Map map = new Map();
		Robot robot = new Robot(playerStageAddress, playerStagePort);
		robot.runThreaded(-1, -1);
		robot.addMoveListener(map);
		Controller controller = new Controller(map, robot);
		robot.addCollisionListener(controller);
		controller.setCoverageAlgorithm(new Coverage());
		controller.setPathFindingAlgorithm(new PathFinding());
		controller.setExploringAlgorithm(new Exploring());
		Commander commanderServer = new Commander(commanderPort, controller, map, robot);
		commanderServer.runServer();
		MapServer mapServer = new MapServer(mapServerPort, map);
		mapServer.runServer();
		map.addMapListener(mapServer);
		StatsServer statsServer = new StatsServer(statsServerPort);
		statsServer.runServer();
		map.addMapStatsListener(statsServer);
		robot.addActuatorListener(statsServer);
		commanderServer.addSessionStatsListener(statsServer);
		
	}
}