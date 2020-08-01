package minecraft.graphic.tessellator;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import minecraft.IResource;
import minecraft.graphic.opengl.buffer.BufferAttrib;
import minecraft.graphic.opengl.buffer.BufferDataType;
import minecraft.graphic.opengl.buffer.BufferLayout;
import minecraft.math.Mat3;
import minecraft.math.Mat4;
import minecraft.math.Vec2;
import minecraft.math.Vec3;
import minecraft.math.Vec4;
import minecraft.util.DebugUtil;
import minecraft.util.UnsafeUtil;
import sun.misc.Unsafe;

/**
 * @author Christian
 */
public class VertexAttribBuilder implements IResource {

	private static final int DEFAULT_INITIAL_CAPACITY = 64;
	
	public static final int BYTE_SIZE = 1;
	public static final int FLOAT_BYTE_SIZE = 4 * BYTE_SIZE;
	public static final int INT_BYTE_SIZE = 4 * BYTE_SIZE;
	
	private static final Unsafe UNSAFE = UnsafeUtil.getUnsafe();
	
	private final BufferLayout layout;
	
	private int currentAttribIndex;
	private int vertexCount;
	
	private long bufferAddress;
	private int bufferCapacity;
	
	private int bufferPosition;

	public VertexAttribBuilder(BufferLayout layout) {
		this(layout, DEFAULT_INITIAL_CAPACITY);
	}
	
	public VertexAttribBuilder(BufferLayout layout, int initialCapacity) {
		if (DebugUtil.PERFORM_INIT_CHECKS && layout == null)
			throw new IllegalArgumentException("layout must not be null!");
		if (DebugUtil.PERFORM_INIT_CHECKS && initialCapacity <= 0)
			throw new IllegalArgumentException("Initial capacity must be positive!");
		
		this.layout = layout;
		
		currentAttribIndex = 0;
		vertexCount = 0;

		allocAttribBuffer((long)layout.getStride() * initialCapacity);
	}
	
	private void allocAttribBuffer(long initialCapacity) throws OutOfMemoryError {
		if (DebugUtil.PERFORM_CHECKS && initialCapacity > (long)Integer.MAX_VALUE)
			throw new IllegalArgumentException("Capacity must be less than 32-bit int max value");
		
		// This is mostly an optimization, so the DirectByteBuffer
		// from the java.nio package does not have to check bounds
		// every time we want to put new data into the buffer. Note
		// that we have the ensureCapacity call before adding data
		// to the buffer so we perform an illegal memory access.
		bufferAddress = MemoryUtil.nmemAllocChecked(initialCapacity);
		bufferCapacity = (int)initialCapacity;

		bufferPosition = 0;
	}
	
	private void reallocAttribBuffer(long newCapacity) throws OutOfMemoryError {
		if (DebugUtil.PERFORM_CHECKS && newCapacity > (long)Integer.MAX_VALUE)
			throw new IllegalArgumentException("Capacity must be less than 32-bit int max value");

		bufferAddress = MemoryUtil.nmemReallocChecked(bufferAddress, newCapacity);
		bufferCapacity = (int)newCapacity;
		
		if (bufferCapacity < bufferPosition)
			bufferPosition = bufferCapacity;
	}

	private void checkClosed() throws IllegalStateException {
		if (DebugUtil.PERFORM_CHECKS && bufferAddress == MemoryUtil.NULL)
			throw new IllegalStateException("Buffer closed.");
	}
	
	private void checkAndIncrementAttrib(BufferDataType dataType, int byteSize) {
		if (DebugUtil.PERFORM_CHECKS) {
			checkClosed();
			
			if (currentAttribIndex >= layout.getAttributeCount())
				throw new IllegalStateException("No more attributes in layout!");
			
			BufferAttrib attrib = layout.getAttribute(currentAttribIndex);
			if (attrib.getDataType() != dataType || attrib.getType().getPrimitiveCount() != byteSize)
				throw new IllegalStateException("Attribute type is incorrect");
		}
		
		currentAttribIndex++;
	}
	
	private void checkVertexAlignment(int expectedAttribIndex) {
		if (DebugUtil.PERFORM_CHECKS) {
			checkClosed();
			
			if (currentAttribIndex != expectedAttribIndex)
				throw new IllegalStateException("Vertex alignment incorrect");
		}
	}

