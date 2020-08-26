package mineclone.common.util;

import mineclone.common.math.LinMath;

public final class ColorUtil {

	public static final int WHITE = 0xFFFFFFFF;
	public static final int BLACK = 0xFF000000;
	
	private ColorUtil() {
	}
	
	public static int unpackA(int argb) {
		return argb >>> 24;
	}

	public static int unpackR(int argb) {
		return (argb >>> 16) & 0xFF;
	}

	public static int unpackG(int argb) {
		return (argb >>> 8) & 0xFF;
	}

	public static int unpackB(int argb) {
		return argb & 0xFF;
	}

	public static int pack(int r, int g, int b) {
		return 0xFF000000 | (r << 16) | (g << 8) | b;
	}

	public static int pack(int a, int r, int g, int b) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static int pack(float r, float g, float b) {
		return pack(deNormalize(r), deNormalize(g), deNormalize(b));
	}
	
	public static int pack(float a, float r, float g, float b) {
		return pack(deNormalize(a), deNormalize(r), deNormalize(g), deNormalize(b));
	}

	public static void checkA(int a) {
		if ((a & (~0xFF)) != 0)
			throw new IllegalArgumentException("Invalid alpha value, must be 0-255: " + a);
	}

	public static void checkR(int r) {
		if ((r & (~0xFF)) != 0)
			throw new IllegalArgumentException("Invalid red value, must be 0-255: " + r);
	}

	public static void checkG(int g) {
		if ((g & (~0xFF)) != 0)
			throw new IllegalArgumentException("Invalid green value, must be 0-255: " + g);
	}

	public static void checkB(int b) {
		if ((b & (~0xFF)) != 0)
			throw new IllegalArgumentException("Invalid blue value, must be 0-255: " + b);
	}
	
	public static void checkARGB(int a, int r, int g, int b) {
		checkA(a);
		checkR(r);
		checkG(g);
		checkB(b);
	}
	
	public static int deNormalize(float value) {
		return LinMath.clamp((int)(value * 255.0f), 0, 255);
	}

	public static float normalize(int value) {
		return value / 255.0f;
	}
}
