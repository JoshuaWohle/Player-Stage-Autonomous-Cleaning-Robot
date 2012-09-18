package ui;


import images.ImgSource;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;


/**
 * 
 * This class is used solely for Design purposes, 
 * used to over right the JButtons Graphics
 * @author Team Cyan
 *
 */
public class ImageButton extends JButton{
	
	
	/**
	 * Constructor used to create the JButton with appropriate images
	 * 
	 * @param actionText - Command text for the button
	 * @param imagePathName - the Default image used
	 * @param selectedImagePathName - the Selected state image used
	 */
	
	public ImageButton(String actionText,String imagePathName,String selectedImagePathName){
		
		ImageIcon img = new ImageIcon(ImgSource.imgPath + imagePathName);
        Image iconResize = img.getImage();
        Image newIcon = iconResize.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		
        ImageIcon imgSelected = new ImageIcon(ImgSource.imgPath + selectedImagePathName);
        Image iconResizeSelected = imgSelected.getImage();
        Image newIconSelected = iconResizeSelected.getScaledInstance(50,50, java.awt.Image.SCALE_SMOOTH);
        
		this.setIcon(new ImageIcon (newIcon));
		this.setOpaque(false);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setActionCommand(actionText);
		this.setSelectedIcon(new ImageIcon(newIconSelected));
		
		
	}

}
