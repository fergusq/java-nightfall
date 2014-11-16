package game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;

import json.JSNode;

public class ResourceLoader {
	static Image LoadImage(String name) {
		try {
			if (getDirectory() != null) try {
				return ImageIO.read(new FileInputStream(new File(getDirectoryPath() + File.separator + "AddOns" + File.separator + name)));
			} catch (FileNotFoundException | IllegalArgumentException e) {}
			return ImageIO.read(ResourceLoader.class.getResource("/res/" + name));
		} catch (Exception e) {
			System.err.println("Failed load image `" + name + "` because: " + e.getMessage());
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}
	}
	static InputStream LoadData(String name) {
		try {
			InputStream s = ResourceLoader.class.getResourceAsStream("/res/" + name);
			if (s == null) {
				s = new FileInputStream(new File(getDirectoryPath() + File.separator + "AddOns" + File.separator + name));
				if (s == null) {
					throw new Exception("Resource not found");
				}
			}
			return s;
		} catch (Exception e) {
			System.err.println("Failed to load data `" + name + "` because: " + e.getMessage());
			return null;
		}
	}
	static JSNode LoadJSON(String name) throws Exception {
		return JSNode.parse(LoadData(name));
	}

	static File[] getAddons() {
		String path = getDirectoryPath() + File.separator + "AddOns";
		return new File(path).listFiles();
	}

	static JSNode[] LoadAddonJSONFiles(String name) throws Exception {
		if (getDirectory() == null) return new JSNode[0];
		String path = getDirectoryPath() + File.separator + "AddOns";
		java.util.ArrayList<JSNode> addonFiles = new java.util.ArrayList<JSNode>();
		File[] files = getAddons();
		for (File f : files) {
			if (f.isDirectory()) {
				String addonFile = f.getPath() + File.separator + name;
				System.err.print("Loading '" + addonFile + "'...");
				if (new File(addonFile).exists()) {
					System.err.println("[FOUND]");
					addonFiles.add(JSNode.parse(new FileInputStream(new File(addonFile))));
				}
				else System.err.println("[MISSING]");
			}
		}
		return (JSNode[]) addonFiles.toArray(new JSNode[0]);
	}

	static JSNode LoadSave(String name) throws Exception {
		String path = getDirectoryPath();

		JSNode userdata;
		try { userdata =
			JSNode.parse(new FileInputStream(new File(path + File.separator + name)));
		} catch (FileNotFoundException | java.security.AccessControlException | NullPointerException ex) { userdata = null; }
		if (userdata == null)
			userdata = JSNode.parse(ResourceLoader.LoadData(name));
		return userdata;
	}

	static void WriteSave(String data, String name) {
		String path = getDirectoryPath();
		try {
			java.io.PrintWriter out = new java.io.PrintWriter(path + File.separator + name);
			out.print(data);
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Directories

	public static File getDirectory() {
		return dataDir;
	}

	public static String getDirectoryPath() {
		return dataDirPath;
	}

	private static String dataDirPath;
	static { try { dataDirPath = System.getProperty("user.home") + File.separator + ".nightfall"; }
		catch (java.security.AccessControlException ex) { ex.printStackTrace(); dataDirPath = ".nightfall"; } }
	private static File dataDir;
	static { try { dataDir = new File(dataDirPath); dataDir.mkdirs(); }
		catch (Exception ex) {
			dataDir = null;
			ex.printStackTrace();
		}
	}
}
