package visualmap;


import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import map.MapTile;

/**
 * @author Team Cyan
 *
 */
public class ScreenShot extends Graphics2D {
	private String imgPath = "";
	
	MapTile[][] map = null;
	BufferedImage img = null;
	Graphics2D g2d = this;
	int sqSize = 10;
	
	int horiz = 0;
	int vert = 0;
	 
	int width = 0;
	int height = 0;
	
	/**
	 * @param map Array of MapTiles used to draw Screenshot
	 */
	public ScreenShot(MapTile[][] map, String path){
		
		this.width = map.length;
		this.height = map[0].length;
		this.map = map;
		
		horiz = height * sqSize;
		vert = width * sqSize;
		img = new BufferedImage(vert,horiz,BufferedImage.TYPE_INT_ARGB);
		g2d = this;
		
		g2d = img.createGraphics();
		imgPath = path;
		drawScreenshotMap();
		
	}
	
	/**
	 * Iterates through array of Maptiles and draws graphics to a buffered image
	 * depends on tile type
	 */
	public void drawScreenshotMap(){
		/**
		 * x and y used to state location to position square on image
		 */
		int x = 0;
		int y = 0;
			 	  
		for(int i = 0; i < height; i++){

				for(int j = 0; j < width; j++){
					//loops through all Map tiles and draws them into the buffered image
					MapTile aTile = map[j][i];
					drawSquare(x,y,aTile);
					
					//x and y adjusted to change co-ordinate of the square
					x=x+10;
			  }
			
			  x = 0 ;
			  y=y+10;
			  
			  
		}
		  
		/**
		 * Date and time used to give file unique name
		 * gives accuracy to minute
		 */
		  Calendar cal = Calendar.getInstance();
		  String tempName = "/" + cal.get(Calendar.YEAR)+ "-" + cal.get(Calendar.MONTH) 
				  + "-" + cal.get(Calendar.DAY_OF_WEEK) + "-" +
				  cal.get(Calendar.HOUR_OF_DAY) + "-" + cal.get(Calendar.MINUTE) + ".png";

		  String filePath = this.imgPath + tempName;
		
		  /**
		   * writes buffered image to file
		   * 
		   */
		  
			File outputfile = new File(filePath);
		    try {
				ImageIO.write(img, "png", outputfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * @param x location of x co-ordinate on the image to draw a new square
	 * @param y location of x co-ordinate on the image to draw a new square
	 * @param bTile tile being read to determine what to draw
	 */
	public void drawSquare(int x,int y, MapTile bTile){
		/**
		 * finds ordinal of the type of the tile passed
		 * sets colour of square accordingly
		 */
		int type = bTile.getType().ordinal();
		
		if(type == 0){
			/**
			 * if no timestamp is found the colour is set to light Gray
			 * otherwise the colour is set to a shade of red depending on when it was last cleaned
			 */
			if(bTile.whenCleaned() == null)
				g2d.setPaint(Color.LIGHT_GRAY);
			else
				g2d.setPaint(tileCleaned(bTile));
		}
		else if(type == 1)
			g2d.setPaint(Color.BLUE);
		else if(type == 2)
			g2d.setPaint(Color.YELLOW);
		else if(type == 3)
			g2d.setPaint(Color.BLACK);
		
		/**
		 * fills a rectangle and positions it at the given location
		 */
		g2d.fillRect(x, y, 10, 10);
	}
	
	/**
	 * @param tile used to find when tile was last cleaned
	 * @return a colour that is dependent on how long ago the tile was cleaned which will be set 
	 * to a square that will be drawn
	 */
	public Color tileCleaned(MapTile tile){
		
		Date temp = tile.whenCleaned();
		Calendar lastCleaned = Calendar.getInstance();
		lastCleaned.setTime(temp);
		
		Calendar cal = Calendar.getInstance();
		
		/**
		 * sets the time the tile was last cleaned to a Calendar variable
		 * sets current time back one hour and uses this to compare
		 * increments time to compare by 15 minutes and sets colour respectively
		 */
		
		
		
		cal.add(Calendar.MINUTE, -60);
		
		if(lastCleaned.before(cal)){
			return new Color(215,0,0);
				}
		
		cal.add(Calendar.MINUTE, 15);
		if(lastCleaned.before(cal)){
			return new Color(215,40,40);
		}
		
		cal.add(Calendar.MINUTE, 15);
		if(lastCleaned.before(cal)){
			return new Color(215,80,80);
		}
		
		cal.add(Calendar.MINUTE, 15);
		if(lastCleaned.before(cal)){
			return new Color(215,120,120);
		}
		
		return new Color(255,255,255);
		
		
		
	}
	

	
	public void addRenderingHints(java.util.Map<?, ?> hints) {
		// TODO Auto-generated method stub
		
	}

	
	public void clip(Shape s) {
		// TODO Auto-generated method stub
		
	}

	
	public void draw(Shape s) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	
	public boolean drawImage(Image img, AffineTransform xform,
			ImageObserver obs) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void drawImage(BufferedImage img, BufferedImageOp op, int x,
			int y) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawRenderableImage(RenderableImage img,
			AffineTransform xform) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawString(String str, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawString(String str, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawString(AttributedCharacterIterator iterator, int x,
			int y) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawString(AttributedCharacterIterator iterator, float x,
			float y) {
		// TODO Auto-generated method stub
		
	}

	
	public void fill(Shape s) {
		// TODO Auto-generated method stub
		
	}

	
	public Color getBackground() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Composite getComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public GraphicsConfiguration getDeviceConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public FontRenderContext getFontRenderContext() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Paint getPaint() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object getRenderingHint(Key hintKey) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public RenderingHints getRenderingHints() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Stroke getStroke() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public AffineTransform getTransform() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void rotate(double theta) {
		// TODO Auto-generated method stub
		
	}

	
	public void rotate(double theta, double x, double y) {
		// TODO Auto-generated method stub
		
	}

	
	public void scale(double sx, double sy) {
		// TODO Auto-generated method stub
		
	}

	
	public void setBackground(Color color) {
		// TODO Auto-generated method stub
		
	}

	
	public void setComposite(Composite comp) {
		// TODO Auto-generated method stub
		
	}

	
	public void setPaint(Paint paint) {
		// TODO Auto-generated method stub
		
	}

	
	public void setRenderingHint(Key hintKey, Object hintValue) {
		// TODO Auto-generated method stub
		
	}

	
	public void setRenderingHints(java.util.Map<?, ?> hints) {
		// TODO Auto-generated method stub
		
	}

	
	public void setStroke(Stroke s) {
		// TODO Auto-generated method stub
		
	}

	
	public void setTransform(AffineTransform Tx) {
		// TODO Auto-generated method stub
		
	}

	
	public void shear(double shx, double shy) {
		// TODO Auto-generated method stub
		
	}

	
	public void transform(AffineTransform Tx) {
		// TODO Auto-generated method stub
		
	}

	
	public void translate(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	
	public void translate(double tx, double ty) {
		// TODO Auto-generated method stub
		
	}

	
	public void clearRect(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	
	public void clipRect(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		// TODO Auto-generated method stub
		
	}

	
	public Graphics create() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	
	public void drawArc(int x, int y, int width, int height,
			int startAngle, int arcAngle) {
		// TODO Auto-generated method stub
		
	}

	
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean drawImage(Image img, int x, int y, Color bgcolor,
			ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean drawImage(Image img, int x, int y, int width,
			int height, ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean drawImage(Image img, int x, int y, int width,
			int height, Color bgcolor, ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, Color bgcolor,
			ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void drawLine(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawOval(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		// TODO Auto-generated method stub
		
	}

	
	public void fillArc(int x, int y, int width, int height,
			int startAngle, int arcAngle) {
		// TODO Auto-generated method stub
		
	}

	
	public void fillOval(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		// TODO Auto-generated method stub
		
	}

	
	public void fillRect(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	
	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		// TODO Auto-generated method stub
		
	}

	
	public Shape getClip() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Rectangle getClipBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Font getFont() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public FontMetrics getFontMetrics(Font f) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void setClip(Shape clip) {
		// TODO Auto-generated method stub
		
	}

	
	public void setClip(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	
	public void setColor(Color c) {
		// TODO Auto-generated method stub
		
	}

	
	public void setFont(Font font) {
		// TODO Auto-generated method stub
		
	}

	
	public void setPaintMode() {
		// TODO Auto-generated method stub
		
	}

	
	public void setXORMode(Color c1) {
		// TODO Auto-generated method stub
		
	}
}
