package ui;


import images.ImgSource;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;



/**
 * This class is used to create the Design for the JPopupMenu
 * @author Team Cyan
 *
 */
public class JPopupMenuExtended extends JPopupMenu {
	public static final Image img = (new ImageIcon(ImgSource.imgPath + "/settings-menu-bkg.png")).getImage();
	public JPopupMenuExtended(){
		super();
	}
	public JPopupMenuExtended(String name){
		super(name);
	}
	
	
	public void paintComponent(Graphics graphics){
		super.paintComponent(graphics);
		((Graphics2D)graphics).drawImage(img, 0,0, null);
	  
	}

}
