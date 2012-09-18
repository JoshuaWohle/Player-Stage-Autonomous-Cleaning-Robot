package visualmap;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import map.Coordinate;
import map.MapTile;
import map.MapTileType;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import consumerUI.ConsumerUI;

import images.ImgSource;


public class VisualMap3D extends VisualMap {

	private final int MAP_SIZE_MAX = 200;
	private final int MAP_SIZE_DEFAULT = 10;
	private final float TILE_SIZE = 0.1f;
	private final float WALL_HEIGHT = 0.1f;
	private final float FLOOR_HEIGHT = 0.01f;
	private String WALL_IMAGE = "/wallTile.png";
	private String FLOOR_IMAGE = "/emptyTile.png";
	private String MOVING_OBST_IMAGE = "/movingTile.png";
	private String NOT_SET_IMAGE = "/notSetTile_black.jpg";
	private String ROBOT_IMAGE_FRONT = "/FRONT.png";
	private String ROBOT_IMAGE_RIGHT = "/RIGHT.png";
	private String ROBOT_IMAGE_LEFT = "/LEFT.png";
	private String ROBOT_IMAGE_BACK = "/BACK.png";
	private String ROBOT_IMAGE_TOP = "/TOP.png";
	

	
	private Texture WALL_TEXTURE = null;
	private Texture FLOOR_TEXTURE = null;
	private Texture ROBOT_TEXTURE = null;
	private Texture MOVING_TEXTURE = null;
	private Texture NOT_SET_TEXTURE = null;
	
	TextureAttributes TATTRIBUTES = null;
	
	BorderLayout container = new BorderLayout();
	GraphicsConfiguration gConfig = SimpleUniverse.getPreferredConfiguration();
	Canvas3D canvas = new Canvas3D(gConfig);
	MouseRotate mouseRotate = null;
	MouseWheelZoom mouseZoom = new MouseWheelZoom();
	
	SimpleUniverse universe = new SimpleUniverse(canvas); // Creates the universe
	BranchGroup rootGroup = new BranchGroup();
	BranchGroup boxGroup = new BranchGroup();
	TransformGroup boxTransformGroup = new TransformGroup();

	Box[][] boxMap = new Box[MAP_SIZE_MAX][MAP_SIZE_MAX];
	TransformGroup boxTransformGroups[][] = new TransformGroup[MAP_SIZE_MAX][MAP_SIZE_MAX];
	Transform3D boxTransform3D[][] = new Transform3D[MAP_SIZE_MAX][MAP_SIZE_MAX];
	
	// Robot related
	Coordinate lastRobotPosition = null;
	Box robot = null;
	TransformGroup robotTransformGroup = new TransformGroup();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VisualMap3D(){
		super();
		this.setLayout(container);
		this.add("Center", canvas);
		
		
		
		rootGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		rootGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);
		
		boxTransformGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		boxTransformGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);
		boxTransformGroup.setCapability(BranchGroup.ALLOW_DETACH);
		boxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		boxTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		boxGroup.setCapability(BranchGroup.ALLOW_DETACH);
		boxTransformGroup.addChild(boxGroup);
		
		Color3f lightColor = new Color3f(.1f, 1.4f, .1f); // Green light
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		
		AmbientLight light = new AmbientLight(lightColor);
		light.setInfluencingBounds(bounds);
		rootGroup.addChild(light);

		rootGroup.addChild(boxTransformGroup);
		
		loadTextures();
		
		this.drawMap();
		
		MouseTranslate navigation = new MouseTranslate(canvas, boxTransformGroup);
		MouseRotate rotation = new MouseRotate(canvas, boxTransformGroup);
		MouseWheelZoom zoom = new MouseWheelZoom(canvas, boxTransformGroup);
		
		navigation.setSchedulingBounds(new BoundingSphere(new Point3d(), 200));
		rotation.setSchedulingBounds(new BoundingSphere(new Point3d(), 200));
		zoom.setSchedulingBounds(new BoundingSphere(new Point3d(), 100));
		
		boxTransformGroup.addChild(navigation);
		boxTransformGroup.addChild(rotation);
		boxTransformGroup.addChild(zoom);

		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(rootGroup);
	}
	
	public MapTile[][] createMap() {
		
		MapTile[][] tempMap = new MapTile[15][15];
		for(int i = 0; i < tempMap.length; i++) {
			for(int j = 0; j < tempMap[0].length; j++) {
				tempMap[i][j] = new MapTile(MapTileType.EMPTY, i, j);
			}
		}
		return tempMap;
	}
	
	private void drawMap() {
		createScene();
	}
	
	private void createScene() {
		
		int width = map.length;
		int height = map[0].length;
		
		boxTransformGroup.removeChild(boxGroup);
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(boxMap[i][j] != null)
					updateBoxFromTile(boxMap[i][j], map[i][j]);
				else {
					createBoxFromTile(map[i][j], false);
				}
			}
		}
		
		// Redraw the viewpoint of the user to center the map
		
		ViewingPlatform view = universe.getViewingPlatform();
		TransformGroup tg = view.getViewPlatformTransform();
		Transform3D trans = new Transform3D();
		float x = (float)width / (float)2 * TILE_SIZE;
		float y = (float)height / (float)2 * TILE_SIZE;
		trans.setTranslation(new Vector3f(x, y, 10.0f));
		tg.setTransform(trans);
		
		boxTransformGroup.addChild(boxGroup);
	}
	
	private void createBoxFromTile(MapTile tile, boolean detach) {
		
		int i = tile.getCoordinateX();
		int j = tile.getCoordinateY();
		
		float heightOfBox = WALL_HEIGHT;
		
		Appearance app = new Appearance();
		app.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		app.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		Texture texture = NOT_SET_TEXTURE;
		
		// Initiate the position at which the box will be drawn
		Vector3f vector = new Vector3f(((float)i)*TILE_SIZE*2, ((float)j)*TILE_SIZE*2, 0.0f);
		
		switch(tile.getType()) {
		case EMPTY:
			texture = FLOOR_TEXTURE;
			heightOfBox = FLOOR_HEIGHT;
			// This is a floor tile, so we have to draw it as such, with a lower Z dimension :)
			vector = new Vector3f(((float)i)*TILE_SIZE*2, ((float)j)*TILE_SIZE*2, -TILE_SIZE+FLOOR_HEIGHT);
			break;
		case STATIONARY:
			texture = WALL_TEXTURE;
			break;
		case MOVING:
			texture = MOVING_TEXTURE;
			break;
		case NOT_SET:
			return;
		default:
			return;
		}
		
		app.setTexture(texture);
		app.setTextureAttributes(TATTRIBUTES);
		boxMap[i][j] = new Box(TILE_SIZE, TILE_SIZE, heightOfBox, Box.GENERATE_TEXTURE_COORDS, app);
		
		boxMap[i][j].setCapability(Box.ENABLE_APPEARANCE_MODIFY);
		boxMap[i][j].setUserData(map[i][j]);
		
		boxTransformGroups[i][j] = new TransformGroup();
		boxTransformGroups[i][j].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		boxTransform3D[i][j] = new Transform3D();
		boxTransform3D[i][j].setTranslation(vector);
		boxTransformGroups[i][j].setTransform(boxTransform3D[i][j]);
		boxTransformGroups[i][j].addChild(boxMap[i][j]);
		
		// Add new node to the current scene
		BranchGroup bg = new BranchGroup();
		bg.addChild(boxTransformGroups[i][j]);
		boxTransformGroup.addChild(bg);
	}
		
	private void updateBoxFromTile(Box box, MapTile tile) {
		
		if(box == null) {
			createBoxFromTile(tile, true);
			return;
		}
		
		MapTileType previousType = ((MapTile)box.getUserData()).getType();
		
		if(previousType == tile.getType())
			return;
		
		float boxHeight = 1;
		
		int x = tile.getCoordinateX();
		int y = tile.getCoordinateY();
		
		Appearance app = box.getAppearance();
		Texture texture = NOT_SET_TEXTURE;
		
		switch(tile.getType()) {
			case EMPTY:
				texture = FLOOR_TEXTURE;
				boxHeight = 0.1f;
				boxTransform3D[x][y].setScale(new Vector3d(1, 1, boxHeight));
				boxTransformGroups[x][y].setTransform(boxTransform3D[x][y]);
				break;
			case STATIONARY:
				texture = WALL_TEXTURE;
				break;
			case MOVING:
				texture = MOVING_TEXTURE;
				break;
			case NOT_SET:
				texture = NOT_SET_TEXTURE;
				break;
			default:
				texture = NOT_SET_TEXTURE;
				break;
			}
		
		if(previousType == MapTileType.EMPTY) {
			boxHeight = 10.0f;
			boxTransform3D[x][y].setScale(new Vector3d(1, 1, boxHeight));
			boxTransformGroups[x][y].setTransform(boxTransform3D[x][y]);
		}
		
		app.setTexture(texture);
		box.setUserData(tile);
	}
	
	
	public void updateMap(MapTile[][] newMap) {
		super.updateMap(newMap);
		this.drawMap();
	}
	
	
	public void updateTile(MapTile tile) {
		int x = tile.getCoordinateX();
		int y = tile.getCoordinateY();
		map[x][y] = tile;
		updateBoxFromTile(boxMap[x][y], tile);
	}
	
	
	public void updateRobotPosition(Coordinate coord) {
		lastRobotPosition = coord;
		if(robot == null) 
			createRobot(coord);
		else
			moveRobot(coord);
	}
	
	public Coordinate getRobotPosition() {
		return this.lastRobotPosition;
	}
	
	private void createRobot(Coordinate coord) {
		int x = coord.getX();
		int y = coord.getY();

		
		TransparencyAttributes t_attr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.0f);
		
		Appearance frontApp = new Appearance();
		frontApp.setTextureAttributes(TATTRIBUTES);
		frontApp.setTransparencyAttributes(t_attr);
		Appearance rightApp = new Appearance();
		rightApp.setTextureAttributes(TATTRIBUTES);
		rightApp.setTransparencyAttributes(t_attr);
		Appearance leftApp = new Appearance();
		leftApp.setTextureAttributes(TATTRIBUTES);
		leftApp.setTransparencyAttributes(t_attr);
		Appearance backApp = new Appearance();
		backApp.setTextureAttributes(TATTRIBUTES);
		backApp.setTransparencyAttributes(t_attr);
		Appearance topApp = new Appearance();
		topApp.setTextureAttributes(TATTRIBUTES);
		topApp.setTransparencyAttributes(t_attr);
		
		TextureLoader tLoader = null;
		
		Appearance app = new Appearance();
		app.setTransparencyAttributes(t_attr);
		robot = new Box(TILE_SIZE, TILE_SIZE, WALL_HEIGHT, Box.GENERATE_TEXTURE_COORDS, app);
		
		frontApp.setTexture(new TextureLoader(ImgSource.themePath + ROBOT_IMAGE_FRONT, "RGBA", this).getTexture());
		rightApp.setTexture(new TextureLoader(ImgSource.themePath + ROBOT_IMAGE_RIGHT, "RGBA", this).getTexture());
		leftApp.setTexture(new TextureLoader(ImgSource.themePath + ROBOT_IMAGE_LEFT, "RGBA", this).getTexture());
		backApp.setTexture(new TextureLoader(ImgSource.themePath + ROBOT_IMAGE_BACK, "RGBA", this).getTexture());
		topApp.setTexture(new TextureLoader(ImgSource.themePath + ROBOT_IMAGE_TOP, "RGBA", this).getTexture());
		
		// Set appearances to show :)
		robot.setAppearance(Box.FRONT, topApp);
		robot.setAppearance(Box.RIGHT, backApp);
		robot.setAppearance(Box.LEFT, frontApp);
		robot.setAppearance(Box.TOP, rightApp);
		robot.setAppearance(Box.BOTTOM, leftApp);

		/*TransparencyAttributes t_attr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.0f);
		Appearance app = new Appearance();
//		app.setTransparencyAttributes(t_attr);
		Texture texture = new TextureLoader(ImgSource.imgPath + ROBOT_IMAGE, "RGBA", this).getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);
	    texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		app.setTexture(texture);
		
		robot = new Sphere(TILE_SIZE, Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS , app);*/
		
		
		Transform3D transform = new Transform3D();
		Vector3f vector = new Vector3f(((float)x)*TILE_SIZE*2, ((float)y)*TILE_SIZE*2, 0.0f);
		transform.setTranslation(vector);
		
		robotTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		robotTransformGroup.setTransform(transform);
		robotTransformGroup.addChild(robot);
		BranchGroup bg = new BranchGroup();
		bg.addChild(robotTransformGroup);
		boxTransformGroup.addChild(bg);
	}
	
	/**
	 * Moves the robot to a certain position
	 * @param coord
	 */
	private void moveRobot(Coordinate coord) {
		int x = coord.getX();
		int y = coord.getY();

		Transform3D transform = new Transform3D();
		Vector3f vector = new Vector3f(((float)x)*TILE_SIZE*2, ((float)y)*TILE_SIZE*2, 0.0f);
		transform.setTranslation(vector);
		robotTransformGroup.setTransform(transform);
	}
	
	private void loadTextures() {
		TextureLoader tLoader = new TextureLoader(ImgSource.themePath + FLOOR_IMAGE, "RGB", this);
		FLOOR_TEXTURE = tLoader.getTexture();
		tLoader = new TextureLoader(ImgSource.themePath + WALL_IMAGE, "RGB", this);
		WALL_TEXTURE = tLoader.getTexture();
		tLoader = new TextureLoader(ImgSource.themePath + MOVING_OBST_IMAGE, "RGB", this);
		MOVING_TEXTURE = tLoader.getTexture();
		tLoader = new TextureLoader(ImgSource.themePath + NOT_SET_IMAGE, "RGB", this);
		NOT_SET_TEXTURE = tLoader.getTexture();
		
		TATTRIBUTES = new TextureAttributes();
		TATTRIBUTES.setTextureMode(TextureAttributes.MODULATE);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		VisualMap3D vmap = new VisualMap3D();
		frame.setContentPane(vmap);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setVisible(true);
		vmap.updateTile(new MapTile(MapTileType.EMPTY, 2, 0));
		vmap.updateTile(new MapTile(MapTileType.STATIONARY, 4, 0));
		vmap.updateRobotPosition(new Coordinate(3, 0));
		
	}

	public void setClickable(boolean b) {
		// TODO Auto-generated method stub hello
		
	}
 
	public void resetSelection() {//
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Coordinate> getSelectedTiles() {
		// TODO Auto-generated method stub
		return null;
	}

	public void clearSelection() {
		// TODO Auto-generated method stub
		
	}
	
}