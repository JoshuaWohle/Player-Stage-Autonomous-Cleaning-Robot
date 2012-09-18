package controller;

/**
 * The modes an object of {@link Controller} can be in
 * @author mayank
 *
 */
public enum ControllerMode {
	IDLE,
	COVERING,
	CLEANING,
	PATH_FINDING,
	PATH_TRAVERSING,
	EXPLORING,
	OBSTACLE_AVOIDANCE,
	INTERRUPTED
}
