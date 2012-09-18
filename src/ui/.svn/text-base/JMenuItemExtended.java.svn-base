package ui;


import images.ImgSource;


import java.awt.Graphics;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;



/**
 * This class is used solely for design purposes, it is used to over right
 * the graphics for the JMenuItems for the POP Up Menu
 * @author Team Cyan
 *
 */
public class JMenuItemExtended extends JMenuItem {
	
	private Image img;
	
	/**
	 * 
	 * @param name
	 */
	
	public JMenuItemExtended(String name){
		super();
		ImageIcon icon = new ImageIcon(ImgSource.imgPath + "/settings-menu-bkg.png");
		img = icon.getImage();
		this.setText(name);
		this.setBorderPainted(true);
	}

	/**
	 * Used to override the graphics component
	 */
	
	public void paint(Graphics graphics){	
		graphics.drawImage(img, 0, 0, this);
		setOpaque(false);
		
		super.paint(graphics);
		setOpaque(true);
	  
	}

}