	public void next() {
		checkVertexAlignment(layout.getAttributeCount());
		
		ensureVertexCapacity();
		
		currentAttribIndex = 0;
		vertexCount++;

	}
	
	private void ensureVertexCapacity() {
		if (bufferCapacity - bufferPosition < layout.getStride()) {
			// Double the buffer capacity.
			reallocAttribBuffer(bufferCapacity << 1);
		}
	}
	
	public void putFloat(float value) {
		checkAndIncrementAttrib(BufferDataType.FLOAT, 1);
		
		putFloatUnsafe(value);
	}

	public void putFloat2(Vec2 vec) {
		putFloat2(vec.x, vec.y);
	}

	public void putFloat2(float x, float y) {
		checkAndIncrementAttrib(BufferDataType.FLOAT, 2);
		
		putFloatUnsafe(x);
		putFloatUnsafe(y);
	}
	
	public void putFloat3(Vec3 vec) {
		putFloat3(vec.x, vec.y, vec.z);
	}
	
	public void putFloat3(float x, float y, float z) {
		checkAndIncrementAttrib(BufferDataType.FLOAT, 3);
		
		putFloatUnsafe(x);
		putFloatUnsafe(y);
		putFloatUnsafe(z);
	}
	
	public void putFloat4(Vec4 vec) {
		putFloat4(vec.x, vec.y, vec.z, vec.w);
	}
	
	public void putFloat4(float x, float y, float z, float w) {
		checkAndIncrementAttrib(BufferDataType.FLOAT, 4);
		
		putFloatUnsafe(x);
		putFloatUnsafe(y);
		putFloatUnsafe(z);
		putFloatUnsafe(w);
	}
	
	public void putMat3(Mat3 mat) {
		checkAndIncrementAttrib(BufferDataType.FLOAT, 3 * 3);
		
		putFloatUnsafe(mat.m00);
		putFloatUnsafe(mat.m10);
		putFloatUnsafe(mat.m20);

		putFloatUnsafe(mat.m01);
		putFloatUnsafe(mat.m11);
		putFloatUnsafe(mat.m21);

		putFloatUnsafe(mat.m02);
		putFloatUnsafe(mat.m12);
		putFloatUnsafe(mat.m22);
	}

	public void putMat4(Mat4 mat) {
		checkAndIncrementAttrib(BufferDataType.FLOAT, 4 * 4);
		
		putFloatUnsafe(mat.m00);
		putFloatUnsafe(mat.m10);
		putFloatUnsafe(mat.m20);
		putFloatUnsafe(mat.m30);

		putFloatUnsafe(mat.m01);
		putFloatUnsafe(mat.m11);
		putFloatUnsafe(mat.m21);
		putFloatUnsafe(mat.m31);

		putFloatUnsafe(mat.m02);
		putFloatUnsafe(mat.m12);
		putFloatUnsafe(mat.m22);
		putFloatUnsafe(mat.m32);

		putFloatUnsafe(mat.m03);
		putFloatUnsafe(mat.m13);
		putFloatUnsafe(mat.m23);
		putFloatUnsafe(mat.m33);
	}
	
	public void putInt(int value) {
		checkAndIncrementAttrib(BufferDataType.INT, 1);
		
		putIntUnsafe(value);
	}

	public void putInt2(int x, int y) {
		checkAndIncrementAttrib(BufferDataType.INT, 2);
		
		putIntUnsafe(x);
		putIntUnsafe(y);
	}

	public void putInt3(int x, int y, int z) {
		checkAndIncrementAttrib(BufferDataType.INT, 3);
		
		putIntUnsafe(x);
		putIntUnsafe(y);
		putIntUnsafe(z);
	}

	public void putInt4(int x, int y, int z, int w) {
		checkAndIncrementAttrib(BufferDataType.INT, 4);
		
		putIntUnsafe(x);
		putIntUnsafe(y);
		putIntUnsafe(z);
		putIntUnsafe(w);
	}

	public void putUInt(int value) {
		checkAndIncrementAttrib(BufferDataType.UINT, 1);
		
		putIntUnsafe(value);
	}
	
