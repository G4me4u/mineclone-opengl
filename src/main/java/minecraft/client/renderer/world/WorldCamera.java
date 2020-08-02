package minecraft.client.renderer.world;

import minecraft.common.math.Mat4;
import minecraft.common.math.ViewFrustum;

public class WorldCamera {

	private float x;
	private float y;
	private float z;
	
	private float rx;
	private float ry;
	
	private final Mat4 viewMatrix;
	private final Mat4 projMatrix;
	private final Mat4 projViewMatrix;

	private final ViewFrustum viewFrustum;
	
	private boolean viewMatrixNeedsRefresh;
	private boolean finalMatrixNeedsRefresh;
	private boolean viewFrustumNeedsRefresh;
	
	public WorldCamera() {
		viewMatrix = new Mat4();
		projMatrix = new Mat4();
		projViewMatrix = new Mat4();
		
		viewFrustum = new ViewFrustum();
		
		viewMatrixNeedsRefresh = false;
		finalMatrixNeedsRefresh = false;
		viewFrustumNeedsRefresh = false;
	}
	
	public void setPerspective(float fov, float aspect, float near, float far) {
		projMatrix.toPerspective(fov, aspect, near, far);

		invalidateView();
	}
	
	public synchronized Mat4 getViewMatrix() {
		if (viewMatrixNeedsRefresh) {
			viewMatrix.toIdentity();
			viewMatrix.rotateX(rx).rotateY(ry);
			viewMatrix.translate(x, y, z);
			
			viewMatrixNeedsRefresh = false;
		}
		
		return viewMatrix;
	}
	
	public synchronized Mat4 getProjViewMatrix() {
		if (finalMatrixNeedsRefresh) {
			projMatrix.mul(getViewMatrix(), projViewMatrix);
			
			finalMatrixNeedsRefresh = false;
		}
		
		return projViewMatrix;
	}
	
	public Mat4 getProjectionMatrix() {
		return projMatrix;
	}
	
	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		invalidateView();
	}

	public void setRotation(float rx, float ry) {
		this.rx = rx;
		this.ry = ry;
		
		invalidateView();
	}
	
	private void invalidateView() {
		viewMatrixNeedsRefresh = true;
		finalMatrixNeedsRefresh = true;
		viewFrustumNeedsRefresh = true;
	}
	
	public ViewFrustum getViewFrustum() {
		if (viewFrustumNeedsRefresh) {
			viewFrustum.initFrustum(getProjViewMatrix());
			viewFrustumNeedsRefresh = false;
		}
		
		return viewFrustum;
	}
}
