package minecraft.client.graphic.tessellator;

import minecraft.common.math.Mat3;

/**
 * @author Christian
 */
public abstract class ColorGradient2D {

	/**
	 * Calculates the color at the given point with the given linear transform
	 * applied to this gradient.
	 * <br><br>
	 * An implementation of this method must follow the basic principle that
	 * the gradient space is linear. For the gradient to be linear means that
	 * {@code C(x0, y0) + t * (C(x1, y1) - C(x0, y0))} must be equivalent to
	 * {@code C(x0 + t * (x1 - x0), y0 + t * (y1 - y0))} for any value t that
	 * is between 0.0 and 1.0. Likewise, all points on a line that is orthogonal
	 * to the line segment from (x0, y0) to (x1, y1) must result in the same
	 * color. It is legal for a gradient to define the point-space where this
	 * condition holds. That means if either of the two points (x0, y0) or
	 * (x1, y1) outside is of the defined space, the constraint is not required.
	 * E.g. this is important if the gradient is only defined between two points.
	 * 
	 * @param x - the x-coordinate of the given point.
	 * @param y - the y-coordinate of the given point.
	 * @param transform - the given linear transform, or {@code null} if there
	 *                    is no transform for which the identity should be used.
	 * 
	 * @return The color at the given point in this gradient after the linear
	 *         transform was applied.
	 */
	public abstract Color getColor(float x, float y, Mat3 transform);
	
}
