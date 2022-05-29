package mineclone.client.graphic.tessellator;

/**
 * @author Christian
 */
public class ClipRect extends ClipShape {

	private final ClipShapeBounds bounds;
	
	private final ClipPlane[] planes;
	
	public ClipRect(float x0, float y0, float x1, float y1) {
		this.bounds = new ClipShapeBounds(x0, y0, x1, y1);
		
		this.planes = new ClipPlane[] {
			new ClipPlane( 1.0f,  0.0f, 0.0f,  x0), // LEFT
			new ClipPlane(-1.0f,  0.0f, 0.0f, -x1), // RIGHT
			new ClipPlane( 0.0f,  1.0f, 0.0f,  y0), // TOP
			new ClipPlane( 0.0f, -1.0f, 0.0f, -y1)  // BOTTOM
		};
	}

	@Override
	public ClipShapeBounds getClipBounds() {
		return bounds;
	}
	
	@Override
	public ClipPlane[] getPlanes() {
		return planes;
	}
}
