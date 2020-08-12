package minecraft.client.graphic.tessellator;

import minecraft.common.math.LinMath;
import minecraft.common.math.Mat3;
import minecraft.common.math.Vec2;

/**
 * @author Christian
 */
public class LinearColorGradient2D extends ColorGradient2D {

	private static final float EPSILON = 0.01f;
	
	private final float sx;
	private final float sy;
	private final float ex;
	private final float ey;

	private final Color startColor;
	private final Color endColor;
	
	public LinearColorGradient2D(Vec2 startPoint, Color startColor, Vec2 endPoint, Color endColor) {
		if (startColor == null)
			throw new IllegalArgumentException("startColor is null!");
		if (endColor == null)
			throw new IllegalArgumentException("endColor is null!");
		
		this.sx = startPoint.x;
		this.sy = startPoint.y;
		this.ex = endPoint.x;
		this.ey = endPoint.y;

		this.startColor = startColor;
		this.endColor = endColor;
	}

	@Override
	public Color getColor(float x, float y, Mat3 transform) {
		if (transform != null) {
			// Apply transform to gradient.
			Vec2 p0 = transform.mul(new Vec2(sx, sy));
			Vec2 p1 = transform.mul(new Vec2(ex, ey));
			
			return getColor(x, y, p0.x, p0.y, p1.x, p1.y);
		}

		return getColor(x, y, sx, sy, ex, ey);
	}
	
	private Color getColor(float x, float y, float sx, float sy, float ex, float ey) {
		// Define a line orthogonal to the gradient going through
		// the start point, where nx * x + ny * y = c.
		float nx = ex - sx;
		float ny = ey - sy;
		float c  = nx * sx + ny * sy;

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
