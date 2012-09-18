package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class facilitates the reading of CSV Files
 * Many of these already exist, but I guess we cannot use them for our group project
 * @author Joshua Wöhle
 *
 */
public class CustomFileHandler {

	private String fileLocation = "";
	protected BufferedReader br = null;
	private FileInputStream fr = null;
	private DataInputStream in = null;
	private Boolean open = false;
	private Boolean overWrite = false;
	
	// Writing instruments
	FileWriter fstream = null;
	BufferedWriter out = null;

	/**
	 * @param fileLocation sets the file location in this object to the parameter
	 * @param write variable used to determine whether to write or read
	 * @param overWrite sets the variable overWrite in this object to the parameter
	 */
	public CustomFileHandler(String fileLocation, Boolean write, Boolean overWrite){
		this.overWrite = overWrite;
		this.fileLocation = fileLocation;
		try {
			if(write) {
				fstream = new FileWriter(this.fileLocation, !this.overWrite);
			
				out = new BufferedWriter(fstream);
				open = true;
			} else {
				this.fileLocation = fileLocation;
				this.fr = new FileInputStream(this.fileLocation);
				this.in = new DataInputStream(fr);
				this.br = new BufferedReader(new InputStreamReader(this.in));
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads a single line of text and returns it as a String
	 * @return
	 * @throws IOException
	 */
	public String readLine(){
		String temp;
		try {
			temp = br.readLine();
		
			if(temp != null)
				return temp;
			else
				return null;
			} catch (IOException e) {
				
				e.printStackTrace();;
				return null;
			}
	}
	
	/**
	 * Writes a line of text to the file
	 * @param line
	 * @throws IOException
	 */
	public void writeLine(String line) {
		try {
			if(!open) {
				fstream = new FileWriter(this.fileLocation, !this.overWrite);
				out = new BufferedWriter(fstream);
				open = true;
			}
			out.write(line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Closes the current output stream
	 * @throws IOException
	 */
	public void closeOutput() {
		try {
			out.close();
			open = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openOutput() {
		out = new BufferedWriter(fstream);
		open = true;
	}
	
}
