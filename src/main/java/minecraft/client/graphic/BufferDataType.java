package minecraft.client.graphic;

public enum BufferDataType {

	FLOAT(4),
	
	INT(4),
	UINT(4),
	
	BYTE(1),
	UBYTE(1),
	
	BOOL(1);
	
	private final int byteSize;
	
	private BufferDataType(int byteSize) {
		this.byteSize = byteSize;
	}
	
	public int getByteSize() {
		return byteSize;
	}
}
