package minecraft.client.graphic.tessellator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import minecraft.client.graphic.ITexture;
import minecraft.client.graphic.ITextureRegion;
import minecraft.common.math.LinMath;
import minecraft.common.math.Mat3;
import minecraft.common.math.Vec2;
import minecraft.common.util.DebugUtil;

/**
 * @author Christian
 */
public abstract class AbstractTessellator2D implements ITessellator2D {

	private static final byte WHITE_TEXTURE_INDEX = Byte.MIN_VALUE;
	private static final int MAX_TEXTURE_COUNT = 32;
	
	private static final int MAX_CLIPPED_CACHE_SIZE = 32;
	private static final float Z_CLIP_OFFSET = 0.0f;
	
	private static final ClipTriangle[] EMPTY_TRIANGLE_CACHE = new ClipTriangle[0];
	
	protected ColorGradient2D colorGradient;
	
	protected final List<ITexture> activeTextures;
	protected ITextureRegion textureRegion;
	protected int textureIndex;
	
	protected final Deque<Mat3> transformStack;

	protected final Deque<ClipShape> clipStack;
	protected ClipTriangle[] triangleCache;
	
	public AbstractTessellator2D() {
		colorGradient = new ConstantColorGradient2D(Color.WHITE);
		
		activeTextures = new ArrayList<>();
		textureRegion = null;
		textureIndex = WHITE_TEXTURE_INDEX;
		
		transformStack = new LinkedList<>();
		
		clipStack = new LinkedList<>();
		triangleCache = EMPTY_TRIANGLE_CACHE;
	}

	protected void bindActiveTextures() {
		for (int i = 0; i < activeTextures.size(); i++)
			activeTextures.get(i).bind(i);
	}
	
	protected void flush() {
		colorGradient = new ConstantColorGradient2D(Color.WHITE);
		
		activeTextures.clear();

		clipStack.clear();
		transformStack.clear();
		
		triangleCache = EMPTY_TRIANGLE_CACHE;
	}

	@Override
	public void drawQuad(float x0, float y0, float x1, float y1) {
		if (textureRegion != null) {
			float u0 = textureRegion.getU0();
			float v0 = textureRegion.getV0();
			float u1 = textureRegion.getU1();
			float v1 = textureRegion.getV1();
			
			drawQuadImpl(x0, y0, u0, v0, x1, y1, u1, v1);
		} else {
			drawQuadImpl(x0, y0, 0.0f, 0.0f, x1, y1, 0.0f, 0.0f);
		}
	}
	
	@Override
	public void drawQuadRegion(float x0, float y0, float u0, float v0, float x1, float y1, float u1, float v1) {
		if (textureRegion != null) {
			ITextureRegion region = textureRegion.getUVRegion(u0, v0, u1, v1);
			
			u0 = region.getU0();
			v0 = region.getV0();
			u1 = region.getU1();
			v1 = region.getV1();
			
			drawQuadImpl(x0, y0, u0, v0, x1, y1, u1, v1);
		} else {
			drawQuadImpl(x0, y0, 0.0f, 0.0f, x1, y1, 0.0f, 0.0f);
		}
	}
	
	private void drawQuadImpl(float x0, float y0, float u0, float v0, float x1, float y1, float u1, float v1) {
		Mat3 transform = transformStack.peek();

		if (transform != null) {
			Vec2 c0 = transform.mul(new Vec2(x0, y0)); // Top-left
			Vec2 c1 = transform.mul(new Vec2(x0, y1)); // Bottom-left
			Vec2 c2 = transform.mul(new Vec2(x1, y1)); // Bottom-right
			Vec2 c3 = transform.mul(new Vec2(x1, y0)); // Top-right
			
			drawTriangleNoTransform(c0.x, c0.y, u0, v0, c1.x, c1.y, u0, v1, c3.x, c3.y, u1, v0);
			drawTriangleNoTransform(c2.x, c2.y, u1, v1, c3.x, c3.y, u1, v0, c1.x, c1.y, u0, v1);
		} else {
			drawTriangleNoTransform(x0, y0, u0, v0, x0, y1, u0, v1, x1, y0, u1, v0);
			drawTriangleNoTransform(x1, y1, u1, v1, x1, y0, u1, v0, x0, y1, u0, v1);
		}
	}
	
