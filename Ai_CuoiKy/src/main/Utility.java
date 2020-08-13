package main;

import java.util.Random;

public class Utility {
	public static Random rand = new Random();

	public static int randomInt(int min, int max) {
		double d = min + rand.nextDouble() * (max - min);
		return (int) d;
	}

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = rand.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}
}
