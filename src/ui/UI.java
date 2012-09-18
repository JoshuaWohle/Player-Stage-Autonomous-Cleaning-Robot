package ui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JLabel;
import utilities.Clock;
import utilities.TickListener;


/**
 * This class is the Super class for the UI
 * It shares the common traits found in all the UIs such as Time,Date & connections
 * @author Team Cyan
 *
 */
public class UI extends JFrame implements TickListener{

	
	private Clock clock;
	protected JLabel time,date;
	private static DateFormat timeFormat = new SimpleDateFormat("H:mm:ss");
	private static DateFormat dateFormat = new SimpleDateFormat("dd:MMM:yyyy");
	protected Font font;
	
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private Socket clientSocket;
	
	private Calendar now;
	protected JFrame thisFrame;
	
	public UI(){
		super();
		thisFrame = this;
		now = Calendar.getInstance();
		
		font = new Font("sansserif",Font.BOLD,15);
		time = new JLabel();
		time.setFont(font);
		time.setForeground(Color.CYAN);	
		date = new JLabel();
		date.setFont(font);
        date.setForeground(Color.CYAN);
        Clock.getClock().addTickListener(this);
		
	}

	
	public void tick(Calendar t){ 
		now = t;
		time.setText("  Time: " + timeFormat.format(t.getTime()));
		date.setText("  Date: " + dateFormat.format(t.getTime()));
	}
	
	public Calendar getDateTime(){
		return now;
	}
	
	
	private void connectClient(String address,int port){
		
		try {
			clientSocket = new Socket(address, port);
			System.out.println("Connected to: " + clientSocket.getPort());
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			
			e.printStackTrace();
		}
		
		
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);}
		try {
			in = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);}
		
	}

	
	
}