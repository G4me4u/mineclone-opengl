package mineclone.common.math;

public final class LinMath {
	
	public static final float EPSILON = 0.01f;
	
	public static final float PI      = (float)Math.PI;
	public static final float TWO_PI  = (float)(Math.PI * 2.0);
	public static final float HALF_PI = (float)(Math.PI / 2.0);

	public static final float DEG_TO_RAD = TWO_PI / 360.0f;
	
	private LinMath() {
	}
	
	public static int clamp(int v, int mn, int mx) {
		return (v < mn) ? mn : ((v > mx) ? mx : v);
	}

	public static double clamp(double v, double mn, double mx) {
		return (v < mn) ? mn : ((v > mx) ? mx : v);
	}

	public static long clamp(long v, long mn, long mx) {
		return (v < mn) ? mn : ((v > mx) ? mx : v);
	}
	
	public static float clamp(float v, float mn, float mx) {
		return (v < mn) ? mn : ((v > mx) ? mx : v);
	}
	
	public static boolean nearZero(float value) {
		return nearZero(value, 1.0E-5f);
	}

	public static boolean nearZero(float value, float epsilon) {
		return (value < epsilon && value > -epsilon);
	}

	public static boolean nearZero(double value) {
		return nearZero(value, 1.0E-5);
	}
	
	public static boolean nearZero(double value, double epsilon) {
		return (value < epsilon && value > -epsilon);
	}
	
	public static float normalizeRadians(float rad) {
		rad %= TWO_PI;
		
		if (rad < 0.0f)
			return rad + TWO_PI;
		return rad;
	}

	public static float normalizeDegrees(float deg) {
		deg %= 360.0f;

		if (deg < 0.0f)
			return deg + 360.0f;
		return deg;
	}
}
