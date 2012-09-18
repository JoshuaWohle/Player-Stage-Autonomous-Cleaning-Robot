package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * This class is the interface to create the Input frame for the client-server connection
 * @author Team Cyan
 *
 */
public class ServerAddressPanel extends JPanel {
	
    JTextField address = new JTextField();
    JTextField port = new JTextField();
    JCheckBox makeDefault = new JCheckBox("Make default?");
	
	public ServerAddressPanel(String addressDef,String portDef){
	
		super(new GridLayout(3, 1));
		
		JPanel northPrime = new JPanel(new BorderLayout());
		northPrime.add(new JLabel("Address: "),BorderLayout.WEST);
		northPrime.add(address,BorderLayout.CENTER);
		address.setText(addressDef);
		
		JPanel centerPrime = new JPanel(new BorderLayout());
		centerPrime.add(new JLabel("Port: "),BorderLayout.WEST);
		centerPrime.add(port,BorderLayout.CENTER);
		port.setText(portDef);
		
		JPanel southPrime = new JPanel(new BorderLayout());
		southPrime.add(makeDefault, BorderLayout.CENTER);

		this.add(northPrime);
		this.add(centerPrime);
		this.add(southPrime);
		
	}
	
	
	/**
	 * Returns the address input the user has specified 
	 * 
	 * @return address
	 */
	public String getAddress(){
		
		return address.getText();
	}
	
	/**
	 * Returns the port input the user has specified
	 * 
	 * @return
	 */
	public String getPort(){
		
		return port.getText();
		
	}
	
	public boolean getMakeDefault(){
		return makeDefault.isSelected();
	}
	
	/**
	 * Checks whether the inputs are valid
	 * @return
	 */
	public boolean checkInput(){
		
		if((address.getText().isEmpty()) || (port.getText().isEmpty())){
			return false;
		}else{
			return true;
		}
		
	}
	
}