	@Override
	public void drawTriangle(float x0, float y0, float x1, float y1, float x2, float y2) {
		if (textureRegion != null) {
			float u0 = textureRegion.getU0();
			float v0 = textureRegion.getV0();
			float u1 = textureRegion.getU1();
			float v1 = textureRegion.getV1();
			
			// Default texture region is top left part
			// of the texture region.
			drawTriangleImpl(x0, y0, u0, v0, x1, y1, u1, v0, x2, y2, u0, v1);
		} else {
			drawTriangleImpl(x0, y0, 0.0f, 0.0f, x1, y1, 0.0f, 0.0f, x2, y2, 0.0f, 0.0f);
		}
	}
	
	@Override
	public void drawTriangleRegion(float x0, float y0, float u0, float v0, 
	                               float x1, float y1, float u1, float v1,
	                               float x2, float y2, float u2, float v2) {
		
		if (textureRegion != null) {
			float du = textureRegion.getU1() - textureRegion.getU0();
			float dv = textureRegion.getV1() - textureRegion.getV0();
			
			float nu0 = textureRegion.getU0() + du * u0;
			float nv0 = textureRegion.getV0() + dv * v0;
			
			float nu1 = textureRegion.getU0() + du * u1;
			float nv1 = textureRegion.getV0() + dv * v1;

			float nu2 = textureRegion.getU0() + du * u2;
			float nv2 = textureRegion.getV0() + dv * v2;
			
			drawTriangleImpl(x0, y0, nu0, nv0, x1, y1, nu1, nv1, x2, y2, nu2, nv2);
		} else {
			drawTriangleImpl(x0, y0, 0.0f, 0.0f, x1, y1, 0.0f, 0.0f, x2, y2, 0.0f, 0.0f);
		}
	}
	
	private void drawTriangleImpl(float x0, float y0, float u0, float v0, 
	                              float x1, float y1, float u1, float v1, 
	                              float x2, float y2, float u2, float v2) {

		Mat3 transform = transformStack.peek();

		if (transform != null) {
			Vec2 p0 = transform.mul(new Vec2(x0, y0));
			Vec2 p1 = transform.mul(new Vec2(x1, y1));
			Vec2 p2 = transform.mul(new Vec2(x2, y2));
			
			drawTriangleNoTransform(p0.x, p0.y, u0, v0, p1.x, p1.y, u1, v1, p2.x, p2.y, u2, v2);
		} else {
			drawTriangleNoTransform(x0, y0, u0, v0, x1, y1, u1, v1, x2, y2, u2, v2);
		}
	}

	@Override
	public void drawLine(float x0, float y0, float x1, float y1, float lineWidth) {
		Mat3 transform = transformStack.peek();

		if (transform != null) {
			Vec2 p0 = transform.mul(new Vec2(x0, y0));
			Vec2 p1 = transform.mul(new Vec2(x1, y1));
			
			drawLineNoTransform(p0.x, p0.y, p1.x, p1.y, lineWidth);
		} else {
			drawLineNoTransform(x0, y0, x1, y1, lineWidth);
		}
	}
	
