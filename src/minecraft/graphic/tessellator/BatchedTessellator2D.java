package minecraft.graphic.tessellator;

import static org.lwjgl.opengl.GL45.*;

import minecraft.graphic.opengl.buffer.BufferLayout;
import minecraft.graphic.opengl.buffer.VertexBuffer;
import minecraft.util.DebugUtil;

/**
 * @author Christian
 */
public class BatchedTessellator2D extends AbstractTessellator2D {
	
	protected final VertexAttribBuilder builder;
	
	private boolean building;
	
	public BatchedTessellator2D(BufferLayout layout) {
		builder = new VertexAttribBuilder(layout);
		
		building = false;
	}

	public void beginScene() {
		if (DebugUtil.PERFORM_CHECKS && building)
			throw new IllegalStateException("Tessellator is already building!");
		
		building = true;
	}

	public void endScene() {
		if (DebugUtil.PERFORM_CHECKS) {
			if (!building)
				throw new IllegalStateException("Tessellator is not building!");
			
			if (!transformStack.isEmpty())
				throw new IllegalStateException("Transform stack was not popped properly!");
			if (!clipStack.isEmpty())
				throw new IllegalStateException("Clip stack was not popped properly!");
		}
		
		building = false;
	}
	
	public void drawAndFlushScene(VertexBuffer buffer) {
		if (DebugUtil.PERFORM_CHECKS && building)
			throw new IllegalStateException("Tessellator is building!");
		
		buffer.bufferSubData(builder.getReadbleBuffer(), 0);
		
		bindActiveTextures();
		glDrawArrays(GL_TRIANGLES, 0, builder.getVertexCount());
	
		flush();
	}
	
	@Override
	protected void flush() {
		super.flush();
	
		builder.clear();
	}

	@Override
	protected void drawTriangleNoTransform(float x0, float y0, float u0, float v0, 
	                                       float x1, float y1, float u1, float v1, 
	                                       float x2, float y2, float u2, float v2) {
		
		if (DebugUtil.PERFORM_CHECKS && !building)
			throw new IllegalStateException("Tessellator is not building!");
		
		super.drawTriangleNoTransform(x0, y0, u0, v0, x1, y1, u1, v1, x2, y2, u2, v2);
	}
	
	@Override
	protected VertexAttribBuilder getBuilder() {
		return builder;
	}
	
	@Override
	public void dispose() {
		builder.close();
	}
}
