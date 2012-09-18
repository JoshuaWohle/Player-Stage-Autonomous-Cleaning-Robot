package map;

public interface MapStatsListener {
	
	public void tileCleaned(int totalCleaned, int totalEmpty);
	
	public void newMap();

}