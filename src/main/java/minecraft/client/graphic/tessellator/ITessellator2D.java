package minecraft.client.graphic.tessellator;

import minecraft.client.graphic.ITextureRegion;
import minecraft.common.IResource;
import minecraft.common.math.Mat3;
import minecraft.common.math.Vec2;

/**
 * @author Christian
 */
public interface ITessellator2D extends IResource {

	default public void drawQuad(Vec2 p0, Vec2 p1) {
		drawQuad(p0.x, p0.y, p1.x, p1.y);
	}
	
	public void drawQuad(float x0, float y0, float x1, float y1);

	default public void drawQuadRegion(Vec2 p0, float u0, float v0, Vec2 p1, float u1, float v1) {
		drawQuadRegion(p0.x, p0.y, p1.x, p1.y, u0, v0, u1, v1);
	}
	
	public void drawQuadRegion(float x0, float y0, float u0, float v0, float x1, float y1, float u1, float v1);

	default public void drawTriangle(Vec2 p0, Vec2 p1, Vec2 p2) {
		drawTriangle(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y);
	}

	public void drawTriangle(float x0, float y0, float x1, float y1, float x2, float y2);

	default public void drawTriangleRegion(Vec2 p0, float u0, float v0, 
	                                       Vec2 p1, float u1, float v1, 
	                                       Vec2 p2, float u2, float v2) {
		
		drawTriangleRegion(p0.x, p0.y, u0, v0, p1.x, p1.y, u1, v1, p2.x, p2.y, u2, v2);
	}

	public void drawTriangleRegion(float x0, float y0, float u0, float v0, 
	                               float x1, float y1, float u1, float v1,
	                               float x2, float y2, float u2, float v2);
	
	default public void drawLine(Vec2 p0, Vec2 p1) {
		drawLine(p0.x, p0.y, p1.x, p1.y);
	}
	
	default public void drawLine(float x0, float y0, float x1, float y1) {
		drawLine(x0, y0, x1, y1, 1.0f);
	}

	default public void drawLine(Vec2 p0, Vec2 p1, float lineWidth) {
		drawLine(p0.x, p0.y, p1.x, p1.y, lineWidth);
	}

	public void drawLine(float x0, float y0, float x1, float y1, float lineWidth);
	
	default public void setColor(Color color) {
		setColorGradient(new ConstantColorGradient2D(color));
	}

	public ColorGradient2D getColorGradient();
	
	public void setColorGradient(ColorGradient2D colorGradient);
	
	public ITextureRegion getTextureRegion();
	
	public void setTextureRegion(ITextureRegion textureRegion);
	
	public void pushTransform();
	
	public void pushTransform(Mat3 transform);

	public Mat3 popTransform();
	
	public void toIdentity();
	
	public void translate(float tx, float ty);
	
	public void scale(float xs, float ys);

	public void rotateZ(float radians);

	default public void pushClip(float x0, float y0, float x1, float y1) {
		pushClip(new ClipRect(x0, y0, x1, y1));
	}

	public void pushClip(ClipShape shape);

	public ClipShape popClip();
	
}