	private void drawLineNoTransform(float x0, float y0, float x1, float y1, float lineWidth) {
		float dx = x1 - x0;
		float dy = y1 - y0;
		
		float distSqr = dx * dx + dy * dy;
		if (distSqr >= LinMath.EPSILON * LinMath.EPSILON) {
			float m = 0.5f * lineWidth / ((float)Math.sqrt(distSqr));
			
			dx *= m;
			dy *= m;
			
			// Vertical line: Top-left
			float lx0 = x0 - dy;
			float ly0 = y0 + dx;

			// Vertical line: Bottom-left
			float lx1 = x1 - dy;
			float ly1 = y1 + dx;

			// Vertical line: Bottom-right
			float lx2 = x1 + dy;
			float ly2 = y1 - dx;

			// Vertical line: Top-right
			float lx3 = x0 + dy;
			float ly3 = y0 - dx;
			
			if (textureRegion != null) {
				float u0 = textureRegion.getU0();
				float v0 = textureRegion.getV0();
				float u1 = textureRegion.getU1();
				float v1 = textureRegion.getV1();
				
				drawTriangleNoTransform(lx0, ly0, u0, v0, lx1, ly1, u0, v1, lx3, ly3, u1, v0);
				drawTriangleNoTransform(lx2, ly2, u1, v1, lx3, ly3, u1, v0, lx1, ly1, u0, v1);
			} else {
				drawTriangleNoTransform(lx0, ly0, 0.0f, 0.0f, lx1, ly1, 0.0f, 0.0f, lx3, ly3, 0.0f, 0.0f);
				drawTriangleNoTransform(lx2, ly2, 0.0f, 0.0f, lx3, ly3, 0.0f, 0.0f, lx1, ly1, 0.0f, 0.0f);
			}
		}
	}
	
	protected void drawTriangleNoTransform(float x0, float y0, float u0, float v0, 
	                                       float x1, float y1, float u1, float v1, 
	                                       float x2, float y2, float u2, float v2) {

		ClipShape clipShape = clipStack.peek();
		
		if (clipShape != null && clipShape.getPlaneCount() > 0) {
			// Note: we need an extra triangle for storing
			//       the initial triangle when drawing.
			ensureCacheSize(clipShape.getPlaneCount() + 1);
			
			triangleCache[0].set(x0, y0, u0, v0, x1, y1, u1, v1, x2, y2, u2, v2);
			clipAndTessellate(clipShape.getPlanes(), 0);
		} else {
			// In this case we can tessellate the triangle immediately
			// and skip the expensive clipping routine.
			tessellateTriangle(x0, y0, u0, v0, x1, y1, u1, v1, x2, y2, u2, v2);
		}
	}
	
	private void ensureCacheSize(int cacheSize) {
		int currentSize = triangleCache.length;
		
		// In case the current cache size exceeds the maximum it should
		// be the exact cache size that we are looking for.
		if (currentSize >  MAX_CLIPPED_CACHE_SIZE && currentSize == cacheSize)
			return;
		if (currentSize <= MAX_CLIPPED_CACHE_SIZE && currentSize >= cacheSize)
			return;
		
		triangleCache = Arrays.copyOf(triangleCache, cacheSize);
		for (int i = currentSize; i < cacheSize; i++)
			triangleCache[i] = new ClipTriangle();
	}
	
	private void clipAndTessellate(ClipPlane[] planes, int planeIndex) {
		ClipTriangle t = triangleCache[planeIndex];
		
		if (planeIndex >= planes.length) {
			tessellateTriangle(t.v0.x, t.v0.y, t.v0.u, t.v0.v,
			                   t.v1.x, t.v1.y, t.v1.u, t.v1.v,
			                   t.v2.x, t.v2.y, t.v2.u, t.v2.v);
		} else {
			ClipPlane plane = planes[planeIndex];
			
			if (plane.contains(t.v0.x, t.v0.y, Z_CLIP_OFFSET)) {
				if (plane.contains(t.v1.x, t.v1.y, Z_CLIP_OFFSET)) {
					if (plane.contains(t.v2.x, t.v2.y, Z_CLIP_OFFSET)) {
						triangleCache[planeIndex + 1].set(t);
						clipAndTessellate(planes, planeIndex + 1);
					} else {
						clip2Inside(planes, planeIndex, t.v0, t.v1, t.v2);
					}
				} else {
					if (plane.contains(t.v2.x, t.v2.y, Z_CLIP_OFFSET)) {
						clip2Inside(planes, planeIndex, t.v2, t.v0, t.v1);
					} else {
						clip2Outside(planes, planeIndex, t.v0, t.v1, t.v2);
					}
				}
			} else {
				if (plane.contains(t.v1.x, t.v1.y, Z_CLIP_OFFSET)) {
					if (plane.contains(t.v2.x, t.v2.y, Z_CLIP_OFFSET)) {
						clip2Inside(planes, planeIndex, t.v1, t.v2, t.v0);
					} else {
						clip2Outside(planes, planeIndex, t.v1, t.v2, t.v0);
					}
				} else {
					if (plane.contains(t.v2.x, t.v2.y, Z_CLIP_OFFSET)) {
						clip2Outside(planes, planeIndex, t.v2, t.v0, t.v1);
					} else {
						// Triangle is completely outside of the clip
						// shape. Discard it completely.
					}
				}
			}
		}
	}
	
