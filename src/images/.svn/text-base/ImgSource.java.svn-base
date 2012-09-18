package images;

import java.io.File;

public class ImgSource {

	public static final String imgPath = "src/images";
	public static String theme = "mario";
	public static String themePath = imgPath + "/themes/" + theme;

	private static String getPath() {
		return (new File("images")).getAbsolutePath();
	}

	public static void refreshThemePath() {
		themePath = imgPath + "/themes/" + theme;
	}
}

