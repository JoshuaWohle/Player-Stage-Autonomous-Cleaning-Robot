package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * This Class is used solely for Design implementation
 * It creates a Gradient like pattern for the Borders
 * @author Team Cyan
 *
 */
public class GradientPanel extends JPanel{

	
	private Color lightColor;
    private Color darkColor;	
	
    /**
     * 
     * Used to take in the width and height border size and colour it accordingly
     * 
     * @param width - Takes the Width for the Border
     * @param height - Takes the Height for the Border
     * @param light - Specifies the Lightest Colour
     * @param dark - Specifies the Darkest Colour
     */
    public GradientPanel(int width, int height, Color light,Color dark){
    	
    	this.lightColor = light;
    	this.darkColor = dark;
    	this.setPreferredSize(new Dimension(width,height));
    	
    }
    
    /**
     * This method is overwritten and used to create a custom like Graphic
     */
    protected void paintComponent(Graphics g){
    	
    	super.paintComponents(g);
    	Graphics2D g2D = (Graphics2D)g;
        GradientPaint gradPaint = new GradientPaint(0, 0, this.lightColor, 0, getHeight(),this.darkColor);
        g2D.setPaint(gradPaint);
        g2D.fillRect(0, 0, getWidth(), getHeight());
    	
    	
    }
    
    
	
}
