package minecraft.common.math;

public final class LinMath {
	
	public static final float EPSILON = 0.01f;
	
	private LinMath() {
	}
	
	public static int clamp(int v, int mn, int mx) {
		return v < mn ? mn : (v > mx ? mx : v);
	}

	public static double clamp(double v, double mn, double mx) {
		return v < mn ? mn : (v > mx ? mx : v);
	}

	public static long clamp(long v, long mn, long mx) {
		return v < mn ? mn : (v > mx ? mx : v);
	}
	
	public static float clamp(float v, float mn, float mx) {
		return v < mn ? mn : (v > mx ? mx : v);
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
}