	public void putUInt2(int x, int y) {
		checkAndIncrementAttrib(BufferDataType.UINT, 2);
		
		putIntUnsafe(x);
		putIntUnsafe(y);
	}
	
	public void putUInt3(int x, int y, int z) {
		checkAndIncrementAttrib(BufferDataType.UINT, 3);
		
		putIntUnsafe(x);
		putIntUnsafe(y);
		putIntUnsafe(z);
	}
	
	public void putUInt4(int x, int y, int z, int w) {
		checkAndIncrementAttrib(BufferDataType.UINT, 4);
		
		putIntUnsafe(x);
		putIntUnsafe(y);
		putIntUnsafe(z);
		putIntUnsafe(w);
	}
	
	public void putByte(byte value) {
		checkAndIncrementAttrib(BufferDataType.BYTE, 1);
		
		putByteUnsafe(value);
	}

	public void putByte2(byte x, byte y) {
		checkAndIncrementAttrib(BufferDataType.BYTE, 2);
		
		putByteUnsafe(x);
		putByteUnsafe(y);
	}

	public void putByte3(byte x, byte y, byte z) {
		checkAndIncrementAttrib(BufferDataType.BYTE, 3);
		
		putByteUnsafe(x);
		putByteUnsafe(y);
		putByteUnsafe(z);
	}

	public void putByte4(byte x, byte y, byte z, byte w) {
		checkAndIncrementAttrib(BufferDataType.BYTE, 4);
		
		putByteUnsafe(x);
		putByteUnsafe(y);
		putByteUnsafe(z);
		putByteUnsafe(w);
	}

	public void putUByte(byte value) {
		checkAndIncrementAttrib(BufferDataType.UBYTE, 1);
		
		putByteUnsafe(value);
	}
	
	public void putUByte2(byte x, byte y) {
		checkAndIncrementAttrib(BufferDataType.UBYTE, 2);
		
		putByteUnsafe(x);
		putByteUnsafe(y);
	}
	
	public void putUByte3(byte x, byte y, byte z) {
		checkAndIncrementAttrib(BufferDataType.UBYTE, 3);
		
		putByteUnsafe(x);
		putByteUnsafe(y);
		putByteUnsafe(z);
	}
	
	public void putUByte4(byte x, byte y, byte z, byte w) {
		checkAndIncrementAttrib(BufferDataType.UBYTE, 4);
		
		putByteUnsafe(x);
		putByteUnsafe(y);
		putByteUnsafe(z);
		putByteUnsafe(w);
	}
	
	public void putBool(boolean value) {
		checkAndIncrementAttrib(BufferDataType.BOOL, 1);
		
		putByteUnsafe((byte)(value ? 0x01 : 0x00));
	}
	
	private void putFloatUnsafe(float value) {
		UNSAFE.putFloat(bufferAddress + bufferPosition, value);
		bufferPosition += FLOAT_BYTE_SIZE;
	}

	private void putIntUnsafe(int value) {
		UNSAFE.putInt(bufferAddress + bufferPosition, value);
		bufferPosition += INT_BYTE_SIZE;
	}

	private void putByteUnsafe(byte value) {
		UNSAFE.putByte(bufferAddress + bufferPosition, value);
		bufferPosition += BYTE_SIZE;
	}
	
	public void clear() {
		checkClosed();
		
		currentAttribIndex = 0;
		vertexCount = 0;

		bufferPosition = 0;
	}

	public ByteBuffer getReadbleBuffer() {
		if (DebugUtil.PERFORM_CHECKS && (bufferPosition % layout.getStride()) != 0)
			throw new IllegalStateException("Attributes have missing data!");
		
		ByteBuffer buffer = MemoryUtil.memByteBuffer(bufferAddress, bufferPosition);
		
		return buffer.asReadOnlyBuffer();
	}
	
	public BufferLayout getLayout() {
		return layout;
	}

	public int getSize() {
		return bufferPosition;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	@Override
	public void dispose() {
		if (bufferAddress != MemoryUtil.NULL) {
			MemoryUtil.nmemFree(bufferAddress);
			
			bufferAddress = MemoryUtil.NULL;
			bufferCapacity = 0;

			bufferPosition = 0;
		}
	}
}
