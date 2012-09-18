package mainapp;

import consumerUI.ConsumerUI;


public class MainAppConsumerUI {
	/**
	 * The main method to execute the UI
	 * Default values are passed to initiate the server-client connection 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String host = "localhost";
		if(args.length > 0)
			host = args[0];
		
		final ConsumerUI consumerUI = new ConsumerUI(host,"9002","9001");
		consumerUI.setVisible(true);
	}
}
