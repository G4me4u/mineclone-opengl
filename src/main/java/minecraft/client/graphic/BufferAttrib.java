package minecraft.client.graphic;

public final class BufferAttrib {

	private final String name;
	private final BufferAttribType type;
	private final BufferDataType dataType;
	private final boolean normalized;
	
	private int offset;

	public BufferAttrib(String name, BufferAttribType type) {
		this(name, type, type.getInternalType());
	}

	public BufferAttrib(String name, BufferAttribType type, BufferDataType dataType) {
		this(name, type, dataType, false);
	}
	
	public BufferAttrib(String name, BufferAttribType type, BufferDataType dataType, boolean normalized) {
		this.name = name;
		this.type = type;
		this.dataType = dataType;
		this.normalized = normalized;
		
		offset = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public BufferAttribType getType() {
		return type;
	}

	public BufferDataType getDataType() {
		return dataType;
	}
	
	public int getSizeInBuffer() {
		return type.getPrimitiveCount() * dataType.getByteSize();
	}
	
	public boolean isNormalized() {
		return normalized;
	}
	
	public int getBufferOffset() {
		return offset;
	}
	
	void setBufferOffset(int offset) {
		if (offset < 0)
			throw new IllegalArgumentException("offset must be non-negative!");
		
		this.offset = offset;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		
		hash += 31 * hash + name.hashCode();
		hash += 31 * hash + type.hashCode();
		hash += 31 * hash + dataType.hashCode();
		hash += 31 * hash + Boolean.hashCode(normalized);
		
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		
		if (!(other instanceof BufferAttrib))
			return false;
		
		BufferAttrib attrib = (BufferAttrib)other;
		
		if (!attrib.name.equals(name))
			return false;
		if (attrib.type != type)
			return false;
		if (attrib.dataType != dataType)
			return false;
		if (attrib.normalized != normalized)
			return false;
		
		return true;
	}
}
