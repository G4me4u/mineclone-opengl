package minecraft.client.graphic.tessellator;

import minecraft.common.math.Mat3;

/**
 * @author Christian
 */
public class ConstantColorGradient2D extends ColorGradient2D {

	private final Color color;
	
	public ConstantColorGradient2D(Color color) {
		if (color == null)
			throw new IllegalArgumentException("color is null");
		
		this.color = color;
	}
	
	@Override
	public Color getColor(float x, float y, Mat3 transform) {
		return color;
	}
}
