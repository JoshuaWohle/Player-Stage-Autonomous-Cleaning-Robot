package utilities;

import java.util.HashMap;

/**
 * @author Team Cyan
 * used for encoding and decoding commands
 */
public class ActionCommandHelper {
	
	private static String separator = "#";
	
	/**
	 * @param options sets key for this action command
	 * @return returns encoded command
	 */
	public static String encodeActionCommand(HashMap<String, String> options) {
		String encodedCommand = "";
		for (String key : options.keySet()) {
			encodedCommand = encodedCommand + key + separator + options.get(key) + separator;
		}
		return encodedCommand;
	}
	
	/**
	 * @param options an array of keys to be set as commands
	 * @return the encoded command
	 */
	public static String encodeActionCommand(String[] options) {
		String encodedCommand = "";
		for(int i = 0; i < options.length; i++) {
			encodedCommand = encodedCommand + i + separator + options[i] + separator;
		}
		return encodedCommand;
	}
	
	/**
	 * @param actionCommand the command to be decoded
	 * @return the Hashmap of the decoded command
	 */
	public static HashMap<String, String> decodeActionCommand(String actionCommand) {
		
		HashMap<String, String> decodedCommand = new HashMap<String, String>();
		String[] tempArray = actionCommand.split(separator);
		for(int i = 0; i < tempArray.length-1; i = i+2) {
			decodedCommand.put(tempArray[i], tempArray[i+1]);
		}
		
		return decodedCommand;
	}
	
}
