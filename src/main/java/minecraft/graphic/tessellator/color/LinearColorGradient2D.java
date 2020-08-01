package minecraft.graphic.tessellator.color;

import minecraft.math.LinMath;
import minecraft.math.Mat3;
import minecraft.math.Vec2;

/**
 * @author Christian
 */
public class LinearColorGradient2D extends ColorGradient2D {

	private static final float EPSILON = 0.01f;
	
	private final Vec2 startPoint;
	private final Color startColor;
	private final Vec2 endPoint;
	private final Color endColor;
	
	public LinearColorGradient2D(Vec2 startPoint, Color startColor, Vec2 endPoint, Color endColor) {
		if (startColor == null)
			throw new IllegalArgumentException("startColor is null!");
		if (endColor == null)
			throw new IllegalArgumentException("endColor is null!");
		
		this.startPoint = startPoint.copy();
		this.startColor = startColor;
		this.endPoint = endPoint.copy();
		this.endColor = endColor;
	}

	@Override
	public Color getColor(float x, float y, Mat3 transform) {
		Vec2 p0 = startPoint;
		Vec2 p1 = endPoint;
		
		// Apply transform to gradient.
		if (transform != null) {
			p0 = transform.mul(p0, new Vec2());
			p1 = transform.mul(p1, new Vec2());
		}
		
		// Define a line orthogonal to the gradient going through
		// the start point, where nx * x + ny * y = c.
		float nx = p1.x - p0.x;
		float ny = p1.y - p0.y;
		float c  = nx * p0.x + ny * p0.y;

		// We can find the interpolation value as the distance to
		// our line divided by the total distance between the start
		// and end point. The distance can be calculated as follows:
		//     dist = (nx * x + ny * y - c) / sqrt(nx * nx + ny * ny)
		// Since the length of the normal vector is also the total
		// distance between the start and end points we can simply
		// divide by the distance squared to avoid the square root.
		float totalDistSq = nx * nx + ny * ny;

		float d = nx * x + ny * y - c;
		if (totalDistSq < EPSILON * EPSILON) {
			// The two points are the same.
			return (d > 0.0f) ? endColor : startColor;
		}
		
		float t = LinMath.clamp(d / totalDistSq, 0.0f, 1.0f);
		return startColor.interpolate(endColor, t);
	}
}