	private void clip2Inside(ClipPlane[] planes, int planeIndex, ClipVertex in0, ClipVertex in1, ClipVertex out) {
		ClipPlane plane = planes[planeIndex];
		ClipTriangle t = triangleCache[planeIndex + 1];
		
		t.v0.set(in0);
		interpolateClipVertex(plane, out, in1, t.v1);
		interpolateClipVertex(plane, out, in0, t.v2);

		clipAndTessellate(planes, planeIndex + 1);
		
		t.v0.set(in1);
		//t.v1.set(t.v1);
		t.v2.set(in0);

		clipAndTessellate(planes, planeIndex + 1);
	}
	
	private void clip2Outside(ClipPlane[] planes, int planeIndex, ClipVertex in, ClipVertex out0, ClipVertex out1) {
		ClipPlane plane = planes[planeIndex];
		ClipTriangle t = triangleCache[planeIndex + 1];
		
		t.v0.set(in);
		interpolateClipVertex(plane, out0, in, t.v1);
		interpolateClipVertex(plane, out1, in, t.v2);

		clipAndTessellate(planes, planeIndex + 1);
	}

	private void interpolateClipVertex(ClipPlane plane, ClipVertex out, ClipVertex in, ClipVertex result) {
		float t = plane.intersect(out.x, out.y, Z_CLIP_OFFSET, in.x, in.y, Z_CLIP_OFFSET);
		
		result.x = out.x + (in.x - out.x) * t;
		result.y = out.y + (in.y - out.y) * t;
		result.u = out.u + (in.u - out.u) * t;
		result.v = out.v + (in.v - out.v) * t;
	}
	
	protected void tessellateTriangle(float x0, float y0, float u0, float v0, 
	                                  float x1, float y1, float u1, float v1, 
	                                  float x2, float y2, float u2, float v2) {
		
		tessellateVertex(x0, y0, u0, v0);
		tessellateVertex(x1, y1, u1, v1);
		tessellateVertex(x2, y2, u2, v2);
	}
	
	protected void tessellateVertex(float x, float y, float u, float v) {
		Color color = colorGradient.getColor(x, y, transformStack.peek());
		
		byte r = (byte)color.getRed();
		byte g = (byte)color.getGreen();
		byte b = (byte)color.getBlue();
		byte a = (byte)color.getAlpha();

		VertexAttribBuilder builder = getBuilder();
		
		builder.putFloat2(x, y);
		builder.putUByte4(r, g, b, a);
		builder.putFloat2(u, v);
		builder.putByte((byte)textureIndex);
	
		builder.next();
	}
	
	protected abstract VertexAttribBuilder getBuilder();
	
	@Override
	public ColorGradient2D getColorGradient() {
		return colorGradient;
	}
	
	@Override
	public void setColorGradient(ColorGradient2D colorGradient) {
		if (DebugUtil.PERFORM_CHECKS) {
			if (colorGradient == null)
				throw new IllegalArgumentException("colorGradient is null!");
		}

		this.colorGradient = colorGradient;
	}
	
	@Override
	public ITextureRegion getTextureRegion() {
		return textureRegion;
	}

	@Override
	public void setTextureRegion(ITextureRegion textureRegion) {
		this.textureRegion = textureRegion;
		
		if (textureRegion != null) {
			textureIndex = getOrAddTextureIndex(textureRegion.getTexture());
		} else {
			textureIndex = WHITE_TEXTURE_INDEX;
		}
	}
	
