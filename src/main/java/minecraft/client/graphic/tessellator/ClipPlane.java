package minecraft.client.graphic.tessellator;

import minecraft.common.math.Vec3;

/**
 * @author Christian
 */
public class ClipPlane {

	private static final float EPSILON = 0.001f;
	
	private final float a;
	private final float b;
	private final float c;
	private final float d;

	/**
	 * Constructs a plane that follows the basic parameterized formula
	 * for a plane {@code Nx * x + Ny * y + Nz * z = P . N}, where N
	 * is the normal vector and P is a point on the plane as a vector.
	 * 
	 * @param normal - The normal vector of the constructed plane.
	 * @param point - An arbitrary point on the constructed plane.
	 */
	public ClipPlane(Vec3 normal, Vec3 point) {
		this(normal.x, normal.y, normal.z, normal.dot(point));
	}
	
	/**
	 * Constructs a plane that follows the basic formula for a plane
	 * {@code a * x + b * y + c * z = d}, where (a, b, c) is the
	 * normal vector and d is a constant that satisfies the plane.
	 * 
	 * @param a - The x-coordinate of the normal vector
	 * @param b - The y-coordinate of the normal vector
	 * @param c - The z-coordinate of the normal vector
	 * @param d - A constant that satisfies the plane.
	 */
	public ClipPlane(float a, float b, float c, float d) {
		float nl2 = a * a + b * b + c * c;
		if (nl2 < EPSILON * EPSILON)
			throw new IllegalArgumentException("Normal vector has near zero length.");
		
		// Normalize the plane
		float nl = (float)Math.sqrt(nl2);
		
		this.a = a / nl;
		this.b = b / nl;
		this.c = c / nl;
		this.d = d / nl;
	}
	
	/**
	 * Checks whether the given point is on the positive point space,
	 * or side, of this plane. For more information on the definition
	 * of the positive side, see {@link #contains(float, float, float)}
	 * 
	 * @param p - the point to check.
	 * 
	 * @return True, if the point is on the positive side of this plane,
	 *         false otherwise.
	 * 
	 * @see #contains(float, float, float)
	 */
	public boolean contains(Vec3 p) {
		return contains(p.x, p.y, p.z);
	}
	
	/**
	 * Checks whether the given point is on the positive point space,
	 * or side, of this plane. In other words, this function returns
	 * true if and only if the dot product between this plane's normal
	 * and a vector from the plane to the given point is non-negative.
	 * 
	 * @param x - the x-coordinate of the point.
	 * @param y - the y-coordinate of the point.
	 * @param z - the z-coordinate of the point.
	 * 
	 * @return True, if the point is on the positive side of this plane,
	 *         false otherwise.
	 */
	public boolean contains(float x, float y, float z) {
		return (a * x + b * y + c * z >= d);
	}
	
	/**
	 * Calculates the intersection between the given line segment and this
	 * plane, if it exists. For more information how this intersection is
	 * calculated see {@link #intersect(float, float, float, float, float, float)}
	 * 
	 * @param p0 - The start point of the line segment.
	 * @param p1 - The end point of the line segment.
	 * 
	 * @see #intersect(float, float, float, float, float, float)
	 */
	public float intersect(Vec3 p0, Vec3 p1) {
		return intersect(p0.x, p0.y, p0.z, p1.x, p1.y, p1.z);
	}
	
	/**
	 * Calculates the intersection between the given line segment and this
	 * plane, if it exists. The intersection point (xi, yi, zi) can be found
	 * using the following code snippet:
	 * <pre>
	 *     float t = shape.intersect(x0, y0, z0, x1, y1, z1);
	 *     if (Float.isNaN(t)) {
	 *         // In this case there is no intersection.
	 *     } else {
	 *         float xi = x0 + (x1 - x0) * t;
	 *         float yi = y0 + (y1 - y0) * t;
	 *         float zi = z0 + (z1 - z0) * t;
	 *         
	 *         // Use the intersection point here.
	 *     }
	 * </pre>
	 * 
	 * The calculated intersection point is guaranteed to be on the positive
	 * side of this plane, see {@link #contains(float, float)}, even though
	 * the function might return false because of rounding errors.
	 * <br><br>
	 * If there is no intersection, the returned value will be {@link Float#NaN}.
	 * 
	 * @param x0 - the x-coordinate of the start point in the line segment.
	 * @param y0 - the y-coordinate of the start point in the line segment.
	 * @param z0 - the z-coordinate of the start point in the line segment.
	 * 
	 * @param x1 - the x-coordinate of the end point in the line segment.
	 * @param y1 - the y-coordinate of the end point in the line segment.
	 * @param z1 - the z-coordinate of the end point in the line segment.
	 * 
	 * @return The interpolation value of the first intersection from the
	 *         start point to the edge of this shape. The interpolation
	 *         value is between ]0.0; 1.0[, or {@link Float#NaN} if there
	 *         was no intersection.
	 * 
	 * @see #contains(float, float)
	 */
	public float intersect(float x0, float y0, float z0, float x1, float y1, float z1) {
		float d0 = a * x0 + b * y0 + c * z0;
		float d1 = a * x1 + b * y1 + c * z1;
		float dd = d0 - d1;
		
		if (dd > EPSILON || dd < -EPSILON) {
			float t = (d0 - d) / dd;
			// Make sure the intersection is on the line segment.
			if (t > 0.0f && t < 1.0f)
				return t;
		}
		
		return Float.NaN;
	}
}
