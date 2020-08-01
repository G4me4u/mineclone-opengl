package minecraft.graphic.opengl.buffer;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL44.GL_DYNAMIC_STORAGE_BIT;
import static org.lwjgl.opengl.GL45.glCreateBuffers;
import static org.lwjgl.opengl.GL45.glNamedBufferStorage;
import static org.lwjgl.opengl.GL45.glNamedBufferSubData;

import java.nio.ByteBuffer;

import minecraft.IResource;

/**
 * @author Christian
 */
public class VertexBuffer implements IResource {

	private final BufferLayout layout;

	private final int bufferHandle;
	private final int capacity;

	/**
	 * Creates a new empty OpenGL vertex buffer object.
	 * 
	 * @param layout - layout - the shader layout to be used in the buffer.
	 * @param vertexCapacity - the maximum number of vertices in the buffer.
	 */
	public VertexBuffer(BufferLayout layout, int vertexCapacity) {
		this.layout = layout;

		bufferHandle = glCreateBuffers();
		capacity = layout.getStride() * vertexCapacity;

		glNamedBufferStorage(bufferHandle, capacity, GL_DYNAMIC_STORAGE_BIT);
	}
	
	public void bufferSubData(ByteBuffer buffer, int offset) {
		if (buffer.remaining() + offset > capacity)
			throw new IndexOutOfBoundsException("Offset out of bounds: " + capacity);

		glNamedBufferSubData(bufferHandle, offset, buffer);
	}

	public BufferLayout getLayout() {
		return layout;
	}

	int getHandle() {
		return bufferHandle;
	}
	
	public int getCapacity() {
		return capacity;
	}

	@Override
	public void dispose() {
		glDeleteBuffers(bufferHandle);
	}
}