	private int getOrAddTextureIndex(ITexture texture) {
		int nextTextureIndex = activeTextures.size();
		
		for (int i = 0; i < nextTextureIndex; i++) {
			ITexture activeTexture = activeTextures.get(i);
			
			if (activeTexture == texture)
				return i;
		}
		
		if (DebugUtil.PERFORM_CHECKS) {
			if (nextTextureIndex >= MAX_TEXTURE_COUNT)
				throw new IllegalStateException("Too many textures. Maximum is " + MAX_TEXTURE_COUNT);
		}
		
		activeTextures.add(texture);
	
		return nextTextureIndex;
	}
	
	@Override
	public void pushTransform() {
		if (transformStack.isEmpty()) {
			pushTransform(new Mat3());
		} else {
			pushTransform(new Mat3(transformStack.peek()));
		}
	}
	
	@Override
	public void pushTransform(Mat3 transform) {
		transformStack.push(transform);
	}

	@Override
	public Mat3 popTransform() {
		if (DebugUtil.PERFORM_CHECKS) {
			if (transformStack.isEmpty())
				throw new IllegalStateException("Transform stack is empty!");
		}

		return transformStack.remove();
	}
	
	@Override
	public void toIdentity() {
		if (DebugUtil.PERFORM_CHECKS) {
			if (transformStack.isEmpty())
				throw new IllegalStateException("Transform stack is empty!");
		}
		
		transformStack.peek().toIdentity();
	}

	@Override
	public void translate(float xt, float yt) {
		if (DebugUtil.PERFORM_CHECKS) {
			if (transformStack.isEmpty())
				throw new IllegalStateException("Transform stack is empty!");
		}
		
		transformStack.peek().translate(xt, yt);
	}
	
	@Override
	public void scale(float xs, float ys) {
		if (DebugUtil.PERFORM_CHECKS) {
			if (transformStack.isEmpty())
				throw new IllegalStateException("Transform stack is empty!");
		}
		
		transformStack.peek().scale(xs, ys);
	}

	@Override
	public void rotateZ(float radians) {
		if (DebugUtil.PERFORM_CHECKS) {
			if (transformStack.isEmpty())
				throw new IllegalStateException("Transform stack is empty!");
		}
		
		transformStack.peek().rotateZ(radians);
	}
	
	@Override
	public void pushClip(ClipShape clipShape) {
		clipStack.push(clipShape);
	}

	@Override
	public ClipShape popClip() {
		if (DebugUtil.PERFORM_CHECKS) {
			if (clipStack.isEmpty())
				throw new IllegalStateException("Clip stack is empty!");
		}
	
		return clipStack.remove();
	}
	
	private static class ClipTriangle {

		public ClipVertex v0;
		public ClipVertex v1;
		public ClipVertex v2;
		
		public ClipTriangle() {
			v0 = new ClipVertex();
			v1 = new ClipVertex();
			v2 = new ClipVertex();
		}
		
		public void set(ClipTriangle t) {
			this.v0.set(t.v0);
			this.v1.set(t.v1);
			this.v2.set(t.v2);
		}

		public void set(float x0, float y0, float u0, float v0,
		                float x1, float y1, float u1, float v1,
		                float x2, float y2, float u2, float v2) {

			this.v0.set(x0, y0, u0, v0);
			this.v1.set(x1, y1, u1, v1);
			this.v2.set(x2, y2, u2, v2);
		}
	}
	
	private static class ClipVertex {
		
		public float x;
		public float y;
		public float u;
		public float v;

		public ClipVertex() {
			x = y = u = v = 0.0f;
		}
		
		public void set(ClipVertex other) {
			x = other.x;
			y = other.y;
			u = other.u;
			v = other.v;
		}
		
		public void set(float x, float y, float u, float v) {
			this.x = x;
			this.y = y;
			this.u = u;
			this.v = v;
		}
	}
}
