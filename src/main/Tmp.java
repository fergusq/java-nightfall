package main;

import java.awt.event.KeyEvent;
import java.lang.reflect.*;

public class Tmp {
	public static String cap(String s) {
		return s.substring(0,1).toUpperCase() + s.substring(1);
	}
	public static void main(String[] args) {
		Field[] fields = KeyEvent.class.getFields();
		for (Field f : fields) {
			if (f.getName().startsWith("VK_")) {
				String s = cap(f.getName().substring(3).toLowerCase());
				while (s.indexOf('_') != -1) {
					int i = s.indexOf('_');
					s = s.substring(0,i) + cap(s.substring(i+1));
				}
				System.out.println(s
						+ "\t\t(java.awt.event.KeyEvent." + f.getName() + "),");
			}
		}
	}
}
