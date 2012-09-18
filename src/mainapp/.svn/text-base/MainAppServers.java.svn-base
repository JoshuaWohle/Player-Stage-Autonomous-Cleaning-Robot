package mainapp;
import server.Commander;
import server.MapServer;
import server.ServerMain;
import server.StatsServer;


public class MainAppServers {
	public static final int PLAYER_STAGE_PORT = 6665;
	public static final String PLAYER_STAGE_ADDRESS = "localhost";
	
	public static void main(String[] args){
		String host = "localhost";
		if(args.length > 0)
			host = args[0];

		System.out.println("Connecting to : " + host);
		new ServerMain("localhost", Commander.PORT, MapServer.PORT, StatsServer.PORT, host, PLAYER_STAGE_PORT);
		
	}
}
