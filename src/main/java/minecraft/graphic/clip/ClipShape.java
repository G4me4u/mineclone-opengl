package minecraft.graphic.clip;

/**
 * @author Christian
 */
public abstract class ClipShape {

	public abstract ClipShapeBounds getClipBounds();

	public int getPlaneCount() {
		return getPlanes().length;
	}

	public abstract ClipPlane[] getPlanes();
	
}
