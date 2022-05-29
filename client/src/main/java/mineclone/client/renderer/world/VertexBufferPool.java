package mineclone.client.renderer.world;

import java.util.Deque;
import java.util.LinkedList;

import mineclone.client.graphic.BufferLayout;
import mineclone.client.graphic.opengl.VertexBuffer;
import mineclone.common.IResource;

public class VertexBufferPool implements IResource {

	public static final int MAX_VERTEX_COUNT = (2 << 14) - 1;
	
	private final BufferLayout layout;
	private final Deque<VertexBuffer> buffers;
	
	private boolean closed;
	
	public VertexBufferPool(BufferLayout layout) {
		this.layout = layout;
		buffers = new LinkedList<>();
	
		closed = false;
	}
	
	public VertexBuffer aquire(int vertexCount) {
		if (vertexCount > MAX_VERTEX_COUNT)
			throw new IllegalArgumentException("Vertex count exceeds maximum!");
		if (closed)
			throw new IllegalStateException("Buffer pool is closed!");
		
		if (!buffers.isEmpty())
			return buffers.remove();
		return new VertexBuffer(layout, MAX_VERTEX_COUNT);
	}

	public void free(VertexBuffer buffer) {
		if (closed) {
			buffer.close();
		} else {
			if (MAX_VERTEX_COUNT * layout.getStride() != buffer.getCapacity())
				throw new IllegalArgumentException("Buffer capacity invalid.");
			
			buffers.add(buffer);
		}
	}
	
	public BufferLayout getLayout() {
		return layout;
	}
	
	@Override
	public void close() {
		if (!closed && !buffers.isEmpty()) {
			closed = true;

			buffers.forEach(VertexBuffer::close);
			buffers.clear();
		}
	}
}
