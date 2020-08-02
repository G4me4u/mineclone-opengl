package minecraft.client.graphic;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class BufferLayout implements Iterable<BufferAttrib> {

	private final BufferAttrib[] attributes;
	
	private final int stride;
	
	public BufferLayout(BufferAttrib... attributes) {
		this.attributes = attributes;

		stride = calculateAndSetOffsets(attributes);
	}
	
	private static int calculateAndSetOffsets(BufferAttrib[] attributes) {
		int offset = 0;
	
		for (BufferAttrib attrib : attributes) {
			attrib.setBufferOffset(offset);
			
			offset += attrib.getSizeInBuffer();
		}
		
		return offset;
	}

	public BufferAttrib getAttribute(int attribIndex) {
		return attributes[attribIndex];
	}

	public int getAttributeCount() {
		return attributes.length;
	}
	
	public int getStride() {
		return stride;
	}
	
	@Override
	public Iterator<BufferAttrib> iterator() {
		return new Iterator<BufferAttrib>() {
			
			private int index = 0;
			
			@Override
			public BufferAttrib next() {
				if (index >= attributes.length)
					throw new NoSuchElementException();
				return attributes[index++];
			}
			
			@Override
			public boolean hasNext() {
				return (index < attributes.length);
			}
		};
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		
		hash += hash * 31 + Arrays.hashCode(attributes);
		hash += hash * 31 + Integer.hashCode(stride);
		
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		
		if (!(other instanceof BufferLayout))
			return false;
		
		BufferLayout layout = (BufferLayout)other;
		
		if (layout.stride != stride)
			return false;
		if (!Arrays.equals(layout.attributes, attributes))
			return false;
	
		return true;
	}
}
