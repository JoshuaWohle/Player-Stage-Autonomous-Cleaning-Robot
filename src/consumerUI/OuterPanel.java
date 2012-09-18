package consumerUI;


import images.ImgSource;


import java.awt.Graphics;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


/**
 * 
 * This Class is used to override the paint component to the OuterPanel
 * @author Team Cyan
 *
 */
public class OuterPanel extends JPanel {


	public void paintComponent(Graphics g) {
		Image image = (new ImageIcon(ImgSource.imgPath + "/panelclr.png")).getImage();		
		g.drawImage(image, 1,1 , null);
	}

}
