package utilities;

import java.io.IOException;

import map.MapTile;

/**
 * @author Joshua Wöhle
 * Helper class to read the map file effectively
 */
public class MapFileHandler extends CustomFileHandler {

	/**
	 * @param fileLocation the location of the file being used
	 * @param write will write if this is set to true, else will read
	 * @throws IOException
	 */
	public MapFileHandler(String fileLocation, Boolean write) throws IOException {
		super(fileLocation, write, true);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Reads a line of text and returns it as a String array
	 * @return String array of all characters on a line
	 * @throws IOException
	 */
	public String[] readLineChars() throws IOException {
		String temp = br.readLine();
		
		if(temp == null)
			return null;
		
		String[] temp2 = new String[temp.length()];
		
		for(int i = 0; i < temp.length(); i++) {
			temp2[i] = temp.substring(i, i+1);
		}

		return temp2;
	}
	
	/**
	 * reads an array line by line then writes line by line to a text file
	 * @param map the Array of MapTiles being read
	 * @param columns number of columns in the map
	 * @param rows number of rows in the map
	 * @throws IOException
	 */
	public void writeMapFromArray(MapTile[][] map, int columns, int rows) throws IOException {
		for(int i = 0; i < rows; i++) {
			String temp = "";
			for(int j = 0; j < columns; j++) {
				temp = temp + Integer.toString(map[j][i].getType().ordinal());
			}
			this.writeLine(temp);
		}
		this.closeOutput();
	}

	/**
	 * @param map
	 */
	public void writeMapCoordinatesFromArray(MapTile[][] map) {
		// TODO Auto-generated method stub
		
	}

}
