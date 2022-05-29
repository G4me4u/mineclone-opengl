package mineclone.client.graphic.opengl;

import static org.lwjgl.opengl.GL45.*;

import java.util.ArrayList;
import java.util.List;

import mineclone.client.graphic.BufferAttrib;
import mineclone.client.graphic.BufferAttribType;
import mineclone.client.graphic.BufferDataType;
import mineclone.client.graphic.BufferLayout;
import mineclone.common.IResource;
import mineclone.common.util.DebugUtil;

public class VertexArray implements IResource {

	private final int vertexArrayHandle;
	
	private final List<BufferLayout> bindingLayouts;
	private int nextAttribIndex;
	
	public VertexArray() {
		vertexArrayHandle = glCreateVertexArrays();
		
		bindingLayouts = new ArrayList<>();
		nextAttribIndex = 0;
	}

	public int addVertexBuffer(VertexBuffer buffer) {
		return addVertexBuffer(buffer, false);
	}
	
	public int addVertexBuffer(VertexBuffer buffer, boolean instanced) {
		int bindingIndex = prepareBufferBinding(buffer.getLayout(), instanced);
		
		setBufferBinding(buffer, bindingIndex);
		
		return bindingIndex;
	}
	
	public void setBufferBinding(VertexBuffer buffer, int bindingIndex) {
		BufferLayout layout = buffer.getLayout();
		
		if (DebugUtil.PERFORM_CHECKS) {
			if (bindingIndex < 0 || bindingIndex >= bindingLayouts.size())
				throw new IllegalArgumentException("Buffer binding does not exist!");
			if (!layout.equals(bindingLayouts.get(bindingIndex)))
				throw new IllegalArgumentException("Buffer layout does not match binding layout!");
		}

		glVertexArrayVertexBuffer(vertexArrayHandle, bindingIndex, buffer.getHandle(), 0, layout.getStride());
	}

	public int prepareBufferBinding(BufferLayout layout) {
		return prepareBufferBinding(layout, false);
	}
	
	public int prepareBufferBinding(BufferLayout layout, boolean instanced) {
		int bindingIndex = bindingLayouts.size();

		for (BufferAttrib attrib : layout) {
			BufferAttribType attribType = attrib.getType();
			BufferDataType internalType = attribType.getInternalType();
			
			switch (internalType) {
			case FLOAT:
				for (int i = 0; i < attribType.getLocationCount(); i++) {
					glEnableVertexArrayAttrib(vertexArrayHandle, nextAttribIndex);
					glVertexArrayAttribFormat(vertexArrayHandle, 
					                          nextAttribIndex, 
					                          attribType.getComponentCount(), 
					                          getGLenumFromDataType(attrib.getDataType()),
					                          attrib.isNormalized(),
					                          attrib.getBufferOffset());
					glVertexArrayAttribBinding(vertexArrayHandle, nextAttribIndex, bindingIndex);
				
					nextAttribIndex++;
				}
				break;
			case BYTE:
			case UBYTE:
			case SHORT:
			case USHORT:
			case INT:
			case UINT:
			case BOOL:
				glEnableVertexArrayAttrib(vertexArrayHandle, nextAttribIndex);
				glVertexArrayAttribIFormat(vertexArrayHandle, 
				                           nextAttribIndex, 
				                           attribType.getComponentCount(), 
				                           getGLenumFromDataType(attrib.getDataType()),
				                           attrib.getBufferOffset());
				glVertexArrayAttribBinding(vertexArrayHandle, nextAttribIndex, bindingIndex);

				nextAttribIndex++;
				break;
			default:
				throw new IllegalStateException("Unknown internal type: " + internalType);
			}
		}

		bindingLayouts.add(layout);
		
		if (instanced)
			glVertexArrayBindingDivisor(vertexArrayHandle, bindingIndex, 1);
		
		return bindingIndex;
	}
	
	private static int getGLenumFromDataType(BufferDataType dataType) {
		switch (dataType) {
		case FLOAT:
			return GL_FLOAT;
		case BYTE:
			return GL_BYTE;
		case UBYTE:
			return GL_UNSIGNED_BYTE;
		case SHORT:
			return GL_SHORT;
		case USHORT:
			return GL_UNSIGNED_SHORT;
		case INT:
			return GL_INT;
		case UINT:
			return GL_UNSIGNED_INT;
		case BOOL:
			return GL_BOOL;
		}
		
		throw new IllegalArgumentException("Unknown data type: " + dataType);
	}
	
	public void bind() {
		glBindVertexArray(vertexArrayHandle);
	}
	
	public void unbind() {
		glBindVertexArray(0);
	}
	
	@Override
	public void close() {
		glDeleteVertexArrays(vertexArrayHandle);
	}
}
